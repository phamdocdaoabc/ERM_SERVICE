package vn.ducbackend.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ducbackend.client.SystemClient;
import vn.ducbackend.domain.dto.*;
import vn.ducbackend.domain.dto.causesCategory.CauseCategoryDetailRequest;
import vn.ducbackend.domain.dto.causesCategory.CauseCategoryDetailResponse;
import vn.ducbackend.domain.dto.causesCategory.CauseCategorySearchRequest;
import vn.ducbackend.domain.dto.causesCategory.CauseCategoryUpdateDTO;
import vn.ducbackend.domain.entity.CauseCategories;
import vn.ducbackend.domain.entity.SystemCauseCategories;
import vn.ducbackend.exception.customException.DuplicateException;
import vn.ducbackend.exception.customException.NotFoundException;
import vn.ducbackend.mapper.CauseCategoryMapper;
import vn.ducbackend.repository.CauseCategoryRepository;
import vn.ducbackend.repository.SystemCauseCategoryRepository;
import vn.ducbackend.repository.specs.CauseCategorySpecs;
import vn.ducbackend.service.CauseCategoryService;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class CauseCategoryServiceImpl implements CauseCategoryService {
    private final CauseCategoryRepository causeCategoryRepository;
    private final SystemCauseCategoryRepository systemCauseCategoryRepository;
    private final CauseCategoryMapper causeCategoryMapper;
    private final SystemClient systemClient;

    // Thêm phân loại nguyên nhân
    @Override
    @Transactional
    public Long create(CauseCategoryDetailRequest request) {
        // Check code trùng
        if (causeCategoryRepository.existsCauseCategoriesByCodeOrName(request.getCode(), request.getName())) {
            throw new DuplicateException("Code [" + request.getCode() + "] or Name [" + request.getName() + "] already exists");
        }

        CauseCategories causeCategories = causeCategoryMapper.toCauseCategory(request);
        causeCategoryRepository.save(causeCategories);

        List<SystemCauseCategories> links = request.getSystemIds().stream()
                .map(systemId -> {
                    SystemCauseCategories link = new SystemCauseCategories();
                    link.setCauseCategoryId(causeCategories.getId());
                    link.setSystemId(systemId);
                    return link;
                })
                .toList();
        systemCauseCategoryRepository.saveAll(links);
        return causeCategories.getId();
    }

    // lấy chi tiết phân loại nguyên nhân
    @Override
    public CauseCategoryDetailResponse getCauseCategory(Long categoryId) {
        CauseCategories causeCategories = causeCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("CauseCategory not found with id: " + categoryId));

        List<SystemCauseCategories> links = systemCauseCategoryRepository.findByCauseCategoryId(categoryId);
        List<Long> systemIds = links.stream()
                .map(SystemCauseCategories::getSystemId)
                .toList();
        // Call API System
        List<LinkResponse> systemResponseList = systemClient.getAllSystems(systemIds).getData().getContent();

        CauseCategoryDetailResponse dto = causeCategoryMapper.toDetailDTO(causeCategories);
        dto.setSystemIds(systemResponseList);
        return dto;
    }

    // all list causeCategories
    @Override
    public Page<CauseCategoryDetailResponse> getListCauseCategory(Pageable pageable, Set<Long> ids) {
        Page<CauseCategories> causeCategories;
        if (ids != null && !ids.isEmpty()) {
            causeCategories = causeCategoryRepository.findByIdIn(ids, pageable);
        } else {
            causeCategories = causeCategoryRepository.findAll(pageable);
        }
        return causeCategories.map(causeCategory -> {
            // map sang DTO
            CauseCategoryDetailResponse dto = causeCategoryMapper.toDetailDTO(causeCategory);

            // lấy links
            List<SystemCauseCategories> links = systemCauseCategoryRepository.findByCauseCategoryId(dto.getId());
            List<Long> systemIds = links.stream()
                    .map(SystemCauseCategories::getSystemId)
                    .toList();

            // Call API System
            List<LinkResponse> systemResponseList = systemClient.getAllSystems(systemIds).getData().getContent();

            dto.setSystemIds(systemResponseList);
            return dto;
        });
    }

    @Override
    @Transactional
    public Long update(CauseCategoryUpdateDTO request) {
        CauseCategories causeCategories = causeCategoryRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("CauseCategory not found with id: " + request.getId()));

        // check trùng tên
        if (causeCategoryRepository.existsByNameAndIdNot(request.getName(), request.getId())) {
            throw new DuplicateException("CauseCategory with name '" + request.getName() + "' already exists.");
        }

        causeCategoryMapper.updateCauseCategoryFromDto(request, causeCategories);
        causeCategoryRepository.save(causeCategories);

        // Xóa các mapping cũ trước khi thêm mới để tránh trùng (nếu yêu cầu business cần)
        systemCauseCategoryRepository.deleteByCauseCategoryId(request.getId());

        List<SystemCauseCategories> links = request.getSystemIds().stream()
                .map(systemId -> {
                    SystemCauseCategories link = new SystemCauseCategories();
                    link.setCauseCategoryId(causeCategories.getId());
                    link.setSystemId(systemId);
                    return link;
                })
                .toList();
        systemCauseCategoryRepository.saveAll(links);
        // Trả về DTO đã update (có thể dùng mapper hoặc build thủ công)
        return causeCategories.getId();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // Kiểm tra tồn tại
        if (!causeCategoryRepository.existsById(id)) {
            throw new RuntimeException("CauseCategory not found with id: " + id);
        }
        // Xóa mapping trước
        systemCauseCategoryRepository.deleteByCauseCategoryId(id);

        // Xóa CauseCategory
        causeCategoryRepository.deleteById(id);
    }

    @Override
    public Page<CauseCategoryDetailResponse> searchCauseCategory(Pageable pageable, CauseCategorySearchRequest request) {
        Specification<CauseCategories> spec = Specification
                .where(CauseCategorySpecs.hasCode(request.getCode()))
                .and(CauseCategorySpecs.hasName(request.getName()))
                .and(CauseCategorySpecs.hasSystemId(request.getSystemId()));

        Page<CauseCategories> causeCategories = causeCategoryRepository.findAll(spec, pageable);

        return causeCategories.map(causeCategory -> {
            // map sang DTO
            CauseCategoryDetailResponse dto = causeCategoryMapper.toDetailDTO(causeCategory);

            // lấy links
            List<SystemCauseCategories> links = systemCauseCategoryRepository.findByCauseCategoryId(dto.getId());
            List<Long> systemIds = links.stream()
                    .map(SystemCauseCategories::getSystemId)
                    .toList();

            // Call API System
            List<LinkResponse> systemResponseList = systemClient.getAllSystems(systemIds).getData().getContent();

            dto.setSystemIds(systemResponseList);
            return dto;
        });
    }
}
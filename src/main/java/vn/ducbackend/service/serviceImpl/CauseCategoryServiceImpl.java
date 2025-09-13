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
import vn.ducbackend.domain.entity.CauseCategory;
import vn.ducbackend.domain.entity.SystemCauseCategory;
import vn.ducbackend.exception.customException.DuplicateException;
import vn.ducbackend.exception.customException.NotFoundException;
import vn.ducbackend.mapper.CauseCategoryMapper;
import vn.ducbackend.repository.CauseCategoryRepository;
import vn.ducbackend.repository.SystemCauseCategoryRepository;
import vn.ducbackend.repository.specs.CauseCategorySpecs;
import vn.ducbackend.service.CauseCategoryService;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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

        CauseCategory causeCategory = causeCategoryMapper.toCauseCategory(request);
        causeCategoryRepository.save(causeCategory);

        List<SystemCauseCategory> systemCauseCategorys = request.getSystemIds().stream()
                .map(system -> {
                    SystemCauseCategory link = new SystemCauseCategory();
                    link.setCauseCategoryId(causeCategory.getId());
                    link.setSystemId(system.getId());
                    return link;
                })
                .toList();
        systemCauseCategoryRepository.saveAll(systemCauseCategorys);
        return causeCategory.getId();
    }

    // lấy chi tiết phân loại nguyên nhân
    @Override
    public CauseCategoryDetailResponse getCauseCategory(Long categoryId) {
        CauseCategory causeCategory = causeCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("CauseCategory not found with id: " + categoryId));

        List<SystemCauseCategory> systemCauseCategorys = systemCauseCategoryRepository.findByCauseCategoryId(categoryId);
        Set<Long> systemIds = systemCauseCategorys.stream()
                .map(SystemCauseCategory::getSystemId)
                .filter(Objects::nonNull) // tránh null
                .collect(Collectors.toSet());
        // Call API System
        List<BasicInfoDTO> systemResponseList = systemClient.getAllSystems(systemIds).getData().getContent();

        CauseCategoryDetailResponse dto = causeCategoryMapper.toDetailDTO(causeCategory);
        dto.setSystemIds(systemResponseList);
        return dto;
    }

    // all list causeCategories
    @Override
    public Page<CauseCategoryDetailResponse> getListCauseCategory(Pageable pageable, Set<Long> ids) {
        Page<CauseCategory> causeCategories;
        if (ids != null && !ids.isEmpty()) {
            causeCategories = causeCategoryRepository.findByIdIn(ids, pageable);
        } else {
            causeCategories = causeCategoryRepository.findAll(pageable);
        }
        return causeCategories.map(causeCategory -> {
            // map sang DTO
            CauseCategoryDetailResponse dto = causeCategoryMapper.toDetailDTO(causeCategory);

            // lấy systemCauseCategorys
            List<SystemCauseCategory> systemCauseCategorys = systemCauseCategoryRepository.findByCauseCategoryId(dto.getId());
            Set<Long> systemIds = systemCauseCategorys.stream()
                    .map(SystemCauseCategory::getSystemId)
                    .filter(Objects::nonNull) // tránh null
                    .collect(Collectors.toSet());

            // Call API System
            List<BasicInfoDTO> systemResponseList = systemClient.getAllSystems(systemIds).getData().getContent();

            dto.setSystemIds(systemResponseList);
            return dto;
        });
    }

    @Override
    @Transactional
    public Long update(CauseCategoryDetailRequest request) {
        CauseCategory causeCategory = causeCategoryRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("CauseCategory not found with id: " + request.getId()));

        // check trùng tên
        if (causeCategoryRepository.existsByNameAndIdNot(request.getName(), request.getId())) {
            throw new DuplicateException("CauseCategory with name '" + request.getName() + "' already exists.");
        }

        causeCategoryMapper.updateCauseCategoryFromDto(request, causeCategory);
        causeCategoryRepository.save(causeCategory);

        // Xóa các mapping cũ trước khi thêm mới để tránh trùng (nếu yêu cầu business cần)
        systemCauseCategoryRepository.deleteByCauseCategoryId(request.getId());

        List<SystemCauseCategory> systemCauseCategorys = request.getSystemIds().stream()
                .map(system -> {
                    SystemCauseCategory link = new SystemCauseCategory();
                    link.setCauseCategoryId(causeCategory.getId());
                    link.setSystemId(system.getId());
                    return link;
                })
                .toList();
        systemCauseCategoryRepository.saveAll(systemCauseCategorys);
        // Trả về DTO đã update (có thể dùng mapper hoặc build thủ công)
        return causeCategory.getId();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // Kiểm tra tồn tại
        if (!causeCategoryRepository.existsById(id)) {
            throw new NotFoundException("CauseCategory not found with id: " + id);
        }
        // Xóa mapping trước
        systemCauseCategoryRepository.deleteByCauseCategoryId(id);

        // Xóa CauseCategory
        causeCategoryRepository.deleteById(id);
    }

    @Override
    public Page<CauseCategoryDetailResponse> searchCauseCategory(Pageable pageable, CauseCategorySearchRequest request) {
        Specification<CauseCategory> spec = Specification
                .where(CauseCategorySpecs.hasCode(request.getCode()))
                .and(CauseCategorySpecs.hasName(request.getName()))
                .and(CauseCategorySpecs.hasSystemId(request.getSystemId()));

        Page<CauseCategory> causeCategories = causeCategoryRepository.findAll(spec, pageable);

        return causeCategories.map(causeCategory -> {
            // map sang DTO
            CauseCategoryDetailResponse dto = causeCategoryMapper.toDetailDTO(causeCategory);

            // lấy systemCauseCategorys
            List<SystemCauseCategory> systemCauseCategorys = systemCauseCategoryRepository.findByCauseCategoryId(dto.getId());
            Set<Long> systemIds = systemCauseCategorys.stream()
                    .map(SystemCauseCategory::getSystemId)
                    .filter(Objects::nonNull) // tránh null
                    .collect(Collectors.toSet());

            // Call API System
            List<BasicInfoDTO> systemResponseList = systemClient.getAllSystems(systemIds).getData().getContent();

            dto.setSystemIds(systemResponseList);
            return dto;
        });
    }
}
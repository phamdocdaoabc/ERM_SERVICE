package vn.ducbackend.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.ducbackend.domain.dto.*;
import vn.ducbackend.domain.entity.CauseCategories;
import vn.ducbackend.domain.entity.SystemCauseCategories;
import vn.ducbackend.domain.entity.Systems;
import vn.ducbackend.mapper.CauseCategoryMapper;
import vn.ducbackend.mapper.SystemMapper;
import vn.ducbackend.repository.CauseCategoryRepository;
import vn.ducbackend.repository.SystemCauseCategoryRepository;
import vn.ducbackend.repository.SystemRepository;
import vn.ducbackend.service.CauseCategoryService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CauseCategoryServiceImpl implements CauseCategoryService {
    private final CauseCategoryRepository causeCategoryRepository;
    private final SystemRepository systemRepository;
    private final SystemCauseCategoryRepository systemCauseCategoryRepository;
    private final CauseCategoryMapper causeCategoryMapper;
    private final SystemMapper systemMapper;

    // Thêm phân loại nguyên nhân
    @Override
    @Transactional
    public CauseCategoryDetailResponse createCauseCategory(CauseCategoryDetailRequest request) {
        CauseCategories causeCategories = causeCategoryMapper.toCauseCategory(request);
        causeCategories = causeCategoryRepository.save(causeCategories);
        // map
        CauseCategoryDetailResponse dto = causeCategoryMapper.toDetailDTO(causeCategories);
        // Lưu quan hệ vào bảng trung gian
        List<SystemCauseCategories> links = new ArrayList<>();
        for (Long systemId : request.getSystemIds()) {
            // optional: check systemId có tồn tại không
            if (!systemRepository.existsById(systemId)) {
                throw new RuntimeException("System not found: " + systemId);
            }

            SystemCauseCategories link = new SystemCauseCategories();
            link.setCauseCategoryId(causeCategories.getId());
            link.setSystemId(systemId);
            link = systemCauseCategoryRepository.save(link);
            links.add(link);
        }
        List<Long> systemIds = links.stream()
                .map(SystemCauseCategories::getSystemId)
                .toList();
        List<Systems> systems = systemRepository.findAllById(systemIds);
        // map systems sang DTO
        List<SystemResponse> systemResponses = systems.stream()
                .map(systemMapper::toSystemResponse)
                .toList();
        dto.setSystemIds(systemResponses);

        return dto;
    }

    // lấy chi tiết phân loại nguyên nhân
    @Override
    public CauseCategoryDetailResponse getDetail(Long categoryId) {
        CauseCategories causeCategories = causeCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Not found"));

        List<SystemCauseCategories> links = systemCauseCategoryRepository.findByCauseCategoryId(categoryId);
        List<Long> systemIds = links.stream()
                .map(SystemCauseCategories::getSystemId)
                .toList();
        List<Systems> systems = systemRepository.findAllById(systemIds);
        // map systems sang DTO
        List<SystemResponse> systemResponses = systems.stream()
                .map(systemMapper::toSystemResponse)
                .toList();

        CauseCategoryDetailResponse dto = causeCategoryMapper.toDetailDTO(causeCategories);
        dto.setSystemIds(systemResponses);
        return dto;
    }

    // all list causeCategories
    @Override
    public Page<CauseCategoryDetailResponse> getAllCauseCategory(Pageable pageable) {
        var causeCategories = causeCategoryRepository.findAll(pageable);

        return causeCategories.map(causeCategory -> {
            // map sang DTO
            CauseCategoryDetailResponse dto = causeCategoryMapper.toDetailDTO(causeCategory);

            // lấy links
            List<SystemCauseCategories> links = systemCauseCategoryRepository.findByCauseCategoryId(dto.getId());
            List<Long> systemIds = links.stream()
                    .map(SystemCauseCategories::getSystemId)
                    .toList();

            List<Systems> systems = systemRepository.findAllById(systemIds);
            List<SystemResponse> systemResponses = systems.stream()
                    .map(systemMapper::toSystemResponse)
                    .toList();

            dto.setSystemIds(systemResponses);
            return dto;
        });
    }

    @Override
    @Transactional
    public CauseCategoryUpdateDTO updateCauseCategory(CauseCategoryUpdateDTO request){
        CauseCategories causeCategories = causeCategoryRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("CauseCategory not found with id: " + request.getId()));

        // check trùng tên
        if (causeCategoryRepository.existsByNameAndIdNot(request.getName(), request.getId())) {
            throw new RuntimeException("CauseCategory with name '" + request.getName() + "' already exists.");
        }

        causeCategoryMapper.updateCauseCategoryFromDto(request, causeCategories);
        causeCategories = causeCategoryRepository.save(causeCategories);

        // Xóa các mapping cũ trước khi thêm mới để tránh trùng (nếu yêu cầu business cần)
        systemCauseCategoryRepository.deleteByCauseCategoryId(request.getId());

        for (Long systemId : request.getSystemIds()){
            if (!systemRepository.existsById(systemId)){
                throw new RuntimeException("System not found: "+systemId);
            }
            SystemCauseCategories link = new SystemCauseCategories();
            link.setCauseCategoryId(causeCategories.getId());
            link.setSystemId(systemId);
            systemCauseCategoryRepository.save(link);
        }
        // Trả về DTO đã update (có thể dùng mapper hoặc build thủ công)
        return causeCategoryMapper.toUpdateDto(causeCategories, request.getSystemIds());
    }

    @Override
    @Transactional
    public void deleteCauseCategory(Long id) {
        // Kiểm tra tồn tại
        if (!causeCategoryRepository.existsById(id)) {
            throw new RuntimeException("CauseCategory not found with id: " + id);
        }

        // Xóa mapping trước
        systemCauseCategoryRepository.deleteByCauseCategoryId(id);

        // Xóa CauseCategory
        causeCategoryRepository.deleteById(id);
    }

}
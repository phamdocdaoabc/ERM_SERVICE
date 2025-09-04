package vn.ducbackend.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ducbackend.client.SystemClient;
import vn.ducbackend.domain.dto.*;
import vn.ducbackend.domain.entity.CauseCategories;
import vn.ducbackend.domain.entity.RiskCategories;
import vn.ducbackend.domain.entity.SystemCauseCategories;
import vn.ducbackend.domain.entity.SystemRiskCategories;
import vn.ducbackend.exception.customException.DuplicateException;
import vn.ducbackend.exception.customException.NotFoundException;
import vn.ducbackend.mapper.RiskCategoryMapper;
import vn.ducbackend.repository.RiskCategoryRepository;
import vn.ducbackend.repository.SystemRiskCategoryRepository;
import vn.ducbackend.service.RiskCategoryService;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class RiskCategoryServiceImpl implements RiskCategoryService {
    private final RiskCategoryRepository riskCategoryRepository;
    private final RiskCategoryMapper riskCategoryMapper;
    private final SystemRiskCategoryRepository systemRiskCategoryRepository;
    private final SystemClient systemClient;

    @Override
    @Transactional
    public Long create(RiskCategoryRequest riskCategoryRequest) {
        // Check code trùng
        if (riskCategoryRepository.existsRiskCategoriesByCodeOrName(riskCategoryRequest.getCode(), riskCategoryRequest.getName())) {
            throw new DuplicateException("Code [" + riskCategoryRequest.getCode() + "] or Name [" + riskCategoryRequest.getName() + "] already exists");
        }

        RiskCategories riskCategories = riskCategoryMapper.toRiskCategories(riskCategoryRequest);
        riskCategoryRepository.save(riskCategories);

        List<SystemRiskCategories> links = riskCategoryRequest.getSystemIds().stream()
                .map(systemId -> {
                    SystemRiskCategories link = new SystemRiskCategories();
                    link.setRiskCategoryId(riskCategories.getId());
                    link.setSystemId(systemId);
                    return link;
                })
                .toList();
        systemRiskCategoryRepository.saveAll(links);
        return riskCategories.getId();
    }

    @Override
    public Page<RiskCategoryDetailResponse> getListRiskCategory(Pageable pageable, Set<Long> ids) {
        Page<RiskCategories> riskCategories;
        if (ids != null && !ids.isEmpty()) {
            riskCategories = riskCategoryRepository.findByIdIn(ids, pageable);
        } else {
            riskCategories = riskCategoryRepository.findAll(pageable);
        }
        return riskCategories.map(riskCategory -> {
            // map sang DTO
            RiskCategoryDetailResponse dto = riskCategoryMapper.toDetailDTO(riskCategory);

            // lấy links
            List<SystemRiskCategories> links = systemRiskCategoryRepository.findByRiskCategoryId(dto.getId());
            List<Long> systemIds = links.stream()
                    .map(SystemRiskCategories::getSystemId)
                    .toList();

            // Call API System
            List<LinkResponse> systemResponseList = systemClient.getAllSystems(systemIds).getData().getContent();

            dto.setSystemIds(systemResponseList);
            return dto;
        });
    }

    @Override
    public RiskCategoryDetailResponse getRiskCategory(Long id) {
        RiskCategories riskCategories = riskCategoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("RiskCategory not found with id: " + id));

        List<SystemRiskCategories> links = systemRiskCategoryRepository.findByRiskCategoryId(id);
        List<Long> systemIds = links.stream()
                .map(SystemRiskCategories::getSystemId)
                .toList();
        // Call API System
        List<LinkResponse> systemResponseList = systemClient.getAllSystems(systemIds).getData().getContent();

        RiskCategoryDetailResponse dto = riskCategoryMapper.toDetailDTO(riskCategories);
        dto.setSystemIds(systemResponseList);
        return dto;
    }

    @Override
    public void delete(Long id) {
        // Kiểm tra tồn tại
        if (!riskCategoryRepository.existsById(id)) {
            throw new RuntimeException("RiskCategory not found with id: " + id);
        }
        // Xóa mapping trước
        systemRiskCategoryRepository.deleteByRiskCategoryId(id);

        // Xóa CauseCategory
        riskCategoryRepository.deleteById(id);
    }

    @Override
    public Long update(RiskCategoryUpdateDTO request) {
        RiskCategories riskCategories = riskCategoryRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("Risk category not found with id: " + request.getId()));

        // check trùng tên
        if (riskCategoryRepository.existsByNameAndIdNot(request.getName(), request.getId())) {
            throw new DuplicateException("Risk category with name '" + request.getName() + "' already exists.");
        }

        riskCategoryMapper.updateRiskCategoryFromDto(request, riskCategories);
        riskCategoryRepository.save(riskCategories);

        // Xóa các mapping cũ trước khi thêm mới để tránh trùng (nếu yêu cầu business cần)
        systemRiskCategoryRepository.deleteByRiskCategoryId(request.getId());

        List<SystemRiskCategories> links = request.getSystemIds().stream()
                .map(systemId -> {
                    SystemRiskCategories link = new SystemRiskCategories();
                    link.setRiskCategoryId(riskCategories.getId());
                    link.setSystemId(systemId);
                    return link;
                })
                .toList();
        systemRiskCategoryRepository.saveAll(links);
        // Trả về DTO đã update (có thể dùng mapper hoặc build thủ công)
        return riskCategories.getId();
    }
}

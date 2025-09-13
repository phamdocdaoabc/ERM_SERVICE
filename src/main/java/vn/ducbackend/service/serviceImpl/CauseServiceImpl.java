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
import vn.ducbackend.domain.dto.causes.CauseDetailResponse;
import vn.ducbackend.domain.dto.causes.CauseRequest;
import vn.ducbackend.domain.dto.causes.CauseSearchRequest;
import vn.ducbackend.domain.dto.causes.CauseUpdateDTO;
import vn.ducbackend.domain.entity.Cause;
import vn.ducbackend.domain.entity.SystemCause;
import vn.ducbackend.exception.customException.DuplicateException;
import vn.ducbackend.exception.customException.NotFoundException;
import vn.ducbackend.mapper.CauseMapper;
import vn.ducbackend.repository.CauseCategoryRepository;
import vn.ducbackend.repository.CauseRepository;
import vn.ducbackend.repository.SystemCauseRepository;
import vn.ducbackend.repository.specs.CauseSpecs;
import vn.ducbackend.service.CauseService;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CauseServiceImpl implements CauseService {
    private final CauseRepository causeRepository;
    private final CauseMapper causeMapper;
    private final SystemCauseRepository systemCauseRepository;
    private final CauseCategoryRepository causeCategoryRepository;
    private final SystemClient systemClient;


    @Override
    @Transactional
    public Long create(CauseRequest causeRequest) {
        // Check code trùng
        if (causeRepository.existsCausesByCodeOrName(causeRequest.getName(), causeRequest.getCode())) {
            throw new DuplicateException("Code [" + causeRequest.getCode() + "] or Name [" + causeRequest.getName() + "] already exists");
        }
        if(!causeCategoryRepository.existsById(causeRequest.getCauseCategoryId())){
            throw new NotFoundException("CauseCategory not found with id: " + causeRequest.getCauseCategoryId());
        }
        Cause cause = causeMapper.toCauses(causeRequest);
        causeRepository.save(cause);

        // Lưu quan hệ vào bảng trung gian
        List<SystemCause> systemCauses = causeRequest.getSystemIds().stream()
                .map(systemId -> {
                    SystemCause link = new SystemCause();
                    link.setCauseId(cause.getId());
                    link.setSystemId(systemId);
                    return link;
                })
                .toList();
        systemCauseRepository.saveAll(systemCauses);
        return cause.getId();
    }

    // list ALL Cause
    @Override
    public Page<CauseDetailResponse> getListCause(Pageable pageable, Set<Long> ids) {
        Page<Cause> causes;
        if (ids != null && !ids.isEmpty()) {
            causes = causeRepository.findByIdIn(ids, pageable);
        } else {
            causes = causeRepository.findAll(pageable);
        }
        return causes.map(cause -> {
            // map sang DTO
            CauseDetailResponse dto = causeMapper.toDetailDTO(cause);

            // lấy systemCauses
            List<SystemCause> systemCauses = systemCauseRepository.findByCauseId(dto.getId());
            Set<Long> systemIds = systemCauses.stream()
                    .map(SystemCause::getSystemId)
                    .filter(Objects::nonNull) // tránh null
                    .collect(Collectors.toSet());

            // Call API System
            List<BasicInfoDTO> systemResponseList = systemClient.getAllSystems(systemIds).getData().getContent();
            dto.setSystemIds(systemResponseList);

            // check causeCategory
            BasicInfoDTO causeCategory = causeMapper.toLinkDTO(causeRepository.findById(cause.getId())
                    .orElseThrow(() -> new NotFoundException("Causes not found with id: " + cause.getId())));
            dto.setCauseCategory(causeCategory);
            return dto;
        });
    }

    @Override
    public CauseDetailResponse getCause(Long id) {
        Cause cause = causeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Causes not found with id :"+id));
        List<SystemCause> systemCauses = systemCauseRepository.findByCauseId(id);
        Set<Long> systemIds = systemCauses.stream()
                .map(SystemCause::getSystemId)
                .filter(Objects::nonNull) // tránh null
                .collect(Collectors.toSet());
        // Call API System
        List<BasicInfoDTO> systemResponseList = systemClient.getAllSystems(systemIds).getData().getContent();

        CauseDetailResponse dto = causeMapper.toDetailDTO(cause);
        dto.setSystemIds(systemResponseList);
        // check causeCategory
        BasicInfoDTO causeCategory = causeMapper.toLinkDTO(causeRepository.findById(dto.getId())
                .orElseThrow(() -> new NotFoundException("Causes not found with id: " + dto.getId())));
        dto.setCauseCategory(causeCategory);
        return dto;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // Kiểm tra tồn tại
        if (!causeRepository.existsById(id)) {
            throw new NotFoundException("Causes not found with id: " + id);
        }
        // Xóa mapping trước
        systemCauseRepository.deleteByCauseId(id);

        // Xóa CauseCategory
        causeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Long update(CauseUpdateDTO causeUpdateDTO) {
        Cause cause = causeRepository.findById(causeUpdateDTO.getId())
                .orElseThrow(() -> new NotFoundException("CauseCategory not found with id: " + causeUpdateDTO.getId()));

        // check trùng tên
        if (causeRepository.existsByNameAndIdNot(causeUpdateDTO.getName(), causeUpdateDTO.getId())) {
            throw new DuplicateException("Causes with name '" + causeUpdateDTO.getName() + "' already exists.");
        }

        causeMapper.updateCausesFromDto(causeUpdateDTO, cause);
        causeRepository.save(cause);

        // Xóa các mapping cũ trước khi thêm mới để tránh trùng (nếu yêu cầu business cần)
        systemCauseRepository.deleteByCauseId(causeUpdateDTO.getId());

        List<SystemCause> systemCauses = causeUpdateDTO.getSystemIds().stream()
                .map(systemId -> {
                    SystemCause link = new SystemCause();
                    link.setCauseId(cause.getId());
                    link.setSystemId(systemId);
                    return link;
                })
                .toList();
        systemCauseRepository.saveAll(systemCauses);
        // Trả về DTO đã update (có thể dùng mapper hoặc build thủ công)
        return cause.getId();
    }

    @Override
    public Page<CauseDetailResponse> searchCause(Pageable pageable, CauseSearchRequest request) {
        Specification<Cause> spec = Specification
                .where(CauseSpecs.hasCode(request.getCode()))
                .and(CauseSpecs.hasName(request.getName()))
                .and(CauseSpecs.hasCauseCategoryId(request.getCauseCategoryId()))
                .and(CauseSpecs.hasSource(request.getSource()))
                .and(CauseSpecs.hasIsActive(request.getIsActive()));

        Page<Cause> causes = causeRepository.findAll(spec, pageable);
        return causes.map(cause-> {
            // map sang DTO
            CauseDetailResponse dto = causeMapper.toDetailDTO(cause);

            // lấy systemCauses
            List<SystemCause> systemCauses = systemCauseRepository.findByCauseId(dto.getId());
            Set<Long> systemIds = systemCauses.stream()
                    .map(SystemCause::getSystemId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            // Call API System
            List<BasicInfoDTO> systemResponseList = systemClient.getAllSystems(systemIds).getData().getContent();
            dto.setSystemIds(systemResponseList);

            // check causeCategory
            BasicInfoDTO causeCategory = causeMapper.toLinkDTO(causeRepository.findById(cause.getId())
                    .orElseThrow(() -> new NotFoundException("Causes not found with id: " + cause.getId())));
            dto.setCauseCategory(causeCategory);
            return dto;
        });
    }
}

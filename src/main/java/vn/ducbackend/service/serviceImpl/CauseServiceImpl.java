package vn.ducbackend.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ducbackend.client.SystemClient;
import vn.ducbackend.domain.dto.*;
import vn.ducbackend.domain.dto.causes.CauseDetailResponse;
import vn.ducbackend.domain.dto.causes.CauseRequest;
import vn.ducbackend.domain.dto.causes.CauseUpdateDTO;
import vn.ducbackend.domain.entity.Causes;
import vn.ducbackend.domain.entity.SystemCauses;
import vn.ducbackend.exception.customException.DuplicateException;
import vn.ducbackend.exception.customException.NotFoundException;
import vn.ducbackend.mapper.CauseMapper;
import vn.ducbackend.repository.CauseCategoryRepository;
import vn.ducbackend.repository.CauseRepository;
import vn.ducbackend.repository.SystemCauseRepository;
import vn.ducbackend.service.CauseService;

import java.util.List;
import java.util.Set;

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
        Causes causes = causeMapper.toCauses(causeRequest);
        causeRepository.save(causes);

        // Lưu quan hệ vào bảng trung gian
        List<SystemCauses> links = causeRequest.getSystemIds().stream()
                .map(systemId -> {
                    SystemCauses link = new SystemCauses();
                    link.setCauseId(causes.getId());
                    link.setSystemId(systemId);
                    return link;
                })
                .toList();
        systemCauseRepository.saveAll(links);
        return causes.getId();
    }

    // list ALL Cause
    @Override
    public Page<CauseDetailResponse> getListCause(Pageable pageable, Set<Long> ids) {
        Page<Causes> causes;
        if (ids != null && !ids.isEmpty()) {
            causes = causeRepository.findByIdIn(ids, pageable);
        } else {
            causes = causeRepository.findAll(pageable);
        }
        return causes.map(cause -> {
            // map sang DTO
            CauseDetailResponse dto = causeMapper.toDetailDTO(cause);

            // lấy links
            List<SystemCauses> links = systemCauseRepository.findByCauseId(dto.getId());
            List<Long> systemIds = links.stream()
                    .map(SystemCauses::getSystemId)
                    .toList();

            // Call API System
            List<LinkResponse> systemResponseList = systemClient.getAllSystems(systemIds).getData().getContent();
            dto.setSystemIds(systemResponseList);

            // check causeCategory
            LinkResponse causeCategory = causeMapper.toLinkDTO(causeRepository.findById(cause.getId())
                    .orElseThrow(() -> new NotFoundException("Causes not found with id: " + cause.getId())));
            dto.setCauseCategory(causeCategory);
            return dto;
        });
    }

    @Override
    public CauseDetailResponse getCause(Long id) {
        Causes causes = causeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Causes not found with id :"+id));
        List<SystemCauses> links = systemCauseRepository.findByCauseId(id);
        List<Long> systemIds = links.stream()
                .map(SystemCauses::getSystemId)
                .toList();
        // Call API System
        List<LinkResponse> systemResponseList = systemClient.getAllSystems(systemIds).getData().getContent();

        CauseDetailResponse dto = causeMapper.toDetailDTO(causes);
        dto.setSystemIds(systemResponseList);
        // check causeCategory
        LinkResponse causeCategory = causeMapper.toLinkDTO(causeRepository.findById(dto.getId())
                .orElseThrow(() -> new NotFoundException("Causes not found with id: " + dto.getId())));
        dto.setCauseCategory(causeCategory);
        return dto;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // Kiểm tra tồn tại
        if (!causeRepository.existsById(id)) {
            throw new RuntimeException("Causes not found with id: " + id);
        }
        // Xóa mapping trước
        systemCauseRepository.deleteByCauseId(id);

        // Xóa CauseCategory
        causeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Long update(CauseUpdateDTO causeUpdateDTO) {
        Causes causes = causeRepository.findById(causeUpdateDTO.getId())
                .orElseThrow(() -> new NotFoundException("CauseCategory not found with id: " + causeUpdateDTO.getId()));

        // check trùng tên
        if (causeRepository.existsByNameAndIdNot(causeUpdateDTO.getName(), causeUpdateDTO.getId())) {
            throw new DuplicateException("Causes with name '" + causeUpdateDTO.getName() + "' already exists.");
        }

        causeMapper.updateCausesFromDto(causeUpdateDTO, causes);
        causeRepository.save(causes);

        // Xóa các mapping cũ trước khi thêm mới để tránh trùng (nếu yêu cầu business cần)
        systemCauseRepository.deleteByCauseId(causeUpdateDTO.getId());

        List<SystemCauses> links = causeUpdateDTO.getSystemIds().stream()
                .map(systemId -> {
                    SystemCauses link = new SystemCauses();
                    link.setCauseId(causes.getId());
                    link.setSystemId(systemId);
                    return link;
                })
                .toList();
        systemCauseRepository.saveAll(links);
        // Trả về DTO đã update (có thể dùng mapper hoặc build thủ công)
        return causes.getId();
    }
}

package vn.ducbackend.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.ducbackend.domain.dto.CauseDetailResponse;
import vn.ducbackend.domain.dto.CauseRequest;
import vn.ducbackend.domain.dto.SystemResponse;
import vn.ducbackend.domain.entity.Causes;
import vn.ducbackend.domain.entity.SystemCauses;
import vn.ducbackend.domain.entity.Systems;
import vn.ducbackend.mapper.CauseMapper;
import vn.ducbackend.mapper.SystemMapper;
import vn.ducbackend.repository.CauseRepository;
import vn.ducbackend.repository.SystemCauseRepository;
import vn.ducbackend.repository.SystemRepository;
import vn.ducbackend.service.CauseService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CauseServiceImpl implements CauseService {
    private final CauseRepository causeRepository;
    private final CauseMapper causeMapper;
    private final SystemRepository systemRepository;
    private final SystemCauseRepository systemCauseRepository;
    private final SystemMapper systemMapper;

    @Override
    public CauseDetailResponse createCause(CauseRequest causeRequest) {
        Causes causes = causeMapper.toCauses(causeRequest);
        causes = causeRepository.save(causes);

        CauseDetailResponse dto = causeMapper.toDetailDTO(causes);
        // Lưu quan hệ vào bảng trung gian
        List<SystemCauses> links = new ArrayList<>();
        for (Long systemId : causeRequest.getSystemIds()) {
            // optional: check systemId có tồn tại không
            if (!systemRepository.existsById(systemId)) {
                throw new RuntimeException("System not found: " + systemId);
            }

            SystemCauses link = new SystemCauses();
            link.setCauseId(causes.getId());
            link.setSystemId(systemId);
            systemCauseRepository.save(link);
            links.add(link);
        }
        List<Long> systemIds = links.stream()
                .map(SystemCauses::getSystemId)
                .toList();
        List<Systems> systems = systemRepository.findAllById(systemIds);
        // map systems sang DTO
        List<SystemResponse> systemResponses = systems.stream()
                .map(systemMapper::toSystemResponse)
                .toList();
        dto.setSystemIds(systemResponses);
        return dto;
    }

    @Override
    public Page<CauseDetailResponse> getAllCauses(Pageable pageable) {
        return null;
    }

    @Override
    public CauseDetailResponse getCause(Long id) {
        return null;
    }

    @Override
    public void deleteCause(Long id) {

    }

    @Override
    public CauseDetailResponse updateCause(CauseRequest causeRequest) {
        return null;
    }
}

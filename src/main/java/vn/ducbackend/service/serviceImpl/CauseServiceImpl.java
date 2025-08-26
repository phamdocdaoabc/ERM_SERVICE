package vn.ducbackend.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.ducbackend.domain.dto.CauseRequest;
import vn.ducbackend.domain.dto.CauseResponse;
import vn.ducbackend.domain.entity.Causes;
import vn.ducbackend.domain.entity.SystemCauseCategories;
import vn.ducbackend.domain.entity.SystemCauses;
import vn.ducbackend.mapper.CauseMapper;
import vn.ducbackend.repository.CauseRepository;
import vn.ducbackend.repository.SystemCauseRepository;
import vn.ducbackend.repository.SystemRepository;
import vn.ducbackend.service.CauseService;
@Service
@RequiredArgsConstructor
@Slf4j
public class CauseServiceImpl implements CauseService {
    private final CauseRepository causeRepository;
    private final CauseMapper causeMapper;
    private final SystemRepository systemRepository;
    private final SystemCauseRepository systemCauseRepository;

    @Override
    public CauseResponse createCause(CauseRequest causeRequest) {
        Causes causes = causeMapper.toCauses(causeRequest);
        causes = causeRepository.save(causes);

        // Lưu quan hệ vào bảng trung gian
        for (Long systemId : causeRequest.getSystemIds()) {
            // optional: check systemId có tồn tại không
            if (!systemRepository.existsById(systemId)) {
                throw new RuntimeException("System not found: " + systemId);
            }

            SystemCauses link = new SystemCauses();
            link.setCauseId(causes.getId());
            link.setSystemId(systemId);
            systemCauseRepository.save(link);
        }
        return causeMapper.toCauseResponse(causes);
    }
}

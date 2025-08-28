package vn.ducbackend.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.ducbackend.domain.dto.CauseDetailResponse;
import vn.ducbackend.domain.dto.CauseRequest;
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


    @Override
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

    @Override
    public Page<CauseDetailResponse> getListCause(Pageable pageable, Set<Long> ids) {
        return null;
    }

    @Override
    public CauseDetailResponse getCause(Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Long update(CauseRequest causeRequest) {
        return null;
    }
}

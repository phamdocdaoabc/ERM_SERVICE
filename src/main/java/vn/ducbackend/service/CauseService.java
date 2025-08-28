package vn.ducbackend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.ducbackend.domain.dto.CauseDetailResponse;
import vn.ducbackend.domain.dto.CauseRequest;

import java.util.Set;

public interface CauseService {
    Long create(CauseRequest causeRequest);

    Page<CauseDetailResponse> getListCause(Pageable pageable, Set<Long> ids);

    CauseDetailResponse getCause(Long id);

    void delete(Long id);

    Long update(CauseRequest causeRequest);
}

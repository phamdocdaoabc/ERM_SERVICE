package vn.ducbackend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.ducbackend.domain.dto.CauseDetailResponse;
import vn.ducbackend.domain.dto.CauseRequest;

public interface CauseService {
    CauseDetailResponse createCause(CauseRequest causeRequest);

    Page<CauseDetailResponse> getAllCauses(Pageable pageable);

    CauseDetailResponse getCause(Long id);

    void deleteCause(Long id);

    CauseDetailResponse updateCause(CauseRequest causeRequest);
}

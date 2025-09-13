package vn.ducbackend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.ducbackend.domain.dto.causes.CauseDetailResponse;
import vn.ducbackend.domain.dto.causes.CauseRequest;
import vn.ducbackend.domain.dto.causes.CauseSearchRequest;
import vn.ducbackend.domain.dto.causes.CauseUpdateDTO;
import vn.ducbackend.domain.dto.causesCategory.CauseCategoryDetailResponse;
import vn.ducbackend.domain.dto.causesCategory.CauseCategorySearchRequest;

import java.util.Set;

public interface CauseService {
    Long create(CauseRequest causeRequest);

    Page<CauseDetailResponse> getListCause(Pageable pageable, Set<Long> ids);

    CauseDetailResponse getCause(Long id);

    void delete(Long id);

    Long update(CauseUpdateDTO causeUpdateDTO);

    Page<CauseDetailResponse> searchCause(Pageable pageable, CauseSearchRequest request);
}

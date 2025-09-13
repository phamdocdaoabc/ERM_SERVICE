package vn.ducbackend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.ducbackend.domain.dto.SampleActionRisk.SampleActionRiskDetailResponse;
import vn.ducbackend.domain.dto.SampleActionRisk.SampleActionRiskRequest;
import vn.ducbackend.domain.dto.SampleActionRisk.SampleActionRiskResponse;

import java.util.Set;

public interface SampleActionRiskService {
    Long create(SampleActionRiskRequest request);

    Page<SampleActionRiskResponse> getListSampleAction(Pageable pageable, Set<Long> ids);

    SampleActionRiskDetailResponse getSampleAction(Long id);

    void delete(Long id);

    Long update(SampleActionRiskRequest request);

    //Page<CauseDetailResponse> searchCause(Pageable pageable, CauseSearchRequest request);
}

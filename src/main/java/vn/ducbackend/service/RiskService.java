package vn.ducbackend.service;

import vn.ducbackend.domain.dto.RiskDTO;

public interface RiskService {
    Long create(RiskDTO riskDTO);

    //Page<RiskTypeResponse> getListRiskType(Pageable pageable, Set<Long> ids);

    //RiskTypeDetailResponse getRiskType(Long id);

    //void delete(Long id);

    //Long update(RiskTypeUpdateDTO request);

}

package vn.ducbackend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.ducbackend.domain.dto.RiskType.RiskTypeDTO;
import vn.ducbackend.service.dto.SearchRiskType;

public interface RiskTypeService {
    Long create(RiskTypeDTO riskTypeDTO);


    RiskTypeDTO getRiskType(Long id);

    void delete(Long id);

    Long update(RiskTypeDTO riskTypeDTO);

    Page<RiskTypeDTO> getListRiskType(Pageable pageable, SearchRiskType searchRiskType);
}

package vn.ducbackend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.ducbackend.domain.dto.attributeRisk.AttributeRiskRequest;
import vn.ducbackend.domain.dto.attributeRisk.AttributeRiskResponse;
import vn.ducbackend.domain.dto.attributeRisk.AttributeRiskUpdateDTO;

import java.util.Set;

public interface AttributeRisksService {
    Long create(AttributeRiskRequest request);

    Page<AttributeRiskResponse> getListAttributeRisk(Pageable pageable, Set<Long> ids);

    AttributeRiskResponse getAttributeRisk(Long id);

    void delete(Long id);

    Long update(AttributeRiskUpdateDTO request);
}

package vn.ducbackend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.ducbackend.domain.dto.attributeGroupRisk.AttributeGroupRiskDTO;
import vn.ducbackend.domain.dto.attributeGroupRisk.AttributeGroupRiskRequest;

import java.util.Set;

public interface AttributeGroupRiskService {
    Long create(AttributeGroupRiskRequest request);

    Page<AttributeGroupRiskDTO> getListAttributeGroupRisk(Pageable pageable, Set<Long> ids);

    AttributeGroupRiskDTO getAttributeGroupRisk(Long id);

    void delete(Long id);

    Long update(AttributeGroupRiskDTO request);
}

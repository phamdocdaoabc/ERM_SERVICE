package vn.ducbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.ducbackend.domain.entity.RiskTypesAttributesValue;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface RiskTypeAttributeValueRepository extends JpaRepository<RiskTypesAttributesValue, Long> {
    List<RiskTypesAttributesValue> findByRiskTypeAttributeId(Long riskTypeAttributeId);

    @Modifying
    @Query("DELETE FROM RiskTypesAttributesValue v WHERE v.riskTypeAttributeId IN :ids")
    void deleteByRiskTypeAttributeIdIn(@Param("ids") List<Long> ids);

    List<RiskTypesAttributesValue> findByRiskTypeAttributeIdIn(Collection<Long> riskTypeAttributeIds);
}

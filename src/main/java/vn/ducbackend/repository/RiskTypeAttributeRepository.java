package vn.ducbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ducbackend.domain.entity.RiskTypeAttribute;

import java.util.Collection;
import java.util.List;

@Repository
public interface RiskTypeAttributeRepository extends JpaRepository<RiskTypeAttribute, Long> {

    List<RiskTypeAttribute> findByRiskTypeId(Long riskTypeId);

    void deleteByRiskTypeId(Long riskTypeId);

    List<RiskTypeAttribute> findByRiskTypeIdIn(Collection<Long> riskTypeIds);
}

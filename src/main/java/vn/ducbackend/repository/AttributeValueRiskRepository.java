package vn.ducbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ducbackend.domain.entity.AttributeValueRisk;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Repository
public interface AttributeValueRiskRepository extends JpaRepository<AttributeValueRisk, Long> {

    void deleteByAttributeRiskId(Long attributeRiskId);

    List<AttributeValueRisk> findByAttributeRiskId(Long attributeRiskId);

    List<AttributeValueRisk> findByIdIn(Collection<Long> ids);
}

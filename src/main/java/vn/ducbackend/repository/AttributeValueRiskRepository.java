package vn.ducbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ducbackend.domain.entity.AttributeValueRisks;

@Repository
public interface AttributeValueRiskRepository extends JpaRepository<AttributeValueRisks, Long> {

    void deleteByAttributeRiskId(Long attributeRiskId);
}

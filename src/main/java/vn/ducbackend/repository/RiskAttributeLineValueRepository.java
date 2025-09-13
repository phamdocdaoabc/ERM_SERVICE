package vn.ducbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ducbackend.domain.entity.RiskAttributeLineValue;

@Repository
public interface RiskAttributeLineValueRepository extends JpaRepository<RiskAttributeLineValue, Long> {
}

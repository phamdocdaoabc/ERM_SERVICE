package vn.ducbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ducbackend.domain.entity.RiskAttributeLine;

import java.util.Set;

@Repository
public interface RiskAttributeLineRepository extends JpaRepository<RiskAttributeLine, Long> {
}

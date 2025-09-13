package vn.ducbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ducbackend.domain.entity.RiskCauseMapping;

@Repository
public interface RiskCauseMappingRepository extends JpaRepository<RiskCauseMapping, Long> {

}

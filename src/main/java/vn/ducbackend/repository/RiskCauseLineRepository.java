package vn.ducbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ducbackend.domain.entity.RiskCauseLine;

@Repository
public interface RiskCauseLineRepository extends JpaRepository<RiskCauseLine, Long> {

}

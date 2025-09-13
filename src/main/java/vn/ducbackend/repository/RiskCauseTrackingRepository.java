package vn.ducbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ducbackend.domain.entity.RiskCauseTracking;

import java.util.Set;

@Repository
public interface RiskCauseTrackingRepository extends JpaRepository<RiskCauseTracking, Long> {

}

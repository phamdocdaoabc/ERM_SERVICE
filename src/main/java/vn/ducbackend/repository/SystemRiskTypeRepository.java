package vn.ducbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ducbackend.domain.entity.SystemRiskType;

import java.util.Collection;
import java.util.List;

@Repository
public interface SystemRiskTypeRepository extends JpaRepository<SystemRiskType, Long> {
    List<SystemRiskType> findByRiskTypeId(Long riskTypeId);

    void deleteByRiskTypeId(Long id);

    List<SystemRiskType> findByRiskTypeIdIn(Collection<Long> riskTypeIds);
}

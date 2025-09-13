package vn.ducbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ducbackend.domain.entity.SampleActionRiskMap;

import java.util.List;

@Repository
public interface SampleActionRiskMapRepository extends JpaRepository<SampleActionRiskMap, Long> {
    void deleteBySampleActionId(Long sampleActionId);

    List<SampleActionRiskMap> findBySampleActionId(Long sampleActionId);
}

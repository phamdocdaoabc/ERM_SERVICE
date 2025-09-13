package vn.ducbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ducbackend.domain.entity.SampleActionRisk;

import java.util.Collection;
import java.util.Set;

@Repository
public interface SampleActionRiskRepository extends JpaRepository<SampleActionRisk, Long> {
    boolean existsSampleActionRiskByCodeOrName(String code, String name);

    Page<SampleActionRisk> findByIdIn(Set<Long> ids, Pageable pageable);

    boolean existsByCodeAndIdNot(String code, Long id);
}

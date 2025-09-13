package vn.ducbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.ducbackend.domain.entity.RiskType;

import java.util.Set;

@Repository
public interface RiskTypeRepository extends JpaRepository<RiskType, Long>, JpaSpecificationExecutor<RiskType> {

    boolean existsRiskTypesByCodeOrName(String code, String name);

    boolean existsByNameAndIdNot(String name, Long id);

    Page<RiskType> findByIdIn(Set<Long> ids, Pageable pageable);

    boolean existsByCodeOrNameAndIdNot(String code, String name, Long id);

}

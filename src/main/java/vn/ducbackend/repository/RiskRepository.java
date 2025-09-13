package vn.ducbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ducbackend.domain.entity.Risk;

import java.util.Set;

@Repository
public interface RiskRepository extends JpaRepository<Risk, Long> {

    boolean existsRisksByCodeOrName(String code, String name);

    boolean existsByNameAndIdNot(String name, Long id);

    Page<Risk> findByIdIn(Set<Long> ids, Pageable pageable);

    boolean existsByCodeOrNameAndIdNot(String code, String name, Long id);
}

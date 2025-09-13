package vn.ducbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ducbackend.domain.entity.AttributeRisk;

import java.util.List;
import java.util.Set;

@Repository
public interface AttributeRiskRepository extends JpaRepository<AttributeRisk, Long> {

    boolean existsAttributeRisksByCodeOrName(String code, String name);

    Page<AttributeRisk> findByIdIn(Set<Long> ids, Pageable pageable);

    boolean existsByNameAndIdNot(String name, Long id);

    List<AttributeRisk> findByIdIn(Set<Long> ids);
}

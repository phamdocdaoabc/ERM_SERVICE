package vn.ducbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ducbackend.domain.entity.AttributeGroupRisk;

import java.util.List;
import java.util.Set;

@Repository
public interface AttributeGroupRiskRepository extends JpaRepository<AttributeGroupRisk, Long> {

    boolean existsAttributeGroupRisksByCodeOrName(String code, String name);

    Page<AttributeGroupRisk> findByIdIn(Set<Long> ids, Pageable pageable);

    boolean existsByNameAndIdNot(String name, Long id);

    List<AttributeGroupRisk> findByIdIn(Set<Long> ids);
}

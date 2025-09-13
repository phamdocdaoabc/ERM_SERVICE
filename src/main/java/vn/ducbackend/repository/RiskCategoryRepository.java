package vn.ducbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ducbackend.domain.entity.RiskCategory;

import java.util.Set;

@Repository
public interface RiskCategoryRepository extends JpaRepository<RiskCategory, Long> {
    Page<RiskCategory> findByIdIn(Set<Long> ids, Pageable pageable);
    boolean existsByNameAndIdNot(String name, Long id);
    boolean existsRiskCategoriesByCodeOrName(String code, String name);
}

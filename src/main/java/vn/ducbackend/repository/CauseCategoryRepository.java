package vn.ducbackend.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.ducbackend.domain.entity.CauseCategory;

import java.util.Set;

@Repository
public interface CauseCategoryRepository extends JpaRepository<CauseCategory, Long>, JpaSpecificationExecutor<CauseCategory> {

    boolean existsByNameAndIdNot(String name, Long id);

    boolean existsCauseCategoriesByCodeOrName(String code, String name);

    Page<CauseCategory> findByIdIn(Set<Long> ids, Pageable pageable);
}

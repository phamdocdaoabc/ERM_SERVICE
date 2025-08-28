package vn.ducbackend.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ducbackend.domain.entity.CauseCategories;

import java.nio.channels.FileChannel;
import java.util.Collection;
import java.util.Set;

@Repository
public interface CauseCategoryRepository extends JpaRepository<CauseCategories, Long> {

    boolean existsByNameAndIdNot(String name, Long id);

    boolean existsCauseCategoriesByCodeOrName(String code, String name);

    Page<CauseCategories> findByIdIn(Set<Long> ids, Pageable pageable);
}

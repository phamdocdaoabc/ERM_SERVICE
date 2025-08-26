package vn.ducbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ducbackend.domain.entity.CauseCategories;

@Repository
public interface CauseCategoryRepository extends JpaRepository<CauseCategories, Long> {

}

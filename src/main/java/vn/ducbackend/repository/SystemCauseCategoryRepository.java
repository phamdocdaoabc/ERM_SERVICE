package vn.ducbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ducbackend.domain.entity.CauseCategories;
import vn.ducbackend.domain.entity.SystemCauseCategories;

import java.util.List;

@Repository
public interface SystemCauseCategoryRepository extends JpaRepository<SystemCauseCategories, Long> {
    List<SystemCauseCategories> findByCauseCategoryId(Long causeCategoryId);

    void deleteByCauseCategoryId(Long causeCategoryId);
}

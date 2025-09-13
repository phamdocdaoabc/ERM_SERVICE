package vn.ducbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.ducbackend.domain.entity.SystemCauseCategory;

import java.util.List;

@Repository
public interface SystemCauseCategoryRepository extends JpaRepository<SystemCauseCategory, Long>, JpaSpecificationExecutor<SystemCauseCategory> {
    List<SystemCauseCategory> findByCauseCategoryId(Long causeCategoryId);

    void deleteByCauseCategoryId(Long causeCategoryId);
}

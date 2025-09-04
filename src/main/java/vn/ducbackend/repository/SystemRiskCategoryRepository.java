package vn.ducbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ducbackend.domain.entity.SystemCauses;
import vn.ducbackend.domain.entity.SystemRiskCategories;

import java.util.List;

@Repository
public interface SystemRiskCategoryRepository extends JpaRepository<SystemRiskCategories, Long> {
    List<SystemRiskCategories> findByRiskCategoryId(Long riskCategoryId);

    void deleteByRiskCategoryId(Long riskCategoryId);
}

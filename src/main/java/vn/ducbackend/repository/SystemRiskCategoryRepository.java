package vn.ducbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ducbackend.domain.entity.SystemRiskCategory;

import java.util.List;

@Repository
public interface SystemRiskCategoryRepository extends JpaRepository<SystemRiskCategory, Long> {
    List<SystemRiskCategory> findByRiskCategoryId(Long riskCategoryId);

    void deleteByRiskCategoryId(Long riskCategoryId);
}

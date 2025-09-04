package vn.ducbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ducbackend.domain.entity.SystemCauseCategories;
import vn.ducbackend.domain.entity.SystemCauses;

import java.util.List;

@Repository
public interface SystemCauseRepository extends JpaRepository<SystemCauses, Long> {
    List<SystemCauses> findByCauseId(Long causeId);

    void deleteByCauseId(Long causeId);
}

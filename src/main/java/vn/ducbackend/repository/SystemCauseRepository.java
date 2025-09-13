package vn.ducbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ducbackend.domain.entity.SystemCause;

import java.util.List;

@Repository
public interface SystemCauseRepository extends JpaRepository<SystemCause, Long> {
    List<SystemCause> findByCauseId(Long causeId);

    void deleteByCauseId(Long causeId);
}

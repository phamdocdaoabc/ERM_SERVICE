package vn.ducbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ducbackend.domain.entity.Causes;

@Repository
public interface CauseRepository extends JpaRepository<Causes, Long> {

}

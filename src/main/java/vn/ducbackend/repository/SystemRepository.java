package vn.ducbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ducbackend.domain.entity.Systems;
@Repository
public interface SystemRepository extends JpaRepository<Systems, Long> {

}

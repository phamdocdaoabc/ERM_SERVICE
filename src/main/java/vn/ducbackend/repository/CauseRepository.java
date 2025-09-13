package vn.ducbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.ducbackend.domain.entity.Cause;

import java.util.Set;

@Repository
public interface CauseRepository extends JpaRepository<Cause, Long>, JpaSpecificationExecutor<Cause> {

    boolean existsCausesByCodeOrName(String code, String name);

    boolean existsByNameAndIdNot(String name, Long id);

    Page<Cause> findByIdIn(Set<Long> ids, Pageable pageable);
}

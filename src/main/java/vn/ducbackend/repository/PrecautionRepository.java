package vn.ducbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ducbackend.domain.entity.Precaution;

import java.util.Set;

@Repository
public interface PrecautionRepository extends JpaRepository<Precaution, Long> {

    boolean existsPrecautionsByCodeOrName(String code, String name);

    Page<Precaution> findByIdIn(Set<Long> ids, Pageable pageable);

    boolean existsByNameAndIdNot(String name, Long id);
}

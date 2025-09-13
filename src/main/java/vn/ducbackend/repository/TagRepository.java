package vn.ducbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ducbackend.domain.entity.Tag;

import java.util.Set;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    boolean existsByNameAndIdNot(String name, Long id);

    Page<Tag> findByIdIn(Set<Long> ids, Pageable pageable);

    boolean existsByName(String name);
}

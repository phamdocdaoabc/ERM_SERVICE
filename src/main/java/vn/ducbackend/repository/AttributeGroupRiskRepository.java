package vn.ducbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ducbackend.domain.entity.AttributeGroupRisks;

import java.util.Set;

@Repository
public interface AttributeGroupRiskRepository extends JpaRepository<AttributeGroupRisks, Long> {

}

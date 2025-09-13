package vn.ducbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ducbackend.domain.entity.FileRisk;

import java.util.Set;

@Repository
public interface FileRiskRepository extends JpaRepository<FileRisk, Long> {

}

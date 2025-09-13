package vn.ducbackend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.ducbackend.domain.dto.Precaution.PrecautionDTO;
import vn.ducbackend.domain.dto.Precaution.PrecautionRequest;

import java.util.Set;

public interface PrecautionService {
    Long create(PrecautionRequest request);

    Page<PrecautionDTO> getListPrecaution(Pageable pageable, Set<Long> ids);

    PrecautionDTO getPrecaution(Long id);

    void delete(Long id);

    Long update(PrecautionDTO request);
}

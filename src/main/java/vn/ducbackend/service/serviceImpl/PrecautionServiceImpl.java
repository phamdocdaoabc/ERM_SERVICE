package vn.ducbackend.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ducbackend.domain.dto.Precaution.PrecautionDTO;
import vn.ducbackend.domain.dto.Precaution.PrecautionRequest;
import vn.ducbackend.domain.entity.Precaution;
import vn.ducbackend.exception.customException.DuplicateException;
import vn.ducbackend.exception.customException.NotFoundException;
import vn.ducbackend.mapper.PrecautionMapper;
import vn.ducbackend.repository.PrecautionRepository;
import vn.ducbackend.service.PrecautionService;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrecautionServiceImpl implements PrecautionService {
    private final PrecautionRepository precautionRepository;
    private final PrecautionMapper precautionMapper;

    @Override
    @Transactional
    public Long create(PrecautionRequest request) {
        // Check code trùng
        if (precautionRepository.existsPrecautionsByCodeOrName(request.getCode(), request.getName())) {
            throw new DuplicateException("Code [" + request.getCode() + "] or Name [" + request.getName() + "] already exists");
        }
        
         Precaution precaution = precautionMapper.toPrecautions(request);
        precautionRepository.save(precaution);

        return precaution.getId();
    }

    @Override
    public Page<PrecautionDTO> getListPrecaution(Pageable pageable, Set<Long> ids) {
        Page<Precaution> precautions;
        if (ids != null && !ids.isEmpty()) {
            precautions = precautionRepository.findByIdIn(ids, pageable);
        } else {
            precautions = precautionRepository.findAll(pageable);
        }
        return precautions.map(precaution -> {
            // map sang DTO
            PrecautionDTO dto = precautionMapper.toDetailDTO(precaution);
            return dto;
        });
    }

    @Override
    public PrecautionDTO getPrecaution(Long id) {
        Precaution precaution = precautionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("precaution not found with id: " + id));

        PrecautionDTO dto = precautionMapper.toDetailDTO(precaution);
        return dto;
    }
    

    @Override
    @Transactional
    public void delete(Long id) {
        // Kiểm tra tồn tại
        if (!precautionRepository.existsById(id)) {
            throw new NotFoundException("precaution not found with id: " + id);
        }
        // Delete
        precautionRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Long update(PrecautionDTO request) {
        Precaution precaution = precautionRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("Precaution not found with id: " + request.getId()));

        // check trùng tên
        if (precautionRepository.existsByNameAndIdNot(request.getName(), request.getId())) {
            throw new DuplicateException("Precaution with name '" + request.getName() + "' already exists.");
        }

        precautionMapper.updatePrecaution(request, precaution);
        precautionRepository.save(precaution);
        return precaution.getId();
    }
}

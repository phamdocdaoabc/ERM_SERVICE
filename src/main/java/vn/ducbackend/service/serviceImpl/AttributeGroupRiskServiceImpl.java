package vn.ducbackend.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ducbackend.domain.dto.*;
import vn.ducbackend.domain.entity.AttributeGroupRisks;
import vn.ducbackend.domain.enums.TypeAttributeGroup;
import vn.ducbackend.exception.customException.DuplicateException;
import vn.ducbackend.exception.customException.NotFoundException;
import vn.ducbackend.mapper.AttributeGroupRiskMapper;
import vn.ducbackend.repository.AttributeGroupRiskRepository;
import vn.ducbackend.service.AttributeGroupRiskService;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttributeGroupRiskServiceImpl implements AttributeGroupRiskService {
    private final AttributeGroupRiskRepository attributeGroupRiskRepository;
    private final AttributeGroupRiskMapper attributeGroupRiskMapper;

    @Override
    @Transactional
    public Long create(AttributeGroupRiskRequest request) {
        // Check code trùng
        if (attributeGroupRiskRepository.existsAttributeGroupRisksByCodeOrName(request.getCode(), request.getName())) {
            throw new DuplicateException("Code [" + request.getCode() + "] or Name [" + request.getName() + "] already exists");
        }

        if(request.getType() == null){
            request.setType(TypeAttributeGroup.BUSINESS);
        }

        AttributeGroupRisks attributeGroupRisks = attributeGroupRiskMapper.toAttributeGroupRisks(request);
        attributeGroupRiskRepository.save(attributeGroupRisks);

        return attributeGroupRisks.getId();
    }

    @Override
    public Page<AttributeGroupRiskDTO> getListAttributeGroupRisk(Pageable pageable, Set<Long> ids) {
        Page<AttributeGroupRisks> attributeGroupRisks;
        if (ids != null && !ids.isEmpty()) {
            attributeGroupRisks = attributeGroupRiskRepository.findByIdIn(ids, pageable);
        } else {
            attributeGroupRisks = attributeGroupRiskRepository.findAll(pageable);
        }
        return attributeGroupRisks.map(attributeGroupRisk -> {
            // map sang DTO
            AttributeGroupRiskDTO dto = attributeGroupRiskMapper.toDetailDTO(attributeGroupRisk);
            return dto;
        });
    }

    @Override
    public AttributeGroupRiskDTO getAttributeGroupRisk(Long id) {
        AttributeGroupRisks attributeGroupRisks = attributeGroupRiskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("attributeGroup not found with id: " + id));

        AttributeGroupRiskDTO dto = attributeGroupRiskMapper.toDetailDTO(attributeGroupRisks);
        return dto;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // Kiểm tra tồn tại
        if (!attributeGroupRiskRepository.existsById(id)) {
            throw new RuntimeException("attributeGroupRisk not found with id: " + id);
        }
        // Delete
        attributeGroupRiskRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Long update(AttributeGroupRiskDTO request) {
        AttributeGroupRisks attributeGroupRisks = attributeGroupRiskRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException(" AttributeGroupRisk not found with id: " + request.getId()));

        // check trùng tên
        if (attributeGroupRiskRepository.existsByNameAndIdNot(request.getName(), request.getId())) {
            throw new DuplicateException("AttributeGroupRisk with name '" + request.getName() + "' already exists.");
        }

        attributeGroupRiskMapper.updateAttributeGroupRiskDTO(request, attributeGroupRisks);
        attributeGroupRiskRepository.save(attributeGroupRisks);
        return attributeGroupRisks.getId();
    }
}

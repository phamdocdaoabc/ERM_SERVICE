package vn.ducbackend.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ducbackend.domain.dto.AttributeRiskRequest;
import vn.ducbackend.domain.dto.AttributeRiskResponse;
import vn.ducbackend.domain.dto.AttributeRiskUpdateDTO;
import vn.ducbackend.domain.dto.LinkResponse;
import vn.ducbackend.domain.entity.*;
import vn.ducbackend.domain.enums.DisplayType;
import vn.ducbackend.domain.enums.TypeAttributeGroup;
import vn.ducbackend.exception.customException.DuplicateException;
import vn.ducbackend.exception.customException.NotFoundException;
import vn.ducbackend.exception.customException.ValidationException;
import vn.ducbackend.mapper.AttributeGroupRiskMapper;
import vn.ducbackend.mapper.AttributeRiskMapper;
import vn.ducbackend.repository.AttributeGroupRiskRepository;
import vn.ducbackend.repository.AttributeRiskRepository;
import vn.ducbackend.repository.AttributeValueRiskRepository;
import vn.ducbackend.service.AttributeRisksService;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttributeRisksServiceImpl implements AttributeRisksService {
    private final AttributeRiskRepository attributeRiskRepository;
    private final AttributeRiskMapper attributeRiskMapper;
    private final AttributeGroupRiskMapper attributeGroupRiskMapper;
    private final AttributeValueRiskRepository attributeValueRiskRepository;
    private final AttributeGroupRiskRepository attributeGroupRiskRepository;
    @Override
    @Transactional
    public Long create(AttributeRiskRequest request) {
        // Check code trùng
        if (attributeRiskRepository.existsAttributeRisksByCodeOrName(request.getCode(), request.getName())) {
            throw new DuplicateException("Code [" + request.getCode() + "] or Name [" + request.getName() + "] already exists");
        }
        // If type = null => BUSINESS
        if(request.getType() == null){
            request.setType(TypeAttributeGroup.BUSINESS);
        }
        AttributeRisks attributeRisks = attributeRiskMapper.toAttributeRisks(request);
        if (request.getDisplayType().equals(DisplayType.TEXTBOX) || request.getDisplayType().equals(DisplayType.CHECKBOX)) {
            if (request.getDataType() == null) {
                throw new ValidationException("dataType required when display_type is Textbox/Checkbox");
            }
            attributeRisks.setDataType(request.getDataType());
        } else {
            attributeRisks.setDataType(null);
        }
        attributeRiskRepository.save(attributeRisks);
        // If display type = selectbox
        if (request.getDisplayType().equals(DisplayType.SELECTBOX)
                && request.getValues() != null) {
            int sort = 1;
            for (String v : request.getValues()) {
                AttributeValueRisks val = new AttributeValueRisks();
                val.setAttributeRiskId(attributeRisks.getId());
                val.setValue(v);
                val.setSortOrder(sort++);
                attributeValueRiskRepository.save(val);
            }
        }
        return attributeRisks.getId();
    }

    @Override
    public Page<AttributeRiskResponse> getListAttributeRisk(Pageable pageable, Set<Long> ids) {
        Page<AttributeRisks> attributeRisks;
        if (ids != null && !ids.isEmpty()) {
            attributeRisks = attributeRiskRepository.findByIdIn(ids, pageable);
        } else {
            attributeRisks =  attributeRiskRepository.findAll(pageable);
        }
        return  attributeRisks.map(attributeRisk -> {
            // map sang DTO
            AttributeRiskResponse dto = attributeRiskMapper.toDetailDTO(attributeRisk);
            AttributeGroupRisks attributeGroupRisks = attributeGroupRiskRepository.findById(attributeRisk.getAttributeGroupId())
                    .orElseThrow(() -> new NotFoundException("AttributeGroupRisk not found with id: " + attributeRisk.getAttributeGroupId()));

            LinkResponse linkResponse = attributeGroupRiskMapper.toLinkDTO(attributeGroupRisks);
            dto.setAttributeGroup(linkResponse);
            return dto;
        });
    }

    @Override
    public AttributeRiskResponse getAttributeRisk(Long id) {
        AttributeRisks attributeRisks = attributeRiskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("AttributeRisk not found with id: " + id));

        AttributeRiskResponse dto = attributeRiskMapper.toDetailDTO(attributeRisks);
        AttributeGroupRisks attributeGroupRisks = attributeGroupRiskRepository.findById(attributeRisks.getAttributeGroupId())
                .orElseThrow(() -> new NotFoundException("AttributeGroupRisk not found with id: " + attributeRisks.getAttributeGroupId()));

        LinkResponse linkResponse = attributeGroupRiskMapper.toLinkDTO(attributeGroupRisks);
        dto.setAttributeGroup(linkResponse);
        return dto;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // Kiểm tra tồn tại
        if (!attributeRiskRepository.existsById(id)) {
            throw new RuntimeException("attributeRisk not found with id: " + id);
        }
        // Xóa mapping trước
        attributeValueRiskRepository.deleteByAttributeRiskId(id);

        // Xóa CauseCategory
        attributeRiskRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Long update(AttributeRiskUpdateDTO request) {
        AttributeRisks attributeRisks = attributeRiskRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("AttributeRisk not found with id: " + request.getId()));

        // check trùng tên
        if (attributeRiskRepository.existsByNameAndIdNot(request.getName(), request.getId())) {
            throw new DuplicateException("attributeRisk with name '" + request.getName() + "' already exists.");
        }

        // If type = null => BUSINESS
        if(request.getType() == null){
            request.setType(TypeAttributeGroup.BUSINESS);
        }

        attributeRiskMapper.updateAttributeRiskUpdateDTO(request, attributeRisks);

        // xử lý dataType & value theo displayType
        if (request.getDisplayType().equals(DisplayType.TEXTBOX) || request.getDisplayType().equals(DisplayType.CHECKBOX)) {
            attributeRisks.setDataType(request.getDataType());
            // xóa value nếu trước đó có
            attributeValueRiskRepository.deleteByAttributeRiskId(attributeRisks.getId());
        } else if (request.getDisplayType().equals(DisplayType.SELECTBOX)) {
            attributeRisks.setDataType(null); // clear
            // clear value cũ
            attributeValueRiskRepository.deleteByAttributeRiskId(attributeRisks.getId());
            // insert value mới
            if (request.getValues() != null) {
                int sort = 1;
                for (String v : request.getValues()) {
                    AttributeValueRisks val = new AttributeValueRisks();
                    val.setAttributeRiskId(attributeRisks.getId());
                    val.setValue(v);
                    val.setSortOrder(sort++);
                    attributeValueRiskRepository.save(val);
                }
            }
        }
        attributeRiskRepository.save(attributeRisks);
        return attributeRisks.getId();
    }
}

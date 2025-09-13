package vn.ducbackend.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ducbackend.domain.dto.BasicInfoDTO;
import vn.ducbackend.domain.dto.attributeRisk.AttributeRiskRequest;
import vn.ducbackend.domain.dto.attributeRisk.AttributeRiskResponse;
import vn.ducbackend.domain.dto.attributeRisk.AttributeRiskUpdateDTO;
import vn.ducbackend.domain.dto.attributeRisk.AttributeValueRiskDTO;
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

import java.util.*;
import java.util.stream.Collectors;

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
        AttributeRisk attributeRisk = attributeRiskMapper.toAttributeRisks(request);
        if (request.getDisplayType().equals(DisplayType.TEXTBOX)) {
            if (request.getDataType() == null) {
                throw new ValidationException("dataType required when display_type is Textbox");
            }
            attributeRisk.setDataType(request.getDataType());
        } else {
            attributeRisk.setDataType(null);
        }
        attributeRiskRepository.save(attributeRisk);
        // If display type = selectbox, CHECKBOX, RADIO, MULTI_SELECTBOX
        if (!request.getDisplayType().equals(DisplayType.TEXTBOX)
                && request.getValues() != null) {
            int sort = 1;
            for (String v : request.getValues()) {
                AttributeValueRisk val = new AttributeValueRisk();
                val.setAttributeRiskId(attributeRisk.getId());
                val.setValue(v);
                val.setSortOrder(sort++);
                attributeValueRiskRepository.save(val);
            }
        }
        return attributeRisk.getId();
    }

    @Override
    public Page<AttributeRiskResponse> getListAttributeRisk(Pageable pageable, Set<Long> ids) {
        Page<AttributeRisk> attributeRisks;
        if (ids != null && !ids.isEmpty()) {
            attributeRisks = attributeRiskRepository.findByIdIn(ids, pageable);
        } else {
            attributeRisks =  attributeRiskRepository.findAll(pageable);
        }
        Set<Long> attributeIds = attributeRisks.stream()
                .map(AttributeRisk::getId)
                .filter(Objects::nonNull) // tránh null
                .collect(Collectors.toSet());;

        List<AttributeGroupRisk> attributeGroupRisks = attributeGroupRiskRepository.findByIdIn(attributeIds);

        Map<Long, AttributeGroupRisk> groupRiskMap = attributeGroupRisks.stream()
                .collect(Collectors.toMap(AttributeGroupRisk::getId, agr -> agr));

        return  attributeRisks.map(attributeRisk -> {
            // map sang DTO
            AttributeRiskResponse dto = attributeRiskMapper.toDetailDTO(attributeRisk);

            AttributeGroupRisk group = groupRiskMap.get(attributeRisk.getAttributeGroupId());
            if (group != null) {
                BasicInfoDTO basicInfoDTO = attributeGroupRiskMapper.toLinkDTO(group);
                dto.setAttributeGroup(basicInfoDTO);
            }

            List<AttributeValueRisk> attributeValueRisks = attributeValueRiskRepository.findByAttributeRiskId(attributeRisk.getId());
            if(!attributeValueRisks.isEmpty() && attributeValueRisks != null) {
                List<AttributeValueRiskDTO> attributeValueRiskDTOS = new ArrayList<>();
                for (AttributeValueRisk attributeValueRisk : attributeValueRisks){
                    AttributeValueRiskDTO attributeValueRiskDTO = new AttributeValueRiskDTO();
                    attributeValueRiskDTO.setId(attributeValueRisk.getId());
                    attributeValueRiskDTO.setValue(attributeValueRisk.getValue());
                    attributeValueRiskDTO.setSortOrder(attributeValueRisk.getSortOrder());
                    attributeValueRiskDTOS.add(attributeValueRiskDTO);
                }
                dto.setRiskAttributeValues(attributeValueRiskDTOS);
            }
            return dto;
        });
    }

    @Override
    public AttributeRiskResponse getAttributeRisk(Long id) {
        AttributeRisk attributeRisk = attributeRiskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("AttributeRisk not found with id: " + id));

        AttributeRiskResponse dto = attributeRiskMapper.toDetailDTO(attributeRisk);
        AttributeGroupRisk attributeGroupRisk = attributeGroupRiskRepository.findById(attributeRisk.getAttributeGroupId())
                .orElseThrow(() -> new NotFoundException("AttributeGroupRisk not found with id: " + attributeRisk.getAttributeGroupId()));

        BasicInfoDTO basicInfoDTO = attributeGroupRiskMapper.toLinkDTO(attributeGroupRisk);
        dto.setAttributeGroup(basicInfoDTO);

        List<AttributeValueRisk> attributeValueRisks = attributeValueRiskRepository.findByAttributeRiskId(id);
        if(!attributeValueRisks.isEmpty() && attributeValueRisks != null) {
            List<AttributeValueRiskDTO> attributeValueRiskDTOS = new ArrayList<>();
            for (AttributeValueRisk attributeValueRisk : attributeValueRisks){
                AttributeValueRiskDTO attributeValueRiskDTO = new AttributeValueRiskDTO();
                attributeValueRiskDTO.setId(attributeValueRisk.getId());
                attributeValueRiskDTO.setValue(attributeValueRisk.getValue());
                attributeValueRiskDTO.setSortOrder(attributeValueRisk.getSortOrder());
                attributeValueRiskDTOS.add(attributeValueRiskDTO);
            }
            dto.setRiskAttributeValues(attributeValueRiskDTOS);
        }

        return dto;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // Kiểm tra tồn tại
        if (!attributeRiskRepository.existsById(id)) {
            throw new NotFoundException("attributeRisk not found with id: " + id);
        }
        // Xóa mapping trước
        attributeValueRiskRepository.deleteByAttributeRiskId(id);

        // Xóa CauseCategory
        attributeRiskRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Long update(AttributeRiskUpdateDTO request) {
        AttributeRisk attributeRisk = attributeRiskRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("AttributeRisk not found with id: " + request.getId()));

        // check trùng tên
        if (attributeRiskRepository.existsByNameAndIdNot(request.getName(), request.getId())) {
            throw new DuplicateException("attributeRisk with name '" + request.getName() + "' already exists.");
        }

        // If type = null => BUSINESS
        if(request.getType() == null){
            request.setType(TypeAttributeGroup.BUSINESS);
        }

        attributeRiskMapper.updateAttributeRiskUpdateDTO(request, attributeRisk);

        // xử lý dataType & value theo displayType
        if (request.getDisplayType().equals(DisplayType.TEXTBOX)) {
            attributeRisk.setDataType(request.getDataType());
            // xóa value nếu trước đó có
            attributeValueRiskRepository.deleteByAttributeRiskId(attributeRisk.getId());
        } else {
            attributeRisk.setDataType(null); // clear
            // clear value cũ
            attributeValueRiskRepository.deleteByAttributeRiskId(attributeRisk.getId());
            // insert value mới
            if (request.getValues() != null) {
                int sort = 1;
                for (String v : request.getValues()) {
                    AttributeValueRisk val = new AttributeValueRisk();
                    val.setAttributeRiskId(attributeRisk.getId());
                    val.setValue(v);
                    val.setSortOrder(sort++);
                    attributeValueRiskRepository.save(val);
                }
            }
        }
        attributeRiskRepository.save(attributeRisk);
        return attributeRisk.getId();
    }
}

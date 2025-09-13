package vn.ducbackend.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ducbackend.client.SystemClient;
import vn.ducbackend.domain.dto.BasicInfoDTO;
import vn.ducbackend.domain.dto.RiskType.*;
import vn.ducbackend.domain.dto.attributeRisk.AttributeValueRiskDTO;
import vn.ducbackend.domain.entity.*;
import vn.ducbackend.exception.customException.DuplicateException;
import vn.ducbackend.exception.customException.NotFoundException;
import vn.ducbackend.mapper.AttributeGroupRiskMapper;
import vn.ducbackend.mapper.AttributeRiskMapper;
import vn.ducbackend.mapper.AttributeValueRiskMapper;
import vn.ducbackend.mapper.RiskTypeMapper;
import vn.ducbackend.repository.*;
import vn.ducbackend.repository.specs.RiskTypeSpecification;
import vn.ducbackend.repository.specs.SpecificationUtils;
import vn.ducbackend.service.RiskTypeService;
import vn.ducbackend.service.dto.SearchRiskType;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RiskTypeServiceImpl implements RiskTypeService {
    private final RiskTypeRepository riskTypeRepository;
    private final RiskTypeMapper riskTypeMapper;

    private final SystemRiskTypeRepository systemRiskTypeRepository;
    private final RiskTypeAttributeRepository riskTypeAttributeRepository;
    private final RiskTypeAttributeValueRepository riskTypeAttributeValueRepository;
    private final AttributeRiskRepository attributeRiskRepository;
    private final AttributeGroupRiskRepository attributeGroupRiskRepository;
    private final AttributeValueRiskRepository attributeValueRiskRepository;

    private final SystemClient systemClient;
    private final AttributeRiskMapper attributeRiskMapper;
    private final AttributeGroupRiskMapper attributeGroupRiskMapper;
    private final AttributeValueRiskMapper attributeValueRiskMapper;


    @Override
    @Transactional
    public Long create(RiskTypeDTO riskTypeDTO) {
        // Check code trùng
        if (riskTypeRepository.existsRiskTypesByCodeOrName(riskTypeDTO.getName(), riskTypeDTO.getCode())) {
            throw new DuplicateException("Code [" + riskTypeDTO.getCode() + "] or Name [" + riskTypeDTO.getName() + "] already exists");
        }
        // Step 1
        RiskType riskType = riskTypeMapper.toRiskTypes(riskTypeDTO);
        riskTypeRepository.save(riskType);
        List<SystemRiskType> systemRiskTypes = new ArrayList<>();
        for (BasicInfoDTO systemRiskType : riskTypeDTO.getSystems()){
            SystemRiskType systemType = new SystemRiskType();
            systemType.setRiskTypeId(riskType.getId());
            systemType.setSystemId(systemRiskType.getId());
            systemRiskTypes.add(systemType);
        }
        systemRiskTypeRepository.saveAll(systemRiskTypes);

        // Step 2: mapping Attributes + Values
        if (riskTypeDTO.getAttributes() != null && !riskTypeDTO.getAttributes().isEmpty()) {
            List<RiskTypeAttribute> riskTypeAttributes = new ArrayList<>();
            for (RiskTypeAttributeDTO riskTypeAttributeDTO : riskTypeDTO.getAttributes()) {
                RiskTypeAttribute riskTypeAttribute = new RiskTypeAttribute();
                riskTypeAttribute.setRiskTypeId(riskType.getId());
                riskTypeAttribute.setAttributesId(riskTypeAttributeDTO.getAttribute().getId());
                riskTypeAttribute.setAttributeGroupId(riskTypeAttributeDTO.getAttributeGroup().getId());
                riskTypeAttributes.add(riskTypeAttribute);
            }
            // saveAll để DB gán ID
            List<RiskTypeAttribute> savedAttrs = riskTypeAttributeRepository.saveAll(riskTypeAttributes);

            // build list values
            List<RiskTypesAttributesValue> valuesToSave = new ArrayList<>();
            for (int i = 0; i < savedAttrs.size(); i++) {
                // Lấy enity đã lưu
                RiskTypeAttribute riskTypeAttribute = savedAttrs.get(i);
                RiskTypeAttributeDTO riskTypeAttributeDTO = riskTypeDTO.getAttributes().get(i);
                List<RiskTypeAttributeValueDTO> riskTypeAttributeValueDTOS = riskTypeAttributeDTO.getAttributeValueRisks();
                if(riskTypeAttributeValueDTOS != null && !riskTypeAttributeValueDTOS.isEmpty()){
                    for(RiskTypeAttributeValueDTO attributeValueDTO : riskTypeAttributeValueDTOS){

                        RiskTypesAttributesValue attributesValue = new RiskTypesAttributesValue();

                        attributesValue.setRiskTypeAttributeId(riskTypeAttribute.getId());

                        attributesValue.setAttributeValueRiskId(attributeValueDTO.getAttributeValueRisk().getId());

                        valuesToSave.add(attributesValue);
                    }
                }
            }
            if (!valuesToSave.isEmpty()) {
                riskTypeAttributeValueRepository.saveAll(valuesToSave);
            }
        }
        return riskType.getId();
    }

    @Override
    public RiskTypeDTO getRiskType(Long id) {
        RiskType riskType = riskTypeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("RiskTypes not found with id :"+id));

        Set<Long> systemIds = systemRiskTypeRepository.findByRiskTypeId(riskType.getId())
                .stream()
                .map(SystemRiskType::getSystemId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        System.out.println("getAllSystems: "+systemIds);

        // Call API System
        List<BasicInfoDTO> systemResponseList = Collections.emptyList();
        if(!systemIds.isEmpty()){
            var response = systemClient.getAllSystems(systemIds);
            if(!systemIds.isEmpty() && response.getData() != null){
                systemResponseList = response.getData().getContent();
            }
        }

        RiskTypeDTO dto = riskTypeMapper.toDetailDTO(riskType);

        // Lấy list id attribute, attributeGroup
       List<RiskTypeAttribute> riskTypeAttributes = riskTypeAttributeRepository.findByRiskTypeId(riskType.getId());
       Set<Long> attributeIds = riskTypeAttributes.stream().map(RiskTypeAttribute::getAttributesId)
               .filter(Objects::nonNull) // tránh null
               .collect(Collectors.toSet());
       Set<Long> attributeGroupIds = riskTypeAttributes.stream().map(RiskTypeAttribute::getAttributeGroupId)
               .filter(Objects::nonNull) // tránh null
               .collect(Collectors.toSet());
       
       Map<Long, BasicInfoDTO> attributeRiskMap = attributeRiskRepository.findByIdIn(attributeIds)
               .stream().collect(Collectors
                       .toMap(AttributeRisk::getId, a ->attributeRiskMapper.toLinkDTO(a)));
       
       Map<Long, BasicInfoDTO> attributeGroupRiskMap = attributeGroupRiskRepository.findByIdIn(attributeGroupIds)
               .stream().collect(Collectors
                       .toMap(AttributeGroupRisk::getId, g->attributeGroupRiskMapper.toLinkDTO(g)));

        // Lấy all RiskTypesAttributesValue
       Set<Long> riskTypeAttributeIds = riskTypeAttributes.stream().map(RiskTypeAttribute::getId)
                .filter(Objects::nonNull) // tránh null
                .collect(Collectors.toSet());
       List<RiskTypesAttributesValue> riskTypesAttributesValueList = riskTypeAttributeValueRepository.findByRiskTypeAttributeIdIn(riskTypeAttributeIds);
       // map(riskTypeId, List<RiskTypesAttributesValue>)
        Map<Long, List<RiskTypesAttributesValue>> riskTypeMapValues = riskTypesAttributesValueList.stream()
                .collect(Collectors.groupingBy(RiskTypesAttributesValue::getRiskTypeAttributeId));

        // Lấy all attributeValue
        Set<Long> attributeValueIds = riskTypesAttributesValueList.stream().map(RiskTypesAttributesValue::getAttributeValueRiskId)
                .collect(Collectors.toSet());
        Map<Long, AttributeValueRiskDTO> attributeValueRiskMap = attributeValueRiskRepository.findByIdIn(attributeValueIds)
                .stream().collect(Collectors
                        .toMap(AttributeValueRisk::getId, v ->attributeValueRiskMapper.toAttributeValueRiskDto(v)));

       List<RiskTypeAttributeDTO> riskTypeAttributeDTOS = new ArrayList<>();
       for(RiskTypeAttribute riskTypeAttribute: riskTypeAttributes){
           RiskTypeAttributeDTO riskTypeAttributeDTO = new RiskTypeAttributeDTO();
           riskTypeAttributeDTO.setId(riskTypeAttribute.getId());

           BasicInfoDTO attributeRisk = attributeRiskMap.get(riskTypeAttribute.getAttributesId());
           if(!Objects.isNull(attributeRisk)){
               riskTypeAttributeDTO.setAttribute(attributeRisk);
           }

           BasicInfoDTO attributeGroupRisk = attributeGroupRiskMap.get(riskTypeAttribute.getAttributeGroupId());
           if(!Objects.isNull(attributeGroupRisk)){
               riskTypeAttributeDTO.setAttributeGroup(attributeGroupRisk);
           }

           List<RiskTypesAttributesValue> riskTypesAttributesValues = riskTypeMapValues.getOrDefault(riskTypeAttribute.getId(), Collections.emptyList());
           List<RiskTypeAttributeValueDTO> riskTypeAttributeValueDTOS = riskTypesAttributesValues.stream()
                   .map(v ->{
                       RiskTypeAttributeValueDTO valueDTO = new RiskTypeAttributeValueDTO();
                       valueDTO.setId(v.getId());
                       AttributeValueRiskDTO attributeValueRisk = attributeValueRiskMap.get(v.getAttributeValueRiskId());
                       valueDTO.setAttributeValueRisk(attributeValueRisk);
                       return valueDTO;
                   })
                   .toList();

           riskTypeAttributeDTO.setAttributeValueRisks(riskTypeAttributeValueDTOS);
           riskTypeAttributeDTOS.add(riskTypeAttributeDTO);
       }
       dto.setSystems(systemResponseList);
       dto.setAttributes(riskTypeAttributeDTOS);
        return dto;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // Kiểm tra tồn tại
        if (!riskTypeRepository.existsById(id)) {
            throw new NotFoundException("RiskTypes not found with id: " + id);
        }
        // Xóa mapping trước
        systemRiskTypeRepository.deleteByRiskTypeId(id);

        List<Long> riskTypeAttributeId = riskTypeAttributeRepository.findByRiskTypeId(id).stream()
                .map(RiskTypeAttribute::getId)
                .toList();

        if (!riskTypeAttributeId.isEmpty()) {
            riskTypeAttributeValueRepository.deleteByRiskTypeAttributeIdIn(riskTypeAttributeId);
            // Xoá luôn các RiskTypeAttributes
            riskTypeAttributeRepository.deleteByRiskTypeId(id);
        }
        riskTypeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Long update(RiskTypeDTO riskTypeDTO) {
        // 1. Kiểm tra tồn tại
        RiskType riskType = riskTypeRepository.findById(riskTypeDTO.getId())
                .orElseThrow(() -> new NotFoundException("RiskTypes not found with id: " + riskTypeDTO.getId()));

        // 2. Kiểm tra trùng (nếu cần)
        boolean exists = riskTypeRepository.existsByCodeOrNameAndIdNot(
                riskTypeDTO.getCode(), riskTypeDTO.getName(), riskTypeDTO.getId());
        if (exists) {
            throw new DuplicateException(
                    "Code [" + riskTypeDTO.getCode() + "] or Name [" + riskTypeDTO.getName() + "] already exists");
        }

        riskTypeMapper.toDetailDTO(riskType);
        riskTypeRepository.save(riskType);

        // 4. Xoá mapping cũ
        systemRiskTypeRepository.deleteByRiskTypeId(riskTypeDTO.getId());
        // Lấy danh sách attrId để xoá values
        List<Long> riskTypeAttributeIds = riskTypeAttributeRepository.findByRiskTypeId(riskTypeDTO.getId())
                .stream()
                .map(RiskTypeAttribute::getId)
                .toList();

        if (!riskTypeAttributeIds.isEmpty()) {
            riskTypeAttributeValueRepository.deleteByRiskTypeAttributeIdIn(riskTypeAttributeIds);
            riskTypeAttributeRepository.deleteByRiskTypeId(riskTypeDTO.getId());
        }

        // 5. Lưu mapping SystemRiskTypes mới
        if (riskTypeDTO.getSystems() != null && !riskTypeDTO.getSystems().isEmpty()) {

            List<SystemRiskType> systemRiskTypes = riskTypeDTO.getSystems().stream()
                    .map(basicInfoDTO -> {
                        SystemRiskType systemRiskType = new SystemRiskType();
                        systemRiskType.setRiskTypeId(riskTypeDTO.getId());
                        systemRiskType.setSystemId(basicInfoDTO.getId());
                        return systemRiskType;
                    })
                    .toList();
            systemRiskTypeRepository.saveAll(systemRiskTypes);
        }

        // 6. Lưu mapping Attributes + Values mới
        if (riskTypeDTO.getAttributes() != null && !riskTypeDTO.getAttributes().isEmpty()) {
            List<RiskTypeAttribute> riskTypeAttributes = new ArrayList<>();

            for (RiskTypeAttributeDTO riskTypeAttributeDTO : riskTypeDTO.getAttributes()) {
                RiskTypeAttribute attr = new RiskTypeAttribute();
                attr.setRiskTypeId(riskTypeDTO.getId());
                attr.setAttributesId(riskTypeAttributeDTO.getAttribute().getId());
                attr.setAttributeGroupId(riskTypeAttributeDTO.getAttributeGroup().getId());
                riskTypeAttributes.add(attr);
            }
            List<RiskTypeAttribute> savedAttrs = riskTypeAttributeRepository.saveAll(riskTypeAttributes);

            // Gán giá trị
            for (int i = 0; i < riskTypeAttributes.size(); i++) {
                RiskTypeAttribute riskTypeAttribute = savedAttrs.get(i);
                RiskTypeAttributeDTO typeAttributeDTO = riskTypeDTO.getAttributes().get(i);
                if (typeAttributeDTO.getAttributeValueRisks() != null && !typeAttributeDTO.getAttributeValueRisks().isEmpty()) {
                    List<RiskTypesAttributesValue> values = typeAttributeDTO.getAttributeValueRisks()
                            .stream()
                            .map(riskTypeAttributeValueDTO -> {
                                RiskTypesAttributesValue riskTypesAttributesValue = new RiskTypesAttributesValue();
                                riskTypesAttributesValue.setRiskTypeAttributeId(riskTypeAttribute.getId());
                                riskTypesAttributesValue.setAttributeValueRiskId(riskTypeAttributeValueDTO.getAttributeValueRisk().getId());
                                return riskTypesAttributesValue;
                            })
                            .toList();
                    riskTypeAttributeValueRepository.saveAll(values);
                }
            }
        }
        return riskType.getId();
    }

    @Override
    public Page<RiskTypeDTO> getListRiskType(Pageable pageable, SearchRiskType searchRiskType) {
        Specification<RiskType> spec = Specification.where(null);
        spec = SpecificationUtils.addIfNotEmpty(spec, searchRiskType.getIds(), RiskTypeSpecification::hasIds);
        spec = SpecificationUtils.addIfHasText(spec, searchRiskType.getCode(), RiskTypeSpecification::hasCode);
        spec = SpecificationUtils.addIfHasText(spec, searchRiskType.getName(), RiskTypeSpecification::hasName);
        spec = SpecificationUtils.addIfNotNull(spec, searchRiskType.getSource(), RiskTypeSpecification::hasSource);
        spec = SpecificationUtils.addIfNotNull(spec, searchRiskType.getObject(), RiskTypeSpecification::hasObject);
        spec = SpecificationUtils.addIfHasText(spec, searchRiskType.getNote(), RiskTypeSpecification::hasNote);
        spec = SpecificationUtils.addIfNotNull(spec, searchRiskType.getIsActive(), RiskTypeSpecification::hasIsActive);

        Page<RiskType> riskTypes = riskTypeRepository.findAll(spec, pageable);

        // 1. Lấy tất cả risktype
        Set<Long> riskTypeIds = riskTypes.stream().map(RiskType::getId)
                .filter(Objects::nonNull) // tránh null
                .collect(Collectors.toSet());

        // xử lý system
        List<SystemRiskType> systemRiskTypes = systemRiskTypeRepository.findByRiskTypeIdIn(riskTypeIds);
        // Map(riskTypeId, List<Long> system
        Map<Long, List<Long>> systemRiskTypeMap = systemRiskTypes.stream()
                .collect(Collectors.groupingBy(SystemRiskType::getRiskTypeId,
                Collectors.mapping(SystemRiskType::getSystemId, Collectors.toList())));
        // gọi api system 1 lần;
        Set<Long> systemRiskTypeIds = systemRiskTypes.stream().map(SystemRiskType::getSystemId)
                .filter(Objects::nonNull) // tránh null
                .collect(Collectors.toSet());
        List<BasicInfoDTO> systemList = Collections.emptyList();
        if(!systemRiskTypeIds.isEmpty()){
            var response = systemClient.getAllSystems(systemRiskTypeIds);
            if(response != null && response.getData() != null){
                systemList = response.getData().getContent();
            }
        }
        Map<Long,BasicInfoDTO> systemMap = systemList.stream()
                .collect(Collectors.toMap(BasicInfoDTO::getId, s->s));

        // 2. Lấy all RiskTypeAttribute theo riskTypeIds
        List<RiskTypeAttribute> riskTypeAttributes = riskTypeAttributeRepository.findByRiskTypeIdIn(riskTypeIds);

          // Lấy AttributeGroupRisk + Attribute + AttributeValueRisk liên quan
        Set<Long> attributeIds = riskTypeAttributes.stream().map(RiskTypeAttribute::getAttributesId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Set<Long> attributeGroupIds = riskTypeAttributes.stream().map(RiskTypeAttribute::getAttributeGroupId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        List<AttributeRisk> attributeRisks = attributeRiskRepository.findByIdIn(attributeIds);
        Map<Long, BasicInfoDTO> attributeMap = attributeRisks.stream()
                .collect(Collectors.toMap(AttributeRisk::getId, a -> attributeRiskMapper.toLinkDTO(a)));

        List<AttributeGroupRisk> attributeGroupRisks = attributeGroupRiskRepository.findByIdIn(attributeGroupIds);
        Map<Long, BasicInfoDTO> attributeGroupMap = attributeGroupRisks.stream()
                .collect(Collectors.toMap(AttributeGroupRisk::getId, g -> attributeGroupRiskMapper.toLinkDTO(g)));

        // 3. Lấy all RiskTypeAttributeValue theo RiskTypeAttributeIds
        Set<Long> riskTypeAttributeIds = riskTypeAttributes.stream().map(RiskTypeAttribute::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        List<RiskTypesAttributesValue> riskTypesAttributesValues = riskTypeAttributeValueRepository.findByRiskTypeAttributeIdIn(riskTypeAttributeIds);

        // Map(riskTypeAttributeId, List<RiskTypesAttributesValue>
        Map<Long, List<RiskTypesAttributesValue>> riskTypeAttributeMapValue = riskTypesAttributesValues.stream()
                .collect(Collectors.groupingBy(RiskTypesAttributesValue::getRiskTypeAttributeId));

        // Lấy all attribute_value_risk
        Set<Long> attributeValueRiskids = riskTypesAttributesValues.stream().map(RiskTypesAttributesValue::getAttributeValueRiskId)
                .collect(Collectors.toSet());
        List<AttributeValueRisk> attributeValueRisks = attributeValueRiskRepository.findByIdIn(attributeValueRiskids);
        Map<Long, AttributeValueRiskDTO> attributeValueRiskMap = attributeValueRisks.stream()
                .collect(Collectors.toMap(AttributeValueRisk::getId, v -> attributeValueRiskMapper.toAttributeValueRiskDto(v)));

        return riskTypes.map(riskType -> {
            RiskTypeDTO riskTypeDTO = riskTypeMapper.toDetailDTO(riskType);

            List<Long> systemIds = systemRiskTypeMap.getOrDefault(riskType.getId(), Collections.emptyList());
            List<BasicInfoDTO> systems = systemIds.stream().map(systemMap::get)
                    .filter(Objects::nonNull).toList();
            riskTypeDTO.setSystems(systems);

            List<RiskTypeAttributeDTO> riskTypeAttributeDTOS = new ArrayList<>();
            for(RiskTypeAttribute riskTypeAttribute : riskTypeAttributes.stream()
                    .filter(a -> a.getRiskTypeId().equals(riskType.getId())).toList()) {
                RiskTypeAttributeDTO riskTypeAttributeDTO = new RiskTypeAttributeDTO();
                riskTypeAttributeDTO.setId(riskTypeAttribute.getId());
                riskTypeAttributeDTO.setAttribute(attributeMap.get(riskTypeAttribute.getAttributesId()));
                riskTypeAttributeDTO.setAttributeGroup(attributeGroupMap.get(riskTypeAttribute.getAttributeGroupId()));
                // Value
                List<RiskTypeAttributeValueDTO> riskTypeAttributeValueDTOS = riskTypeAttributeMapValue.getOrDefault(riskTypeAttribute.getId(), Collections.emptyList())
                        .stream()
                        .map(v -> {
                            RiskTypeAttributeValueDTO riskTypeAttributeValueDTO = new RiskTypeAttributeValueDTO();
                            riskTypeAttributeValueDTO.setId(v.getId());
                            AttributeValueRiskDTO attributeValueRisk = attributeValueRiskMap.get(v.getAttributeValueRiskId());
                            riskTypeAttributeValueDTO.setAttributeValueRisk(attributeValueRisk);
                            return riskTypeAttributeValueDTO;
                        })
                        .toList();
                riskTypeAttributeDTO.setAttributeValueRisks(riskTypeAttributeValueDTOS);
                riskTypeAttributeDTOS.add(riskTypeAttributeDTO);
            }
            riskTypeDTO.setAttributes(riskTypeAttributeDTOS);
            return riskTypeDTO;
        });
    }
}

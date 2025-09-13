package vn.ducbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import vn.ducbackend.domain.dto.*;
import vn.ducbackend.domain.dto.attributeGroupRisk.AttributeGroupRiskDTO;
import vn.ducbackend.domain.dto.attributeGroupRisk.AttributeGroupRiskRequest;
import vn.ducbackend.domain.entity.AttributeGroupRisk;

@Mapper(componentModel = "spring")
public interface AttributeGroupRiskMapper {
    AttributeGroupRiskMapper INSTANCE = Mappers.getMapper(AttributeGroupRiskMapper.class);

    AttributeGroupRisk toAttributeGroupRisks(AttributeGroupRiskRequest request);


    AttributeGroupRiskDTO toDetailDTO(AttributeGroupRisk entity);

    // method hỗ trợ update từ DTO sang entity
    void updateAttributeGroupRiskDTO(AttributeGroupRiskDTO dto, @MappingTarget AttributeGroupRisk entity);

    BasicInfoDTO toLinkDTO(AttributeGroupRisk entity);
}

package vn.ducbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import vn.ducbackend.domain.dto.*;
import vn.ducbackend.domain.entity.AttributeGroupRisks;
import vn.ducbackend.domain.entity.RiskCategories;

@Mapper(componentModel = "spring")
public interface AttributeGroupRiskMapper {
    AttributeGroupRiskMapper INSTANCE = Mappers.getMapper(AttributeGroupRiskMapper.class);

    AttributeGroupRisks toAttributeGroupRisks(AttributeGroupRiskRequest request);


    AttributeGroupRiskDTO toDetailDTO(AttributeGroupRisks entity);

    // method hỗ trợ update từ DTO sang entity
    void updateAttributeGroupRiskDTO(AttributeGroupRiskDTO dto, @MappingTarget AttributeGroupRisks entity);

    LinkResponse toLinkDTO( AttributeGroupRisks entity );
}

package vn.ducbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import vn.ducbackend.domain.dto.*;
import vn.ducbackend.domain.entity.AttributeGroupRisks;
import vn.ducbackend.domain.entity.AttributeRisks;

@Mapper(componentModel = "spring")
public interface AttributeRiskMapper {
    AttributeRiskMapper INSTANCE = Mappers.getMapper(AttributeRiskMapper.class);

    @Mapping(target = "dataType", ignore = true) //Không tự động map
    AttributeRisks toAttributeRisks(AttributeRiskRequest request);


    AttributeRiskResponse toDetailDTO(AttributeRisks entity);

    // method hỗ trợ update từ DTO sang entity
    @Mapping(target = "dataType", ignore = true)
    void updateAttributeRiskUpdateDTO(AttributeRiskUpdateDTO dto, @MappingTarget AttributeRisks entity);

}

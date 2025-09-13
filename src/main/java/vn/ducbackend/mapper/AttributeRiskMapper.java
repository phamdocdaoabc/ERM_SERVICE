package vn.ducbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import vn.ducbackend.domain.dto.BasicInfoDTO;
import vn.ducbackend.domain.dto.attributeRisk.AttributeRiskRequest;
import vn.ducbackend.domain.dto.attributeRisk.AttributeRiskResponse;
import vn.ducbackend.domain.dto.attributeRisk.AttributeRiskUpdateDTO;
import vn.ducbackend.domain.entity.AttributeRisk;

@Mapper(componentModel = "spring")
public interface AttributeRiskMapper {
    AttributeRiskMapper INSTANCE = Mappers.getMapper(AttributeRiskMapper.class);

    @Mapping(target = "dataType", ignore = true) //Không tự động map
    AttributeRisk toAttributeRisks(AttributeRiskRequest request);


    AttributeRiskResponse toDetailDTO(AttributeRisk entity);

    // method hỗ trợ update từ DTO sang entity
    @Mapping(target = "dataType", ignore = true)
    void updateAttributeRiskUpdateDTO(AttributeRiskUpdateDTO dto, @MappingTarget AttributeRisk entity);

    BasicInfoDTO toLinkDTO(AttributeRisk entity);
}

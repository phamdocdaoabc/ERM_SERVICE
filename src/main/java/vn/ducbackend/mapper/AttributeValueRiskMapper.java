package vn.ducbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import vn.ducbackend.domain.dto.attributeRisk.AttributeValueRiskDTO;
import vn.ducbackend.domain.entity.AttributeValueRisk;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface AttributeValueRiskMapper {
    AttributeValueRiskMapper INSTANCE = Mappers.getMapper(AttributeValueRiskMapper.class);

    AttributeValueRiskDTO toAttributeValueRiskDto(AttributeValueRisk entity);

}

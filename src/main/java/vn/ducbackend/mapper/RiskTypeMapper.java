package vn.ducbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import vn.ducbackend.domain.dto.BasicInfoDTO;
import vn.ducbackend.domain.dto.RiskType.RiskTypeDTO;
import vn.ducbackend.domain.entity.RiskType;

@Mapper(componentModel = "spring")
public interface RiskTypeMapper {
    RiskTypeMapper INSTANCE = Mappers.getMapper(RiskTypeMapper.class);

    RiskType toRiskTypes(RiskTypeDTO riskTypeDTO);

    RiskTypeDTO toDetailDTO(RiskType enity);

    BasicInfoDTO toLinkDTO(RiskType entity);

}

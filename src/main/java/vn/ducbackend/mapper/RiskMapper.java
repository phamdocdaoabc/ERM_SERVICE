package vn.ducbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import vn.ducbackend.domain.dto.BasicInfoDTO;
import vn.ducbackend.domain.dto.RiskDTO;
import vn.ducbackend.domain.entity.Risk;

@Mapper(componentModel = "spring")
public interface RiskMapper {
    RiskMapper INSTANCE = Mappers.getMapper(RiskMapper.class);

    Risk toRisk(RiskDTO riskDTO);

    //RiskTypeResponse toRiskTypeResponse(RiskType enity);

    //RiskTypeDetailResponse toDetailDTO(RiskType enity);

    BasicInfoDTO toLinkDTO(Risk entity);

}

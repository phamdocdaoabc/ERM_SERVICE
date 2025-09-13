package vn.ducbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import vn.ducbackend.domain.dto.SampleActionRisk.SampleActionRiskDetailResponse;
import vn.ducbackend.domain.dto.SampleActionRisk.SampleActionRiskRequest;
import vn.ducbackend.domain.dto.SampleActionRisk.SampleActionRiskResponse;
import vn.ducbackend.domain.entity.SampleActionRisk;

@Mapper(componentModel = "spring")
public interface SampleActionRiskMapper {
    SampleActionRiskMapper INSTANCE = Mappers.getMapper(SampleActionRiskMapper.class);

    SampleActionRisk toSampleActionRisk(SampleActionRiskRequest request);

    SampleActionRiskResponse toSampleActionRiskResponse(SampleActionRisk enity);

    SampleActionRiskDetailResponse toDetailDTO(SampleActionRisk enity);

   // LinkResponse toLinkDTO(RiskTypes RiskTypes);
}

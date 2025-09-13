package vn.ducbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import vn.ducbackend.domain.dto.BasicInfoDTO;
import vn.ducbackend.domain.dto.RiskCauseTrackingDTO;
import vn.ducbackend.domain.entity.Risk;
import vn.ducbackend.domain.entity.RiskCauseTracking;

@Mapper(componentModel = "spring")
public interface RiskCauseTrackingMapper {
    RiskCauseTrackingMapper INSTANCE = Mappers.getMapper(RiskCauseTrackingMapper.class);

    RiskCauseTracking toRiskCauseTracking(RiskCauseTrackingDTO riskCauseTrackingDTO);

    BasicInfoDTO toLinkDTO(Risk entity);

}

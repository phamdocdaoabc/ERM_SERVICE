package vn.ducbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import vn.ducbackend.domain.dto.BasicInfoDTO;
import vn.ducbackend.domain.dto.SampleActionRisk.SampleActionRiskMapRequest;
import vn.ducbackend.domain.dto.SampleActionRisk.SampleActionRiskMapResponse;
import vn.ducbackend.domain.entity.SampleActionRiskMap;

@Mapper(componentModel = "spring")
public interface SampleActionRiskMapMapper {
    SampleActionRiskMapMapper INSTANCE = Mappers.getMapper(SampleActionRiskMapMapper.class);

    SampleActionRiskMap toSampleActionRisk(SampleActionRiskMapRequest request);

    @Mapping(target = "precautionId", ignore = true)
    @Mapping(target = "departmentId", ignore = true)
    SampleActionRiskMapResponse toActionRiskMapResponse(SampleActionRiskMap enity);

    //RiskTypeDetailResponse toDetailDTO(RiskType enity);

//    // Map Long -> LinkResponse
//    default LinkResponse map(Long id) {
//        if (id == null) return null;
//        return LinkResponse.builder()
//                .id(id)
//                .code(null)  // nếu cần thì set sau
//                .name(null)  // nếu cần thì set sau
//                .build();
//    }

    BasicInfoDTO toLinkDTO(SampleActionRiskMap entity);
}

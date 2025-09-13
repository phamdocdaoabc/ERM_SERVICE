package vn.ducbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import vn.ducbackend.domain.dto.*;
import vn.ducbackend.domain.dto.causes.CauseDetailResponse;
import vn.ducbackend.domain.dto.causes.CauseRequest;
import vn.ducbackend.domain.dto.causes.CauseUpdateDTO;
import vn.ducbackend.domain.entity.Cause;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CauseMapper {
    CauseMapper INSTANCE = Mappers.getMapper(CauseMapper.class);

    Cause toCauses(CauseRequest causeRequest);

    CauseDetailResponse toDetailDTO(Cause enity);

    BasicInfoDTO toLinkDTO(Cause cause);

    // method hỗ trợ update từ DTO sang entity
    void updateCausesFromDto(CauseUpdateDTO dto, @MappingTarget Cause entity);

    CauseUpdateDTO toUpdateDTO(Cause cause, List<Long> systemIds);
}

package vn.ducbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import vn.ducbackend.domain.dto.BasicInfoDTO;
import vn.ducbackend.domain.dto.Precaution.PrecautionDTO;
import vn.ducbackend.domain.dto.Precaution.PrecautionRequest;
import vn.ducbackend.domain.entity.Precaution;

@Mapper(componentModel = "spring")
public interface PrecautionMapper {
    PrecautionMapper INSTANCE = Mappers.getMapper(PrecautionMapper.class);

    Precaution toPrecautions(PrecautionRequest request);


    PrecautionDTO toDetailDTO(Precaution entity);

    // method hỗ trợ update từ DTO sang entity
    void updatePrecaution(PrecautionDTO dto, @MappingTarget Precaution entity);

    BasicInfoDTO toLinkDTO(Precaution entity );
}

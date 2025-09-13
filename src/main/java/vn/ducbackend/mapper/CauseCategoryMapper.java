package vn.ducbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import vn.ducbackend.domain.dto.BasicInfoDTO;
import vn.ducbackend.domain.dto.causesCategory.CauseCategoryDetailRequest;
import vn.ducbackend.domain.dto.causesCategory.CauseCategoryDetailResponse;
import vn.ducbackend.domain.entity.CauseCategory;

@Mapper(componentModel = "spring")
public interface CauseCategoryMapper {
    CauseCategoryMapper INSTANCE = Mappers.getMapper(CauseCategoryMapper.class);

    CauseCategory toCauseCategory(CauseCategoryDetailRequest causeCategoryDTO);


    CauseCategoryDetailResponse toDetailDTO(CauseCategory entity);
    // method hỗ trợ update từ DTO sang entity
    void updateCauseCategoryFromDto(CauseCategoryDetailRequest dto, @MappingTarget CauseCategory entity);

    BasicInfoDTO toLinkDTO(CauseCategory entity);
}

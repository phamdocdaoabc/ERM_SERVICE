package vn.ducbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import vn.ducbackend.domain.dto.*;
import vn.ducbackend.domain.entity.CauseCategories;
import vn.ducbackend.domain.entity.Systems;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CauseCategoryMapper {
    CauseCategoryMapper INSTANCE = Mappers.getMapper(CauseCategoryMapper.class);

    CauseCategories toCauseCategory(CauseCategoryDetailRequest causeCategoryDTO);

    CauseCategoryDTO toCauseCategoryDto(CauseCategories causeCategories);

    CauseCategoryDetailResponse toDetailDTO(CauseCategories entity);
    // method hỗ trợ update từ DTO sang entity
    void updateCauseCategoryFromDto(CauseCategoryUpdateDTO dto, @MappingTarget CauseCategories entity);

    CauseCategoryUpdateDTO toUpdateDto(CauseCategories entity, List<Long> systemIds);
}

package vn.ducbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import vn.ducbackend.domain.dto.*;
import vn.ducbackend.domain.dto.riskCategory.RiskCategoryDetailResponse;
import vn.ducbackend.domain.dto.riskCategory.RiskCategoryRequest;
import vn.ducbackend.domain.dto.riskCategory.RiskCategoryUpdateDTO;
import vn.ducbackend.domain.entity.RiskCategories;

@Mapper(componentModel = "spring")
public interface RiskCategoryMapper {
    RiskCategoryMapper INSTANCE = Mappers.getMapper(RiskCategoryMapper.class);

    RiskCategories toRiskCategories(RiskCategoryRequest riskCategoryRequest);


    RiskCategoryDetailResponse toDetailDTO(RiskCategories entity);

    // method hỗ trợ update từ DTO sang entity
    void updateRiskCategoryFromDto(RiskCategoryUpdateDTO dto, @MappingTarget RiskCategories entity);

    LinkResponse toLinkDTO( RiskCategories entity );
}

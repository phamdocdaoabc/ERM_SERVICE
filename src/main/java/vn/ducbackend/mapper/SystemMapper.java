package vn.ducbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import vn.ducbackend.domain.dto.SystemRequest;
import vn.ducbackend.domain.dto.SystemResponse;
import vn.ducbackend.domain.entity.Systems;

@Mapper(componentModel = "spring")
public interface SystemMapper {
    SystemMapper INSTANCE = Mappers.getMapper(SystemMapper.class);

    Systems toSystem(SystemRequest request);

    SystemResponse toSystemResponse(Systems systems);

    // method hỗ trợ update từ DTO sang entity
    void updateSystemFromDto(SystemResponse dto, @MappingTarget Systems entity);
}

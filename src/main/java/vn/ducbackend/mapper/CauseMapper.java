package vn.ducbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import vn.ducbackend.domain.dto.CauseRequest;
import vn.ducbackend.domain.dto.CauseResponse;
import vn.ducbackend.domain.entity.Causes;

@Mapper(componentModel = "spring")
public interface CauseMapper {
    CauseMapper INSTANCE = Mappers.getMapper(CauseMapper.class);

    Causes toCauses(CauseRequest causeRequest);

    CauseResponse toCauseResponse(Causes causes);
}

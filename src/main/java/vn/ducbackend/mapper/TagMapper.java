package vn.ducbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import vn.ducbackend.domain.dto.Tag.TagRequest;
import vn.ducbackend.domain.dto.Tag.TagResponse;
import vn.ducbackend.domain.entity.Tag;

@Mapper(componentModel = "spring")
public interface TagMapper {
    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    Tag toTag(TagRequest request);

    TagResponse toResponse(Tag enity);

    void updateTagFromRequest(TagRequest request, @MappingTarget Tag tag);
}

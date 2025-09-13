package vn.ducbackend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.ducbackend.domain.dto.Tag.TagRequest;
import vn.ducbackend.domain.dto.Tag.TagResponse;

import java.util.Set;

public interface TagService {
    Long create(TagRequest request);

    TagResponse getTag(Long id);

    Page<TagResponse> getListTag(Pageable pageable, Set<Long> ids);

    Long update(TagRequest request);

    void delete(Long id);
}

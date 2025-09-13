package vn.ducbackend.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ducbackend.domain.dto.Tag.TagRequest;
import vn.ducbackend.domain.dto.Tag.TagResponse;
import vn.ducbackend.domain.entity.Tag;
import vn.ducbackend.exception.customException.DuplicateException;
import vn.ducbackend.exception.customException.NotFoundException;
import vn.ducbackend.mapper.TagMapper;
import vn.ducbackend.repository.TagRepository;
import vn.ducbackend.service.TagService;

import java.util.Set;


@Service
@RequiredArgsConstructor
@Slf4j
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;


    @Override
    @Transactional
    public Long create(TagRequest request) {
        // Check name đã toond tại
        if (tagRepository.existsByName(request.getName())) {
            throw new DuplicateException( "Name [" + request.getName() + "] already exists");
        }
        Tag tag = tagMapper.toTag(request);
        tagRepository.save(tag);
        return tag.getId();
    }

    @Override
    public TagResponse getTag(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tag not found with id: " + id));
        return tagMapper.toResponse(tag);
    }

    @Override
    public Page<TagResponse> getListTag(Pageable pageable, Set<Long> ids) {
        Page<Tag> tags;
        if (ids != null && !ids.isEmpty()) {
            tags = tagRepository.findByIdIn(ids, pageable);
        } else {
            tags = tagRepository.findAll(pageable);
        }
        return tags.map(tag -> tagMapper.toResponse(tag));
    }

    @Override
    @Transactional
    public Long update(TagRequest request) {
        Tag tag = tagRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("Tag not found with id: " + request.getId()));

        // check trùng tên
        if (tagRepository.existsByNameAndIdNot(request.getName(), request.getId())) {
            throw new DuplicateException("Tag with name '" + request.getName() + "' already exists.");
        }

        tagMapper.updateTagFromRequest(request, tag);
        tagRepository.save(tag);
        return tag.getId();
    }

    @Override
    public void delete(Long id) {
        // Kiểm tra tồn tại
        if (!tagRepository.existsById(id)) {
            throw new NotFoundException("tag not found with id: " + id);
        }

        // Xóa CauseCategory
        tagRepository.deleteById(id);
    }
}
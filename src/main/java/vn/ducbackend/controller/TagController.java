package vn.ducbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import vn.ducbackend.domain.ApiResponse;
import vn.ducbackend.domain.IdsResponse;
import vn.ducbackend.domain.PageResponse;
import vn.ducbackend.domain.dto.Tag.TagRequest;
import vn.ducbackend.domain.dto.Tag.TagResponse;
import vn.ducbackend.service.TagService;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/tag")
@RequiredArgsConstructor
@Slf4j
public class TagController {

    private final TagService tagService;

    @PostMapping
    ApiResponse<IdsResponse<Long>> create(@RequestBody @Valid TagRequest request) {
        return ApiResponse.<IdsResponse<Long>>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString()) // chuỗi UUID random
                .data(IdsResponse.<Long>builder()
                        .id(tagService.create(request))
                        .build()
                )
                .build();
    }

    @GetMapping()
    ApiResponse<TagResponse> getTag(@RequestParam Long id) {
        TagResponse response = tagService.getTag(id);
        return ApiResponse.<TagResponse>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString()) // chuỗi UUID random
                .data(response)
                .build();
    }

    // Nhận tham số phân trang từ request (page, size, sort)
    @GetMapping("/list")
    public ApiResponse<PageResponse<TagResponse>> getListTag(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) Set<Long> ids
    ) {
        Page<TagResponse> pageResult = tagService.getListTag(pageable, ids);
        // map sang response chuẩn của bạn
        PageResponse<TagResponse> response = PageResponse.<TagResponse>builder()
                .content(pageResult.getContent())
                .page(pageResult.getNumber())
                .size(pageResult.getSize())
                .sort(pageable.getSort().toString())
                .totalElements(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .numberOfElements(pageResult.getNumberOfElements())
                .build();

        return ApiResponse.<PageResponse<TagResponse>>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString()) // chuỗi UUID random
                .data(response)
                .build();
    }


    // API chỉnh sửa
    @PutMapping
    ApiResponse<IdsResponse<Long>> update(@RequestBody @Valid TagRequest request) {
        return ApiResponse.<IdsResponse<Long>>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString())
                .data(IdsResponse.<Long>builder()
                        .id(tagService.update(request))
                        .build()
                )
                .build();
    }

    @DeleteMapping()
    ApiResponse<String> delete(@RequestParam Long id) {
        tagService.delete(id);
        return ApiResponse.<String>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString()) // chuỗi UUID random
                .data("Delete Cause Category Scuccessfully")
                .build();
    }

}

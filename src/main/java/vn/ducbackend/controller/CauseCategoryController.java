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
import vn.ducbackend.domain.dto.*;
import vn.ducbackend.service.CauseCategoryService;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/cause-categories")
@RequiredArgsConstructor
@Slf4j
public class CauseCategoryController {

    private final CauseCategoryService causeCategoryService;

    // API tạo mới cause category
    @PostMapping
    ApiResponse<IdsResponse<Long>> createCauseCategory(@RequestBody @Valid CauseCategoryDetailRequest request) {
        return ApiResponse.<IdsResponse<Long>>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString()) // chuỗi UUID random
                .data(IdsResponse.<Long>builder()
                        .id(causeCategoryService.create(request))
                        .build()
                )
                .build();
    }

    // API lấy chi tiết cause category theo id
    @GetMapping()
    ApiResponse<CauseCategoryDetailResponse> getDetail(@RequestParam Long id) {
        CauseCategoryDetailResponse response = causeCategoryService.getCauseCategory(id);
        return ApiResponse.<CauseCategoryDetailResponse>builder()
                .data(response)
                .build();
    }

    // API list all cause category
    // Nhận tham số phân trang từ request (page, size, sort)
    @GetMapping("/list")
    public ApiResponse<PageResponse<CauseCategoryDetailResponse>> getAll(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) Set<Long> ids
    ) {
        Page<CauseCategoryDetailResponse> pageResult = causeCategoryService.getListCauseCategory(pageable, ids);
        // map sang response chuẩn của bạn
        PageResponse<CauseCategoryDetailResponse> response = PageResponse.<CauseCategoryDetailResponse>builder()
                .content(pageResult.getContent())
                .page(pageResult.getNumber())
                .size(pageResult.getSize())
                .sort(pageable.getSort().toString())
                .totalElements(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .numberOfElements(pageResult.getNumberOfElements())
                .build();

        return ApiResponse.<PageResponse<CauseCategoryDetailResponse>>builder()
                .data(response)
                .build();
    }


    // API chỉnh sửa
    @PutMapping
    ApiResponse<IdsResponse<Long>> updateCauseCategory(@RequestBody @Valid CauseCategoryUpdateDTO request) {
        try {
            return ApiResponse.<IdsResponse<Long>>builder()
                    .message("Successfully")
                    .traceId(UUID.randomUUID().toString())
                    .data(IdsResponse.<Long>builder()
                            .id(causeCategoryService.update(request))
                            .build()
                    )
                    .build();
        } catch (Exception e) {
            return ApiResponse.<IdsResponse<Long>>builder()
                    .message("Error : " + e.getMessage())
                    .build();
        }
    }

    @DeleteMapping()
    ApiResponse<String> deleteCauseCategory(@RequestParam Long id) {
        causeCategoryService.delete(id);
        return ApiResponse.<String>builder()
                .data("Delete Cause Category Scuccessfully")
                .build();
    }
}

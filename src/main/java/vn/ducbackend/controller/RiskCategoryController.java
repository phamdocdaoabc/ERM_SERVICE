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
import vn.ducbackend.domain.dto.riskCategory.RiskCategoryDetailResponse;
import vn.ducbackend.domain.dto.riskCategory.RiskCategoryRequest;
import vn.ducbackend.domain.dto.riskCategory.RiskCategoryUpdateDTO;
import vn.ducbackend.service.RiskCategoryService;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/risk-categories")
@RequiredArgsConstructor
@Slf4j
public class RiskCategoryController {

    private final RiskCategoryService riskCategoryService;

    @PostMapping
    ApiResponse<IdsResponse<Long>> create(@RequestBody @Valid RiskCategoryRequest request) {
        return ApiResponse.<IdsResponse<Long>>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString()) // chuỗi UUID random
                .data(IdsResponse.<Long>builder()
                        .id(riskCategoryService.create(request))
                        .build()
                )
                .build();
    }

    @GetMapping()
    ApiResponse<RiskCategoryDetailResponse> getRiskCategory(@RequestParam Long id) {
        RiskCategoryDetailResponse response = riskCategoryService.getRiskCategory(id);
        return ApiResponse.<RiskCategoryDetailResponse>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString()) // chuỗi UUID random
                .data(response)
                .build();
    }

    // Nhận tham số phân trang từ request (page, size, sort)
    @GetMapping("/list")
    public ApiResponse<PageResponse<RiskCategoryDetailResponse>> getListRiskCategory(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) Set<Long> ids
    ) {
        Page<RiskCategoryDetailResponse> pageResult = riskCategoryService.getListRiskCategory(pageable, ids);
        // map sang response chuẩn của bạn
        PageResponse<RiskCategoryDetailResponse> response = PageResponse.<RiskCategoryDetailResponse>builder()
                .content(pageResult.getContent())
                .page(pageResult.getNumber())
                .size(pageResult.getSize())
                .sort(pageable.getSort().toString())
                .totalElements(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .numberOfElements(pageResult.getNumberOfElements())
                .build();

        return ApiResponse.<PageResponse<RiskCategoryDetailResponse>>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString()) // chuỗi UUID random
                .data(response)
                .build();
    }


    // API chỉnh sửa
    @PutMapping
    ApiResponse<IdsResponse<Long>> update(@RequestBody @Valid RiskCategoryUpdateDTO request) {
        return ApiResponse.<IdsResponse<Long>>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString())
                .data(IdsResponse.<Long>builder()
                        .id(riskCategoryService.update(request))
                        .build()
                )
                .build();
    }

    @DeleteMapping()
    ApiResponse<String> delete(@RequestParam Long id) {
        riskCategoryService.delete(id);
        return ApiResponse.<String>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString()) // chuỗi UUID random
                .data("Delete Cause Category Scuccessfully")
                .build();
    }


}

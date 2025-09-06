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
import vn.ducbackend.domain.dto.AttributeGroupRiskDTO;
import vn.ducbackend.domain.dto.AttributeRiskRequest;
import vn.ducbackend.domain.dto.AttributeRiskResponse;
import vn.ducbackend.domain.dto.AttributeRiskUpdateDTO;
import vn.ducbackend.service.AttributeRisksService;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/attribute-risk")
@RequiredArgsConstructor
@Slf4j
public class AttributeRiskController {

    private final AttributeRisksService attributeRiskService;

    // API tạo mới cause category
    @PostMapping
    ApiResponse<IdsResponse<Long>> create(@RequestBody @Valid AttributeRiskRequest request) {
        return ApiResponse.<IdsResponse<Long>>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString()) // chuỗi UUID random
                .data(IdsResponse.<Long>builder()
                        .id(attributeRiskService.create(request))
                        .build()
                )
                .build();
    }

    // API lấy chi tiết risk category theo id
    @GetMapping()
    ApiResponse<AttributeRiskResponse> getAttributeRisk(@RequestParam Long id) {
        AttributeRiskResponse response = attributeRiskService.getAttributeRisk(id);
        return ApiResponse.<AttributeRiskResponse>builder()
                .data(response)
                .build();
    }

    // API list all cause category
    // Nhận tham số phân trang từ request (page, size, sort)
    @GetMapping("/list")
    public ApiResponse<PageResponse<AttributeRiskResponse>> getListAttributeRisk(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) Set<Long> ids
    ) {
        Page<AttributeRiskResponse> pageResult = attributeRiskService.getListAttributeRisk(pageable, ids);
        // map sang response chuẩn của bạn
        PageResponse<AttributeRiskResponse> response = PageResponse.<AttributeRiskResponse>builder()
                .content(pageResult.getContent())
                .page(pageResult.getNumber())
                .size(pageResult.getSize())
                .sort(pageable.getSort().toString())
                .totalElements(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .numberOfElements(pageResult.getNumberOfElements())
                .build();

        return ApiResponse.<PageResponse<AttributeRiskResponse>>builder()
                .data(response)
                .build();
    }

    @DeleteMapping()
    ApiResponse<String> delete(@RequestParam Long id) {
        attributeRiskService.delete(id);
        return ApiResponse.<String>builder()
                .data("Delete Cause Category Scuccessfully")
                .build();
    }

    @PutMapping
    ApiResponse<IdsResponse<Long>> update(@RequestBody @Valid AttributeRiskUpdateDTO request) {
        return ApiResponse.<IdsResponse<Long>>builder()
                .message("Update Successfully")
                .traceId(UUID.randomUUID().toString())
                .data(IdsResponse.<Long>builder()
                        .id(attributeRiskService.update(request))
                        .build()
                )
                .build();
    }

}

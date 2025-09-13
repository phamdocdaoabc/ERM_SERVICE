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
import vn.ducbackend.domain.dto.attributeGroupRisk.AttributeGroupRiskDTO;
import vn.ducbackend.domain.dto.attributeGroupRisk.AttributeGroupRiskRequest;
import vn.ducbackend.service.AttributeGroupRiskService;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/attribute-group-risk")
@RequiredArgsConstructor
@Slf4j
public class AttributeGroupRiskController {

    private final AttributeGroupRiskService attributeGroupRiskService;

    @PostMapping
    ApiResponse<IdsResponse<Long>> create(@RequestBody @Valid AttributeGroupRiskRequest request) {
        return ApiResponse.<IdsResponse<Long>>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString()) // chuỗi UUID random
                .data(IdsResponse.<Long>builder()
                        .id(attributeGroupRiskService.create(request))
                        .build()
                )
                .build();
    }

    @GetMapping()
    ApiResponse<AttributeGroupRiskDTO> getAttributeGroupRisk(@RequestParam Long id) {
        AttributeGroupRiskDTO response = attributeGroupRiskService.getAttributeGroupRisk(id);
        return ApiResponse.<AttributeGroupRiskDTO>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString()) // chuỗi UUID random
                .data(response)
                .build();
    }

    // Nhận tham số phân trang từ request (page, size, sort)
    @GetMapping("/list")
    public ApiResponse<PageResponse<AttributeGroupRiskDTO>> getListAttributeGroupRisk(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) Set<Long> ids
    ) {
        Page<AttributeGroupRiskDTO> pageResult = attributeGroupRiskService.getListAttributeGroupRisk(pageable, ids);
        // map sang response chuẩn của bạn
        PageResponse<AttributeGroupRiskDTO> response = PageResponse.<AttributeGroupRiskDTO>builder()
                .content(pageResult.getContent())
                .page(pageResult.getNumber())
                .size(pageResult.getSize())
                .sort(pageable.getSort().toString())
                .totalElements(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .numberOfElements(pageResult.getNumberOfElements())
                .build();

        return ApiResponse.<PageResponse<AttributeGroupRiskDTO>>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString()) // chuỗi UUID random
                .data(response)
                .build();
    }


    // API chỉnh sửa
    @PutMapping
    ApiResponse<IdsResponse<Long>> update(@RequestBody @Valid AttributeGroupRiskDTO request) {
        return ApiResponse.<IdsResponse<Long>>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString())
                .data(IdsResponse.<Long>builder()
                        .id(attributeGroupRiskService.update(request))
                        .build()
                )
                .build();
    }

    @DeleteMapping()
    ApiResponse<String> delete(@RequestParam Long id) {
        attributeGroupRiskService.delete(id);
        return ApiResponse.<String>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString()) // chuỗi UUID random
                .data("Delete Cause Category Scuccessfully")
                .build();
    }

}

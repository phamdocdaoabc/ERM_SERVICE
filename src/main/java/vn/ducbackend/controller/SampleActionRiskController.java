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
import vn.ducbackend.domain.dto.SampleActionRisk.SampleActionRiskDetailResponse;
import vn.ducbackend.domain.dto.SampleActionRisk.SampleActionRiskRequest;
import vn.ducbackend.domain.dto.SampleActionRisk.SampleActionRiskResponse;
import vn.ducbackend.service.SampleActionRiskService;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/sample-action-risk")
@RequiredArgsConstructor
@Slf4j
public class SampleActionRiskController {
    private final SampleActionRiskService sampleActionRiskService;

    @PostMapping
    ApiResponse<IdsResponse<Long>> create(@RequestBody @Valid SampleActionRiskRequest request) {
        return ApiResponse.<IdsResponse<Long>>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString()) // chuỗi UUID random
                .data(IdsResponse.<Long>builder()
                        .id(sampleActionRiskService.create(request))
                        .build()
                )
                .build();
    }

    @GetMapping("/list")
    public ApiResponse<PageResponse<SampleActionRiskResponse>> getListSampleAction(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) Set<Long> ids
    ) {
        Page<SampleActionRiskResponse> pageResult = sampleActionRiskService.getListSampleAction(pageable, ids);
        // map sang response chuẩn của bạn
        PageResponse<SampleActionRiskResponse> response = PageResponse.<SampleActionRiskResponse>builder()
                .content(pageResult.getContent())
                .page(pageResult.getNumber())
                .size(pageResult.getSize())
                .sort(pageable.getSort().toString())
                .totalElements(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .numberOfElements(pageResult.getNumberOfElements())
                .build();

        return ApiResponse.<PageResponse<SampleActionRiskResponse>>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString()) // chuỗi UUID random
                .data(response)
                .build();
    }


    @GetMapping()
    ApiResponse<SampleActionRiskDetailResponse> getSampleAction(@RequestParam Long id) {
        SampleActionRiskDetailResponse response = sampleActionRiskService.getSampleAction(id);
        return ApiResponse.<SampleActionRiskDetailResponse>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString()) // chuỗi UUID random
                .data(response)
                .build();
    }

    // API chỉnh sửa
    @PutMapping
    ApiResponse<IdsResponse<Long>> update(@RequestBody @Valid SampleActionRiskRequest request) {
        return ApiResponse.<IdsResponse<Long>>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString())
                .data(IdsResponse.<Long>builder()
                        .id(sampleActionRiskService.update(request))
                        .build()
                )
                .build();
    }

    @DeleteMapping()
    ApiResponse<String> delete(@RequestParam Long id) {
        sampleActionRiskService.delete(id);
        return ApiResponse.<String>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString()) // chuỗi UUID random
                .data("Delete Sample Action Scuccessfully")
                .build();
    }
}

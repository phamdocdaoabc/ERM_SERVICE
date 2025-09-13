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
import vn.ducbackend.domain.dto.causes.CauseDetailResponse;
import vn.ducbackend.domain.dto.causes.CauseRequest;
import vn.ducbackend.domain.dto.causes.CauseSearchRequest;
import vn.ducbackend.domain.dto.causes.CauseUpdateDTO;
import vn.ducbackend.domain.enums.Source;
import vn.ducbackend.service.CauseService;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/cause")
@RequiredArgsConstructor
@Slf4j
public class CauseController {
    private final CauseService causeService;

    @PostMapping
    ApiResponse<IdsResponse<Long>> create(@RequestBody CauseRequest request) {
        return ApiResponse.<IdsResponse<Long>>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString()) // chuỗi UUID random
                .data(IdsResponse.<Long>builder()
                        .id(causeService.create(request))
                        .build()
                )
                .build();
    }

    // API list all causes
    // Nhận tham số phân trang từ request (page, size, sort)
    @GetMapping("/list")
    public ApiResponse<PageResponse<CauseDetailResponse>> getListCause(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) Set<Long> ids
    ) {
        Page<CauseDetailResponse> pageResult = causeService.getListCause(pageable, ids);
        // map sang response chuẩn của bạn
        PageResponse<CauseDetailResponse> response = PageResponse.<CauseDetailResponse>builder()
                .content(pageResult.getContent())
                .page(pageResult.getNumber())
                .size(pageResult.getSize())
                .sort(pageable.getSort().toString())
                .totalElements(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .numberOfElements(pageResult.getNumberOfElements())
                .build();

        return ApiResponse.<PageResponse<CauseDetailResponse>>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString()) // chuỗi UUID random
                .data(response)
                .build();
    }

    // API lấy chi tiết cause category theo id
    @GetMapping()
    ApiResponse<CauseDetailResponse> getCauses(@RequestParam Long id) {
        CauseDetailResponse response = causeService.getCause(id);
        return ApiResponse.<CauseDetailResponse>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString()) // chuỗi UUID random
                .data(response)
                .build();
    }

    // API chỉnh sửa
    @PutMapping
    ApiResponse<IdsResponse<Long>> update(@RequestBody @Valid CauseUpdateDTO request) {
        return ApiResponse.<IdsResponse<Long>>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString())
                .data(IdsResponse.<Long>builder()
                        .id(causeService.update(request))
                        .build()
                )
                .build();
    }

    @DeleteMapping()
    ApiResponse<String> delete(@RequestParam Long id) {
        causeService.delete(id);
        return ApiResponse.<String>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString()) // chuỗi UUID random
                .data("Delete Cause Category Scuccessfully")
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<PageResponse<CauseDetailResponse>> getAll(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long causeCategoryId,
            @RequestParam(required = false) Source source,
            @RequestParam(required = false) Boolean isActive
    ) {
        CauseSearchRequest request = new CauseSearchRequest();
        request.setCode(code);
        request.setName(name);
        request.setCauseCategoryId(causeCategoryId);
        request.setSource(source);
        request.setIsActive(isActive);
        Page<CauseDetailResponse> pageResult =
                causeService.searchCause(pageable, request);

        PageResponse<CauseDetailResponse> response = PageResponse.<CauseDetailResponse>builder()
                .content(pageResult.getContent())
                .page(pageResult.getNumber())
                .size(pageResult.getSize())
                .totalElements(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .numberOfElements(pageResult.getNumberOfElements())
                .sort(pageable.getSort().toString())
                .build();

        return ApiResponse.<PageResponse<CauseDetailResponse>>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString()) // chuỗi UUID random
                .data(response)
                .build();
    }
}

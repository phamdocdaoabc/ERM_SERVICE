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
import vn.ducbackend.domain.dto.Precaution.PrecautionDTO;
import vn.ducbackend.domain.dto.Precaution.PrecautionRequest;
import vn.ducbackend.service.PrecautionService;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/precaution")
@RequiredArgsConstructor
@Slf4j
public class PrecautionController {

    private final PrecautionService precautionService;
    
    @PostMapping
    ApiResponse<IdsResponse<Long>> create(@RequestBody @Valid PrecautionRequest request) {
        return ApiResponse.<IdsResponse<Long>>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString()) // chuỗi UUID random
                .data(IdsResponse.<Long>builder()
                        .id(precautionService.create(request))
                        .build()
                )
                .build();
    }
    
    @GetMapping()
    ApiResponse<PrecautionDTO> getPrecaution(@RequestParam Long id) {
        PrecautionDTO response = precautionService.getPrecaution(id);
        return ApiResponse.<PrecautionDTO>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString()) // chuỗi UUID random
                .data(response)
                .build();
    }

    // API list all cause category
    // Nhận tham số phân trang từ request (page, size, sort)
    @GetMapping("/list")
    public ApiResponse<PageResponse<PrecautionDTO>> getListPrecaution(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) Set<Long> ids
    ) {
        Page<PrecautionDTO> pageResult = precautionService.getListPrecaution(pageable, ids);
        // map sang response chuẩn của bạn
        PageResponse<PrecautionDTO> response = PageResponse.<PrecautionDTO>builder()
                .content(pageResult.getContent())
                .page(pageResult.getNumber())
                .size(pageResult.getSize())
                .sort(pageable.getSort().toString())
                .totalElements(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .numberOfElements(pageResult.getNumberOfElements())
                .build();

        return ApiResponse.<PageResponse<PrecautionDTO>>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString()) // chuỗi UUID random
                .data(response)
                .build();
    }


    // API chỉnh sửa
    @PutMapping
    ApiResponse<IdsResponse<Long>> update(@RequestBody @Valid PrecautionDTO request) {
        return ApiResponse.<IdsResponse<Long>>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString())
                .data(IdsResponse.<Long>builder()
                        .id(precautionService.update(request))
                        .build()
                )
                .build();
    }

    @DeleteMapping()
    ApiResponse<String> delete(@RequestParam Long id) {
        precautionService.delete(id);
        return ApiResponse.<String>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString()) // chuỗi UUID random
                .data("Delete Cause Category Scuccessfully")
                .build();
    }

}

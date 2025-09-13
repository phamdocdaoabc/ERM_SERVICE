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
import vn.ducbackend.domain.dto.RiskType.RiskTypeDTO;
import vn.ducbackend.domain.enums.PartyType;
import vn.ducbackend.domain.enums.Source;
import vn.ducbackend.service.RiskTypeService;
import vn.ducbackend.service.dto.SearchRiskType;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/risk-type")
@RequiredArgsConstructor
@Slf4j
public class RiskTypeController {
    private final RiskTypeService riskTypeService;

    @PostMapping
    ApiResponse<IdsResponse<Long>> create(@RequestBody @Valid RiskTypeDTO request) {
        return ApiResponse.<IdsResponse<Long>>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString()) // chuỗi UUID random
                .data(IdsResponse.<Long>builder()
                        .id(riskTypeService.create(request))
                        .build()
                )
                .build();
    }

    @GetMapping("/list")
    public ApiResponse<PageResponse<RiskTypeDTO>> getListRiskType(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) Set<Long> ids,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Source source,
            @RequestParam(required = false) PartyType object,
            @RequestParam(required = false) String note,
            @RequestParam(required = false) Boolean isActive
    ) {
        Page<RiskTypeDTO> pageResult = riskTypeService.getListRiskType(pageable, SearchRiskType.builder()
                        .ids(ids)
                        .code(code)
                        .name(name)
                        .source(source)
                        .object(object)
                        .note(note)
                        .isActive(isActive)
                        .build());
        // map sang response chuẩn của bạn
        PageResponse<RiskTypeDTO> response = PageResponse.<RiskTypeDTO>builder()
                .content(pageResult.getContent())
                .page(pageResult.getNumber())
                .size(pageResult.getSize())
                .sort(pageable.getSort().toString())
                .totalElements(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .numberOfElements(pageResult.getNumberOfElements())
                .build();

        return ApiResponse.<PageResponse<RiskTypeDTO>>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString()) // chuỗi UUID random
                .data(response)
                .build();
    }


    @GetMapping()
    ApiResponse<RiskTypeDTO> getRiskType(@RequestParam Long id) {
        RiskTypeDTO response = riskTypeService.getRiskType(id);
        return ApiResponse.<RiskTypeDTO>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString()) // chuỗi UUID random
                .data(response)
                .build();
    }

    // API chỉnh sửa
    @PutMapping
    ApiResponse<IdsResponse<Long>> update(@RequestBody @Valid RiskTypeDTO request) {
        return ApiResponse.<IdsResponse<Long>>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString())
                .data(IdsResponse.<Long>builder()
                        .id(riskTypeService.update(request))
                        .build()
                )
                .build();
    }

    @DeleteMapping()
    ApiResponse<String> delete(@RequestParam Long id) {
        riskTypeService.delete(id);
        return ApiResponse.<String>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString()) // chuỗi UUID random
                .data("Delete Cause Category Scuccessfully")
                .build();
    }
}

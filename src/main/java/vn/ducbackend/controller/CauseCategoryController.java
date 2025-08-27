package vn.ducbackend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import vn.ducbackend.domain.ApiResponse;
import vn.ducbackend.domain.PageResponse;
import vn.ducbackend.domain.dto.*;
import vn.ducbackend.service.CauseCategoryService;

import java.util.List;

@RestController
@RequestMapping("/cause-categories")
@RequiredArgsConstructor
@Slf4j
public class CauseCategoryController {

    private final CauseCategoryService causeCategoryService;
    // API tạo mới cause category
    @PostMapping
    ApiResponse<CauseCategoryDetailResponse> createCauseCategory(@RequestBody CauseCategoryDetailRequest request) {
        ApiResponse<CauseCategoryDetailResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(causeCategoryService.createCauseCategory(request));
        return apiResponse;
    }

    // API lấy chi tiết cause category theo id
    @GetMapping()
    ApiResponse<CauseCategoryDetailResponse> getDetail(@RequestParam Long id) {
        CauseCategoryDetailResponse response = causeCategoryService.getDetail(id);
        return ApiResponse.<CauseCategoryDetailResponse>builder()
                .data(response)
                .build();
    }

    // API list all cause category
    // Nhận tham số phân trang từ request (page, size, sort)
    @GetMapping("/list")
    public ApiResponse<PageResponse<CauseCategoryDetailResponse>> getAll(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<CauseCategoryDetailResponse> pageResult = causeCategoryService.getAllCauseCategory(pageable);

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
    ApiResponse<CauseCategoryUpdateDTO>  updateCauseCategory(@RequestBody CauseCategoryUpdateDTO request){
        return ApiResponse.<CauseCategoryUpdateDTO>builder()
                .data(causeCategoryService.updateCauseCategory(request))
                .build();
    }

    @DeleteMapping()
    ApiResponse<String> deleteCauseCategory(@RequestParam Long id){
        causeCategoryService.deleteCauseCategory(id);
        return ApiResponse.<String>builder()
                .data("Delete Cause Category Scuccessfully")
                .build();
    }
}

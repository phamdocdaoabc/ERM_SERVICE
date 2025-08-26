package vn.ducbackend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import vn.ducbackend.domain.dto.*;
import vn.ducbackend.service.CauseCategoryService;

@RestController
@RequestMapping("/cause-categories")
@RequiredArgsConstructor
@Slf4j
public class CauseCategoryController {

    private final CauseCategoryService causeCategoryService;
    // API tạo mới cause category
    @PostMapping
    ApiResponse<CauseCategoryDTO> createCauseCategory(@RequestBody CauseCategoryDetailRequest request) {
        ApiResponse<CauseCategoryDTO> apiResponse = new ApiResponse<>();
        apiResponse.setData(causeCategoryService.createCauseCategory(request));
        return apiResponse;
    }

    // API lấy chi tiết cause category theo id
    @GetMapping
    ApiResponse<CauseCategoryDetailResponse> getDetail(@RequestParam Long id) {
        CauseCategoryDetailResponse response = causeCategoryService.getDetail(id);
        return ApiResponse.<CauseCategoryDetailResponse>builder()
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

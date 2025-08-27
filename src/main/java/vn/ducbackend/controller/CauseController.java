package vn.ducbackend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.ducbackend.domain.ApiResponse;
import vn.ducbackend.domain.dto.*;
import vn.ducbackend.service.CauseService;

@RestController
@RequestMapping("/cause")
@RequiredArgsConstructor
@Slf4j
public class CauseController {
    private final CauseService causeService;
    @PostMapping
    ApiResponse<CauseResponse> createCauseCategory(@RequestBody CauseRequest request) {
        ApiResponse<CauseResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(causeService.createCause(request));
        return apiResponse;
    }
}

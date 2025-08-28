package vn.ducbackend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.ducbackend.domain.ApiResponse;
import vn.ducbackend.domain.IdsResponse;
import vn.ducbackend.domain.dto.*;
import vn.ducbackend.service.CauseService;

import java.util.UUID;

@RestController
@RequestMapping("/cause")
@RequiredArgsConstructor
@Slf4j
public class CauseController {
    private final CauseService causeService;

    @PostMapping
    ApiResponse<IdsResponse<Long>> createCauseCategory(@RequestBody CauseRequest request) {
        return ApiResponse.<IdsResponse<Long>>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString()) // chuỗi UUID random
                .data(IdsResponse.<Long>builder()
                        .id(causeService.create(request))
                        .build()
                )
                .build();
    }
}

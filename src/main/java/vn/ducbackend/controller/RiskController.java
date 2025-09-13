package vn.ducbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.ducbackend.domain.ApiResponse;
import vn.ducbackend.domain.IdsResponse;
import vn.ducbackend.domain.dto.RiskDTO;
import vn.ducbackend.service.RiskService;

import java.util.UUID;

@RestController
@RequestMapping("/risk")
@RequiredArgsConstructor
@Slf4j
public class RiskController {
    private final RiskService riskService;

    @PostMapping
    ApiResponse<IdsResponse<Long>> create(@RequestBody @Valid RiskDTO request) {
        return ApiResponse.<IdsResponse<Long>>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString()) // chuỗi UUID random
                .data(IdsResponse.<Long>builder()
                        .id(riskService.create(request))
                        .build()
                )
                .build();
    }
}

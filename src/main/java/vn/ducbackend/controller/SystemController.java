package vn.ducbackend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import vn.ducbackend.domain.ApiResponse;
import vn.ducbackend.domain.dto.SystemRequest;
import vn.ducbackend.domain.dto.SystemResponse;
import vn.ducbackend.service.SystemService;

import java.util.List;

@RestController
@RequestMapping("/system")
@RequiredArgsConstructor
@Slf4j
public class SystemController {
    private final SystemService systemService;

    @PostMapping
    ApiResponse<SystemResponse> createSystem(@RequestBody SystemRequest systemRequest){
        ApiResponse<SystemResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(systemService.createSystem(systemRequest));
        return apiResponse;
    }

    @GetMapping(params = "id")
    ApiResponse<SystemResponse> getSystem(@RequestParam Long id){
        ApiResponse<SystemResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(systemService.getSystem(id));
        return apiResponse;
    }

    @GetMapping
    ApiResponse<List<SystemResponse>> getAll(){
        return ApiResponse.<List<SystemResponse>>builder()
                .data(systemService.getAllSystem())
                .build();
    }

    @PutMapping()
    ApiResponse<SystemResponse> updateRole(@RequestBody SystemResponse systemResponse){
        return ApiResponse.<SystemResponse>builder()
                .data(systemService.updateSystem(systemResponse))
                .build();
    }


    @DeleteMapping()
    ApiResponse<String> deleteSystem(@RequestParam Long id){
        systemService.deleteSystem(id);
        return ApiResponse.<String>builder()
                .data("Delete System Scuccessfully")
                .build();
    }
}

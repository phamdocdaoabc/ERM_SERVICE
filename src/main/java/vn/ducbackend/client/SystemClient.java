package vn.ducbackend.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.ducbackend.domain.ApiResponse;
import vn.ducbackend.domain.PageResponse;
import vn.ducbackend.domain.dto.LinkResponse;

import java.util.List;

@FeignClient(
        name = "system-service",
        url = "${system.service.url}"
)
public interface SystemClient {
    @GetMapping("/api/v1/system")
    ApiResponse<LinkResponse> getSystemsById(@RequestParam("systemId") Long systemId);

    @GetMapping("/api/v1/system/list")
    ApiResponse<PageResponse<LinkResponse>> getAllSystems(@RequestParam("ids") List<Long> ids);
}

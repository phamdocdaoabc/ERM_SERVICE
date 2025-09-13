package vn.ducbackend.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.ducbackend.domain.ApiResponse;
import vn.ducbackend.domain.PageResponse;
import vn.ducbackend.domain.dto.BasicInfoDTO;

import java.util.List;
import java.util.Set;

@FeignClient(
        name = "system-service",
        url = "${system.service.url}"
)
public interface SystemClient {
    @GetMapping("/api/v1/system")
    ApiResponse<BasicInfoDTO> getSystemsById(@RequestParam("systemId") Long systemId);

    @GetMapping("/api/v1/system/list")
    ApiResponse<PageResponse<BasicInfoDTO>> getAllSystems(@RequestParam("ids") Set<Long> ids);
}

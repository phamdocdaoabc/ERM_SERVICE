package vn.ducbackend.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import vn.ducbackend.domain.ApiResponse;
import vn.ducbackend.domain.PageResponse;
import vn.ducbackend.domain.dto.BasicInfoDTO;

import java.util.List;

@FeignClient(
        name = "employee-service",
        url = "${employee.service.url}"
)
public interface EmployeeClient {
    @GetMapping("/api/v1/employee/list")
    ApiResponse<PageResponse<BasicInfoDTO>> getAllDepartment(@RequestParam("ids") List<Long> ids,
                                                             @RequestHeader("Authorization") String authorization,
                                                             @RequestHeader("current-domain") String domain
                                                             );
}

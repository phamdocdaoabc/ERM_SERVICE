package vn.ducbackend.service;

import vn.ducbackend.domain.dto.SystemRequest;
import vn.ducbackend.domain.dto.SystemResponse;

import java.util.List;

public interface SystemService {
    SystemResponse createSystem(SystemRequest roleRequest);

    SystemResponse getSystem(Long id);

    List<SystemResponse> getAllSystem();

    void deleteSystem(Long id);

    SystemResponse updateSystem(SystemResponse systemResponse);
}

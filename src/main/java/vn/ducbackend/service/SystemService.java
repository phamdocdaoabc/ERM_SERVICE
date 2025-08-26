package vn.ducbackend.service;

import java.util.List;

public interface SystemService {
    RoleResponse createRole(RoleRequest roleRequest);

    List<RoleResponse> getAll();

    void delete(String name);

    RoleResponse updateRole(String Id, RoleUpdateRequest roleUpdateRequest);
}

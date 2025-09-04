package vn.ducbackend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.ducbackend.domain.enums.SourceCause;
import vn.ducbackend.domain.enums.TypeCause;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskCategoryDetailResponse {
    private Long id;

    private String code;

    private String name;

    private List<LinkResponse> systemIds; // Hệ thống

    private LinkResponse parent;

    private String description;

    private Boolean isActive;
}

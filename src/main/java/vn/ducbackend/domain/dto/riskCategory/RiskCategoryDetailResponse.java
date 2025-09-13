package vn.ducbackend.domain.dto.riskCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.ducbackend.domain.dto.BasicInfoDTO;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskCategoryDetailResponse {
    private Long id;

    private String code;

    private String name;

    private List<BasicInfoDTO> systemIds; // Hệ thống

    private BasicInfoDTO parent;

    private String description;

    private Boolean isActive;
}

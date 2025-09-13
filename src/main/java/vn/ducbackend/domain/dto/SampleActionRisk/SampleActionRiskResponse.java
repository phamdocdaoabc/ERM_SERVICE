package vn.ducbackend.domain.dto.SampleActionRisk;

import lombok.*;
import vn.ducbackend.domain.dto.BasicInfoDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SampleActionRiskResponse {
    private Long id;

    private String code;

    private String name;

    private BasicInfoDTO riskType;

    private BasicInfoDTO causeCategory;

    private String note;

    private Boolean isActive;
}

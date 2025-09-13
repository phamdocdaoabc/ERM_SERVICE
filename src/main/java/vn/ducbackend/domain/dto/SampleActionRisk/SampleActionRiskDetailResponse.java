package vn.ducbackend.domain.dto.SampleActionRisk;

import lombok.*;
import vn.ducbackend.domain.dto.BasicInfoDTO;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SampleActionRiskDetailResponse {
    private Long id;

    private String code;

    private String name;

    private BasicInfoDTO riskType;

    private BasicInfoDTO causeCategory;

    private String note;

    private Boolean isActive;

    private List<SampleActionRiskMapResponse> sampleActionMaps;
}

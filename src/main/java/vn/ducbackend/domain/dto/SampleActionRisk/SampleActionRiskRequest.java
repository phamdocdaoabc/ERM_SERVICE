package vn.ducbackend.domain.dto.SampleActionRisk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SampleActionRiskRequest {
    private  Long id;
    private String code;

    private String name;

    private Long riskTypeId;

    private Long causeCategoryId;

    private String note;

    private Boolean isActive;

    private List<SampleActionRiskMapRequest> sampleActionMaps;
}

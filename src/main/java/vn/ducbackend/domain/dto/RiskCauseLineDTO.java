package vn.ducbackend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.ducbackend.domain.dto.attributeRisk.AttributeValueRiskDTO;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RiskCauseLineDTO {
    private Long id;
    private RiskCauseTrackingDTO causeTracking;
}

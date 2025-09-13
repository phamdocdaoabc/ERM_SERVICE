package vn.ducbackend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.ducbackend.domain.dto.attributeRisk.AttributeRiskResponse;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RiskAttributeLineDTO {
    private Long id;

    private AttributeRiskResponse attributeRiskResponse;

    private List<RiskAttributeLineValueDTO> riskAttributeLineValues;
}

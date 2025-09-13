package vn.ducbackend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.ducbackend.domain.dto.attributeRisk.AttributeValueRiskDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RiskAttributeLineValueDTO {
    private Long id;
    private AttributeValueRiskDTO attributeValueRisk;
    private String value;
}

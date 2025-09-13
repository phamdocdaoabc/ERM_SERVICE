package vn.ducbackend.domain.dto.RiskType;

import lombok.*;
import vn.ducbackend.domain.dto.attributeRisk.AttributeValueRiskDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskTypeAttributeValueDTO {
    private Long id;
    private AttributeValueRiskDTO attributeValueRisk;
}

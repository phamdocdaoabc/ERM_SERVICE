package vn.ducbackend.domain.dto.attributeRisk;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttributeValueRiskDTO {
    private Long id;
    private String value;
    private Integer sortOrder;
}

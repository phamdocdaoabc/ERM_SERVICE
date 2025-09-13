package vn.ducbackend.domain.dto.RiskType;

import lombok.*;
import vn.ducbackend.domain.dto.BasicInfoDTO;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskTypeAttributeDTO {
    private Long id;
    private BasicInfoDTO attribute;
    private BasicInfoDTO attributeGroup;
    private List<RiskTypeAttributeValueDTO> attributeValueRisks;
}

package vn.ducbackend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.ducbackend.domain.enums.ObjectRiskCause;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RiskCauseMappingDTO {
    private Long id;
    private ObjectRiskCause objectType;
    private BasicInfoDTO object;
}

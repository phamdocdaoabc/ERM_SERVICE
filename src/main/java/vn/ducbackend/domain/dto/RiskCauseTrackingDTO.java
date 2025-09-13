package vn.ducbackend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.ducbackend.domain.dto.causes.CauseDetailResponse;
import vn.ducbackend.domain.enums.ObjectRiskCause;
import vn.ducbackend.domain.enums.StatusRiskCause;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RiskCauseTrackingDTO {
    private Long id;
    private String code;
    private CauseDetailResponse cause;
    private BasicInfoDTO causeCategory;
    private Long repeatCount;
    private ObjectRiskCause objectType;
    private StatusRiskCause status;
    private List<RiskCauseMappingDTO> objectDetais;
    // Tiếp tục action
}

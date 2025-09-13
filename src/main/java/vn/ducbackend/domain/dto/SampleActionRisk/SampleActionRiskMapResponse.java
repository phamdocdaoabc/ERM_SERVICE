package vn.ducbackend.domain.dto.SampleActionRisk;

import lombok.*;
import vn.ducbackend.domain.dto.BasicInfoDTO;
import vn.ducbackend.domain.enums.TypeAction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SampleActionRiskMapResponse {
    private Long id;

    private BasicInfoDTO precautionId; // Biện pháp phòng ngừa

    private TypeAction type;

    private BasicInfoDTO departmentId; // Quản lý bộ phận

    private String content;
}

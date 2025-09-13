package vn.ducbackend.domain.dto.SampleActionRisk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.ducbackend.domain.enums.TypeAction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SampleActionRiskMapRequest {
    private Long precautionId; // Biện pháp phòng ngừa

    private TypeAction type;

    private Long departmentId; // Quản lý bộ phận

    private String content;
}

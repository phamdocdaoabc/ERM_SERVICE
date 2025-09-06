package vn.ducbackend.domain.dto.riskCategory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RiskCategoryRequest {
    private String code;

    private String name;

    private List<Long> systemIds;

    private Long parentId;

    private String description;

    private Boolean isActive;

}

package vn.ducbackend.domain.dto.attributeGroupRisk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.ducbackend.domain.enums.TypeAttributeGroup;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttributeGroupRiskDTO {
    private Long id;

    private String code;

    private String name;

    private TypeAttributeGroup type;

    private String description;

    private Boolean isActive;
}

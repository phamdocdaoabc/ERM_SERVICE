package vn.ducbackend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.ducbackend.domain.enums.TypeAttributeGroup;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttributeGroupRiskRequest {
    private String code;

    private String name;

    private TypeAttributeGroup type;

    private String description;

    private Boolean isActive;
}

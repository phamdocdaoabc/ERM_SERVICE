package vn.ducbackend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.ducbackend.domain.enums.DataType;
import vn.ducbackend.domain.enums.DisplayType;
import vn.ducbackend.domain.enums.TypeAttributeGroup;
import vn.ducbackend.domain.enums.TypeCause;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttributeRiskResponse {
    private Long id;

    private String code;

    private String name;

    private LinkResponse attributeGroup;

    private DisplayType displayType; // Hình thức

    private DataType dataType; // Loại dữ liệu

    private TypeAttributeGroup type;

    private String description;

    private Boolean isActive;
}

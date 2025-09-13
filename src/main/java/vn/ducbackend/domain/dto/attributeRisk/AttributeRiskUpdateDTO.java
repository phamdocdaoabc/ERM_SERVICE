package vn.ducbackend.domain.dto.attributeRisk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.ducbackend.domain.enums.DataType;
import vn.ducbackend.domain.enums.DisplayType;
import vn.ducbackend.domain.enums.TypeAttributeGroup;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttributeRiskUpdateDTO {
    private Long id;

    private String code;

    private String name;

    private Long attributeGroupId;

    private DisplayType displayType; // Hình thức

    private DataType dataType; // Loại dữ liệu

    private TypeAttributeGroup type;

    private String description;

    private Boolean isActive;

    private List<String> values; // nếu selectbox, gửi danh sách value
}

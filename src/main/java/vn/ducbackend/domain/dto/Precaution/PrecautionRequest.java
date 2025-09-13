package vn.ducbackend.domain.dto.Precaution;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrecautionRequest {
    private String code;

    private String name;

    private String description;

    private Boolean isActive;
}

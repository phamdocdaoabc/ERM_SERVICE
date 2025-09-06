package vn.ducbackend.domain.dto.causesCategory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CauseCategorySearchRequest {
    private String code;

    private String name;

    private Long systemId;

}

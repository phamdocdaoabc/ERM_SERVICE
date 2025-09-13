package vn.ducbackend.domain.dto.causes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.ducbackend.domain.enums.Source;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CauseSearchRequest {
    private String code;

    private String name;

    private Long causeCategoryId;

    private Source source;

    private Boolean isActive;
}

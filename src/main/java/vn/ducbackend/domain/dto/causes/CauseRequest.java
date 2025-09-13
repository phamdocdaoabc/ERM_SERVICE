package vn.ducbackend.domain.dto.causes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.ducbackend.domain.enums.Source;
import vn.ducbackend.domain.enums.TypeCause;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CauseRequest {
    private String code;

    private String name;

    private TypeCause type;

    private Source source;

    private Long causeCategoryId;

    private String note;

    private Boolean isActive;

    private List<Long> systemIds;
}

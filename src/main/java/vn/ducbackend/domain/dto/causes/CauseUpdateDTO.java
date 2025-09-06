package vn.ducbackend.domain.dto.causes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.ducbackend.domain.enums.SourceCause;
import vn.ducbackend.domain.enums.TypeCause;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CauseUpdateDTO {
    private Long id;

    private String code;

    private String name;

    private TypeCause type;

    private SourceCause source;

    private Long causeCategoryId;

    private String note;

    private Boolean isActive;

    private List<Long> systemIds;
}

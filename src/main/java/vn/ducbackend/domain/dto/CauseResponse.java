package vn.ducbackend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.ducbackend.domain.enums.SourceCause;
import vn.ducbackend.domain.enums.TypeCause;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CauseResponse {
    private Long id;

    private String code;

    private String name;

    private TypeCause type;

    private SourceCause source;

    private Long causeCategoryId;

    private String note;

    private boolean isActive;
}

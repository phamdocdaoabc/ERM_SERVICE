package vn.ducbackend.domain.dto;

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
public class CauseCategorySearchRequest {
    private String code;

    private String name;

    private Long systemId;

}

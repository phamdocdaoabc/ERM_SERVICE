package vn.ducbackend.domain.dto.causes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.ducbackend.domain.dto.LinkResponse;
import vn.ducbackend.domain.enums.SourceCause;
import vn.ducbackend.domain.enums.TypeCause;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CauseDetailResponse {
    private Long id;

    private String code;

    private String name;

    private List<LinkResponse> systemIds; // Hệ thống

    private TypeCause type;

    private LinkResponse causeCategory; // Phân loại nguyên nhân

    private SourceCause source;

    private String note;

    private Boolean isActive;
}

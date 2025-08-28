package vn.ducbackend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    private CauseCategoryDTO causeCategory; // Phân loại nguyên nhân

    private SourceCause source;

    private String note;

    private boolean isActive;
}

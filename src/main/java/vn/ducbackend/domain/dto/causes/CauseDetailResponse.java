package vn.ducbackend.domain.dto.causes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.ducbackend.domain.dto.BasicInfoDTO;
import vn.ducbackend.domain.enums.Source;
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

    private List<BasicInfoDTO> systemIds; // Hệ thống

    private TypeCause type;

    private BasicInfoDTO causeCategory; // Phân loại nguyên nhân

    private Source source;

    private String note;

    private Boolean isActive;
}

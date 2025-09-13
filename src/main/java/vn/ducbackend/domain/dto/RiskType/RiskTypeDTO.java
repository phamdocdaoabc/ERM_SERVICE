package vn.ducbackend.domain.dto.RiskType;

import lombok.*;
import vn.ducbackend.domain.dto.BasicInfoDTO;
import vn.ducbackend.domain.enums.PartyType;
import vn.ducbackend.domain.enums.Source;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskTypeDTO {
    private Long id;
    // Step add 1
    private String code;

    private String name;

    private List<BasicInfoDTO> systems;

    private Source source;

    private PartyType object;

    private String note;

    private Boolean isActive;

    // Step add 2
    private List<RiskTypeAttributeDTO> attributes;
}

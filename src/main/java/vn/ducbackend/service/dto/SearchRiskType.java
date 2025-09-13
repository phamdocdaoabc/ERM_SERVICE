package vn.ducbackend.service.dto;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import vn.ducbackend.domain.enums.PartyType;
import vn.ducbackend.domain.enums.Source;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchRiskType {
    private Set<Long> ids;
    private String code;
    private String name;
    private Source source;
    private PartyType object;
    private String note;
    private Boolean isActive;
}

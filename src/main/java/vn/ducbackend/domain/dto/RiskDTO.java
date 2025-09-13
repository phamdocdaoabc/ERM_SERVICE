package vn.ducbackend.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.ducbackend.domain.dto.Tag.TagResponse;
import vn.ducbackend.domain.enums.PriorityLevel;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RiskDTO {
    // Step 1
    private String code;

    private String name;

    private BasicInfoDTO system;

    private BasicInfoDTO riskType;

    private BasicInfoDTO riskCategory;

    private BasicInfoDTO recorder;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate time;

    private TagResponse tag;

    private PriorityLevel priorityLevel;

    private String description;

    private String expected;

    private List<FileResponse> attachments;

    private List<RiskAttributeLineDTO> riskAttributeLines;
    // Step 2
    private Long level;

    private Long point;

    private List<RiskCauseLineDTO> riskCauseLines;
}

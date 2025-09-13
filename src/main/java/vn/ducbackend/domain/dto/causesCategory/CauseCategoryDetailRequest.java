package vn.ducbackend.domain.dto.causesCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.ducbackend.domain.dto.BasicInfoDTO;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CauseCategoryDetailRequest {
    private Long id;
    @NotBlank(message = "Code is required")
    private String code;
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be <= 100 characters")
    private String name;
    private String description;
    private String note;
    private List<BasicInfoDTO> systemIds;
}

package vn.ducbackend.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CauseCategoryUpdateDTO {
    @NotNull(message = "id is required")
    private Long id;
    @NotBlank(message = "Code is required")
    private String code;
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be <= 100 characters")
    private String name;
    private String description;
    private String note;
    private List<Long> systemIds;
}

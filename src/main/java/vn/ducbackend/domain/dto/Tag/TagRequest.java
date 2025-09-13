package vn.ducbackend.domain.dto.Tag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagRequest {
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name must be <= 50 characters")
    private String name;

    private String color;
}

package vn.ducbackend.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemResponse {
    private Long id;
    private String name;
}

package vn.ducbackend.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import vn.ducbackend.domain.enums.TypeAction;

import java.time.LocalDateTime;

@Entity
@Table(name = "sample_action_risks_map")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SampleActionRiskMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "sample_action_id")
    private Long sampleActionId;

    @Column(name = "precaution_id")
    private Long precautionId;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private TypeAction type;

    @Column(name = "department_id") // Bộ phận xử lý
    private Long departmentId;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "update_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "created_up")
    private String createdUp;
    @Column(name = "update_up")
    private String updateUp;
}

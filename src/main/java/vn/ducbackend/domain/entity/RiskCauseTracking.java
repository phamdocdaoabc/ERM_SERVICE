package vn.ducbackend.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import vn.ducbackend.domain.enums.ObjectRiskCause;
import vn.ducbackend.domain.enums.StatusRiskCause;

import java.time.LocalDateTime;

@Entity
@Table(name = "risk_cause_trackings")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RiskCauseTracking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "cause_category_id")
    private Long causeCategoryId;

    @Column(name = "cause_id")
    private Long causeId;

    @Column(name = "repeat_count")
    private Long repeatCount;

    @Column(name = "object ")
    @Enumerated(EnumType.STRING)
    private ObjectRiskCause object;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusRiskCause status;

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

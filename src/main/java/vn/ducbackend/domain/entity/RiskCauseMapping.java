package vn.ducbackend.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "risk_cause_mapping")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RiskCauseMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "risk_cause_tracking_id")
    private Long  riskCauseTrackingId;

    @Column(name = "device_id")
    private Long deviceId;

    @Column(name = "human_id")
    private Long humanId;

    @Column(name = "partner_id")
    private Long partnerId;

    @Column(name = "supplier_id")
    private Long supplierId;

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

package vn.ducbackend.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "system_risk_categories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SystemRiskCategories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "risk_category_id")
    private Long riskCategoryId;

    @Column(name = "system_id")
    private Long systemId;

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

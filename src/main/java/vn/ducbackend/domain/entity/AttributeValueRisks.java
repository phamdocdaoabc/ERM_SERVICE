package vn.ducbackend.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import vn.ducbackend.domain.enums.TypeAttributeGroup;

import java.time.LocalDateTime;

@Entity
@Table(name = "attribute_value_risks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttributeValueRisks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "attribute_risk_id")
    private Long attributeRiskId;

    @Column(name = "value")
    private String value;

    @Column(name = "sort_order")
    private Integer sortOrder;

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

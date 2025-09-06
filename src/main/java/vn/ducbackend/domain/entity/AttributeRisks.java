package vn.ducbackend.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import vn.ducbackend.domain.enums.DataType;
import vn.ducbackend.domain.enums.DisplayType;
import vn.ducbackend.domain.enums.TypeAttributeGroup;
import vn.ducbackend.domain.enums.TypeCause;

import java.time.LocalDateTime;

@Entity
@Table(name = "attribute_risks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttributeRisks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "attribute_group_id")
    private Long attributeGroupId;

    @Column(name = "display_type")
    @Enumerated(EnumType.STRING)
    private DisplayType displayType;

    @Column(name = "data_type")
    @Enumerated(EnumType.STRING)
    private DataType dataType;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private TypeAttributeGroup type;

    @Column(name = "description")
    private String description;

    @Column(name = "is_active")
    private Boolean isActive;

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

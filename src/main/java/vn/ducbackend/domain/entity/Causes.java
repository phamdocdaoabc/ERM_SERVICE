package vn.ducbackend.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "causes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Causes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "source")
    private String source;

    @Column(name = "cause_category_id")
    private Long causeCategoryId;

    @Column(name = "note")
    private String note;

    @Column(name = "is_active")
    private boolean isActive;

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

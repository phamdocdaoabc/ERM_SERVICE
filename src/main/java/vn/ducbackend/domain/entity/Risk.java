package vn.ducbackend.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import vn.ducbackend.domain.enums.PriorityLevel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "risks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Risk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "system_id")
    private Long systemId;

    @Column(name = "risk_type_id")
    private Long riskTypeId;

    @Column(name = "risk_category_id ")
    private Long riskCategoryId ;

    @Column(name = "recorder_id")
    private Long recorderId;

    @Column(name = "tag_id")
    private Long tagId;

    @Column(name = "time")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate time;

    @Column(name = "priority_level ")
    private PriorityLevel priorityLevel;

    @Column(name = "description")
    private String description;

    @Column(name = "expected")
    private String expected;

    @Column(name = "level")
    private Long level;

    @Column(name = "point")
    private Long point;

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

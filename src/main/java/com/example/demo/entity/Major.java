package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "majors")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Major {
    @Id
    @UuidGenerator
    @Column(columnDefinition = "UNIQUEIDENTIFIER")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false, columnDefinition = "UNIQUEIDENTIFIER")
    private Department department;

    @Column(name = "major_code", nullable = false, unique = true, length = 20)
    private String majorCode;

    @Column(name = "major_name", nullable = false, length = 255)
    private String majorName;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
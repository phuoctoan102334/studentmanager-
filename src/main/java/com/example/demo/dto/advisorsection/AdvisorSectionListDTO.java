package com.example.demo.dto.advisorsection;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter @Setter
public class AdvisorSectionListDTO {
    private UUID id;
    private UUID employeeId;
    private String employeeName;
    private String employeeCode;
    private UUID studentClassId;
    private String studentClassName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private UUID createdBy;
    private LocalDateTime updatedAt;
    private UUID updatedBy;
}
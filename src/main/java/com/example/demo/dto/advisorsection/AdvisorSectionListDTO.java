package com.example.demo.dto.advisorsection;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private Boolean isActive;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;
    private UUID createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime updatedAt;
    private UUID updatedBy;
}
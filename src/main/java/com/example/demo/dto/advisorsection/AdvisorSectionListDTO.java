package com.example.demo.dto.advisorsection;

import lombok.Getter;
import lombok.Setter;
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
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
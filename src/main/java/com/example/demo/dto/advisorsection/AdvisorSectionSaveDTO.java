package com.example.demo.dto.advisorsection;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.UUID;

@Getter @Setter
public class AdvisorSectionSaveDTO {
    private UUID employeeId;
    private UUID studentClassId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isActive;
}
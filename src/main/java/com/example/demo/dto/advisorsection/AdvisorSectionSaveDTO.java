package com.example.demo.dto.advisorsection;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter @Setter
public class AdvisorSectionSaveDTO {
    private UUID employeeId;
    private UUID studentClassId;
}
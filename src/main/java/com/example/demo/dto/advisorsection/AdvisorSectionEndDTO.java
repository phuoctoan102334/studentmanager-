package com.example.demo.dto.advisorsection;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.UUID;

@Getter @Setter
public class AdvisorSectionEndDTO {
    private UUID id;
    private LocalDate endDate;
}
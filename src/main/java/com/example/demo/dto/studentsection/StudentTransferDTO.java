package com.example.demo.dto.studentsection;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter @Setter
public class StudentTransferDTO {
    private UUID studentId;
    private UUID newClassId;
    private String reason;
}
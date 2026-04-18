// dto/studentsection/StudentTransferDTO.java
package com.example.demo.dto.studentsection;

import lombok.Data;
import java.util.UUID;

@Data
public class StudentTransferDTO {
    private UUID studentId;
    private UUID newClassId;
    private String reason;
}
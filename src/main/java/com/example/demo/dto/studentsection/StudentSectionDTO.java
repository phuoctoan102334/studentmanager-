// dto/studentsection/StudentSectionDTO.java
package com.example.demo.dto.studentsection;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class StudentSectionDTO {
    private UUID id;
    private UUID studentId;
    private String studentCode;
    private String studentName;
    private UUID studentClasseId;
    private String classCode;
    private String status;
    private String note;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean isActive;
}
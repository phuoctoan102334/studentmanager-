// dto/student/StudentListDTO.java
package com.example.demo.dto.student;

import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class StudentListDTO {
    private UUID id;
    private String code;
    private String fullName;
    private LocalDate dateOfBirth;
    private String status;
    private String studentClassName;
}
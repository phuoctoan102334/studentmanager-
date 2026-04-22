// dto/student/StudentListDTO.java
package com.example.demo.dto.student;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class StudentListDTO {
    private UUID id;
    private String code;
    private String fullName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private String status;
    private String studentClassName;
}
package com.example.demo.dto.studentsection;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter @Setter
public class StudentTransferDTO {
    @NotNull(message = "Mã sinh viên không được để trống")
    private UUID studentId;
    
    @NotNull(message = "Mã lớp mới không được để trống")
    private UUID newClassId;
    
    private String reason;
}
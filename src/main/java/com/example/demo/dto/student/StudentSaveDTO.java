package com.example.demo.dto.student;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class StudentSaveDTO {
    @NotBlank(message = "Mã sinh viên không được để trống")
    private String code;

    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;

    @NotNull(message = "Ngày sinh không được để trống")
    private LocalDate dateOfBirth;

    @Pattern(regexp = "[012]", message = "Giới tính không hợp lệ (0: Nữ, 1: Nam, 2: Khác)")
    private String gender;

    private String personalIdentificationNumber;
    private LocalDate dateOfIssue;
    private String cardPlace;
    private String address;
    private String currentAddress;
    private String status;
    private LocalDateTime admissionYear;
    private UUID academicYearId;
    private UUID departmentId;
    private UUID majorId;
    private UUID trainingProgramId;
    private UUID studentClasseId;
}
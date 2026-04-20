package com.example.demo.service.impl;

import com.example.demo.dto.studentclass.*;
import com.example.demo.entity.StudentClass;
import com.example.demo.repository.*;
import com.example.demo.service.StudentClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentClassServiceImpl implements StudentClassService {

    private final StudentClassRepository classRepo;
    private final StudentClassSectionRepository sectionRepo;
    private final DepartmentRepository departmentRepo;
    private final MajorRepository majorRepo;
    private final AcademicYearRepository academicYearRepo;
    private final TrainingProgramRepository trainingProgramRepo;
    private final EmployeeRepository employeeRepo;

    @Override
    public List<StudentClassListDTO> getAll() {
        return classRepo.findByIsActiveTrueAndDeletedAtIsNull()
                .stream().map(this::toListDTO).collect(Collectors.toList());
    }

    @Override
    public StudentClassDetailDTO getById(UUID id) {
        Objects.requireNonNull(id, "ID không được để trống");
        StudentClass sc = classRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp: " + id));
        return toDetailDTO(sc);
    }

    @Override
    public StudentClassDetailDTO getByCode(String code) {
        Objects.requireNonNull(code, "Mã lớp không được để trống");
        StudentClass sc = classRepo.findByCodeAndDeletedAtIsNull(code)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp: " + code));
        return toDetailDTO(sc);
    }

    @Override
    @Transactional
    @SuppressWarnings("null")
    public StudentClassDetailDTO create(StudentClassSaveDTO dto, UUID createdBy) {
        if (classRepo.existsByCode(dto.getCode())) {
            throw new RuntimeException("Mã lớp đã tồn tại: " + dto.getCode());
        }
        StudentClass sc = new StudentClass();
        mapFromDTO(sc, dto);
        sc.setCreatedBy(createdBy);
        return toDetailDTO(classRepo.save(sc));
    }

    @Override
    @Transactional
    @SuppressWarnings("null")
    public StudentClassDetailDTO update(UUID id, StudentClassSaveDTO dto, UUID updatedBy) {
        Objects.requireNonNull(id, "ID không được để trống");
        StudentClass sc = classRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp: " + id));
        mapFromDTO(sc, dto);
        sc.setUpdatedBy(updatedBy);
        return toDetailDTO(classRepo.save(sc));
    }

    @Override
    @Transactional
    @SuppressWarnings("null")
    public void delete(UUID id, UUID deletedBy) {
        Objects.requireNonNull(id, "ID không được để trống");
        StudentClass sc = classRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp: " + id));
        
        // Kiểm tra xem có sinh viên nào đang hoạt động trong lớp không thông qua bảng section
        boolean hasActiveStudents = !sectionRepo.findByStudentClass_IdAndIsActiveTrueOrderByStudent_Code(id).isEmpty();
        if (hasActiveStudents) {
             throw new RuntimeException("Không thể xóa lớp đang có sinh viên hoạt động");
        }

        sc.setDeletedAt(LocalDateTime.now());
        sc.setDeletedBy(deletedBy);
        sc.setIsActive(false);
        classRepo.save(sc);
    }

    @Override
    @SuppressWarnings("null")
    public List<StudentClassListDTO> search(String keyword, UUID departmentId, UUID majorId) {
        return classRepo.search(keyword, departmentId, majorId)
                .stream().map(this::toListDTO).collect(Collectors.toList());
    }

    @SuppressWarnings("null")
    private void mapFromDTO(StudentClass sc, StudentClassSaveDTO dto) {
        sc.setCode(dto.getCode());
        sc.setName(dto.getName());
        sc.setIsActive(true);

        if (dto.getDepartmentId() != null) departmentRepo.findById(dto.getDepartmentId()).ifPresent(sc::setDepartment);
        if (dto.getMajorId() != null) majorRepo.findById(dto.getMajorId()).ifPresent(sc::setMajor);
        if (dto.getAcademicYearId() != null) academicYearRepo.findById(dto.getAcademicYearId()).ifPresent(sc::setAcademicYear);
        if (dto.getTrainingProgramId() != null) trainingProgramRepo.findById(dto.getTrainingProgramId()).ifPresent(sc::setTrainingProgram);
        if (dto.getEmployeeId() != null) employeeRepo.findById(dto.getEmployeeId()).ifPresent(sc::setEmployee);
    }

    private StudentClassListDTO toListDTO(StudentClass sc) {
        StudentClassListDTO d = new StudentClassListDTO();
        d.setId(sc.getId());
        d.setCode(sc.getCode());
        d.setName(sc.getName());
        if (sc.getDepartment() != null) d.setDepartmentName(sc.getDepartment().getName());
        if (sc.getEmployee() != null) d.setAdvisorName(sc.getEmployee().getFullName());
        d.setIsActive(sc.getIsActive());
        return d;
    }

    private StudentClassDetailDTO toDetailDTO(StudentClass sc) {
        StudentClassDetailDTO d = new StudentClassDetailDTO();
        d.setId(sc.getId());
        d.setCode(sc.getCode());
        d.setName(sc.getName());
        if (sc.getDepartment() != null) { d.setDepartmentId(sc.getDepartment().getId()); d.setDepartmentName(sc.getDepartment().getName()); }
        if (sc.getMajor() != null) { d.setMajorId(sc.getMajor().getId()); d.setMajorName(sc.getMajor().getMajorName()); }
        if (sc.getAcademicYear() != null) { d.setAcademicYearId(sc.getAcademicYear().getId()); d.setAcademicYear(sc.getAcademicYear().getAcademicYear()); }
        if (sc.getTrainingProgram() != null) { d.setTrainingProgramId(sc.getTrainingProgram().getId()); d.setTrainingProgramName(sc.getTrainingProgram().getProgramName()); }
        if (sc.getEmployee() != null) { d.setEmployeeId(sc.getEmployee().getId()); d.setEmployeeName(sc.getEmployee().getFullName()); }
        d.setIsActive(sc.getIsActive());
        d.setCreatedAt(sc.getCreatedAt());
        d.setCreatedBy(sc.getCreatedBy());
        return d;
    }
}
// StudentServiceImpl.java
package com.example.demo.service;

import com.example.demo.dto.student.*;
import com.example.demo.dto.studentsection.StudentTransferDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepo;
    private final StudentClassRepository classRepo;
    private final StudentClassSectionRepository sectionRepo;

    @Override
    public List<StudentListDTO> getAll() {
        return studentRepo.findByIsActiveTrueAndDeletedAtIsNull()
                .stream().map(this::toListDTO).collect(Collectors.toList());
    }

    @Override
    public StudentDetailDTO getById(UUID id) {
        Student s = studentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên: " + id));
        return toDetailDTO(s);
    }

    @Override
    public StudentDetailDTO getByCode(String code) {
        Student s = studentRepo.findByCodeAndDeletedAtIsNull(code)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên: " + code));
        return toDetailDTO(s);
    }

    @Override
    @Transactional
    public StudentDetailDTO create(StudentSaveDTO dto, UUID createdBy) {
        if (studentRepo.existsByCode(dto.getCode())) {
            throw new RuntimeException("Mã sinh viên đã tồn tại: " + dto.getCode());
        }
        Student s = new Student();
        mapFromDTO(s, dto);
        s.setCreatedBy(createdBy);

        studentRepo.save(s);

        // Tạo bản ghi section nếu có lớp
        if (s.getStudentClass() != null) {
            StudentClassSection section = StudentClassSection.builder()
                    .student(s)
                    .studentClass(s.getStudentClass())
                    .status("studying")
                    .startDate(LocalDateTime.now())
                    .isActive(true)
                    .createdBy(createdBy)
                    .build();
            sectionRepo.save(section);
        }

        return toDetailDTO(s);
    }

    @Override
    @Transactional
    public StudentDetailDTO update(UUID id, StudentSaveDTO dto, UUID updatedBy) {
        Student s = studentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên: " + id));
        mapFromDTO(s, dto);
        s.setUpdatedBy(updatedBy);
        return toDetailDTO(studentRepo.save(s));
    }

    @Override
    @Transactional
    public void delete(UUID id, UUID deletedBy) {
        Student s = studentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên: " + id));
        s.setDeletedAt(LocalDateTime.now());
        s.setDeletedBy(deletedBy);
        s.setIsActive(false);
        studentRepo.save(s);
    }

    @Override
    public List<StudentListDTO> search(String keyword, UUID departmentId, UUID majorId, String status, UUID classId) {
        return studentRepo.search(keyword, departmentId, majorId, status, classId)
                .stream().map(this::toListDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void transfer(StudentTransferDTO dto, UUID doneBy) {
        Student student = studentRepo.findById(dto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên"));

        StudentClass newClass = classRepo.findById(dto.getNewClassId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp mới"));

        // Đóng bản ghi cũ
        sectionRepo.findByStudent_IdAndIsActiveTrueAndEndDateIsNull(dto.getStudentId())
                .ifPresent(old -> {
                    old.setEndDate(LocalDateTime.now());
                    old.setStatus("completed");
                    old.setNote(dto.getReason());
                    old.setIsActive(false);
                    old.setUpdatedBy(doneBy);
                    sectionRepo.save(old);
                });

        // Tạo bản ghi mới
        StudentClassSection newSection = StudentClassSection.builder()
                .student(student)
                .studentClass(newClass)
                .status("studying")
                .note(dto.getReason())
                .startDate(LocalDateTime.now())
                .isActive(true)
                .createdBy(doneBy)
                .build();
        sectionRepo.save(newSection);

        // Cập nhật lớp hiện tại trên students
        student.setStudentClass(newClass);
        student.setUpdatedBy(doneBy);
        studentRepo.save(student);
    }

    // ---- Mapper helpers ----
    private void mapFromDTO(Student s, StudentSaveDTO dto) {
        s.setCode(dto.getCode());
        s.setFullName(dto.getFullName());
        s.setDateOfBirth(dto.getDateOfBirth());
        s.setGender(dto.getGender());
        s.setPersonalIdentificationNumber(dto.getPersonalIdentificationNumber());
        s.setDateOfIssue(dto.getDateOfIssue());
        s.setCardPlace(dto.getCardPlace());
        s.setAddress(dto.getAddress());
        s.setCurrentAddress(dto.getCurrentAddress());
        s.setStatus(dto.getStatus() != null ? dto.getStatus() : "studying");
        s.setAdmissionYear(dto.getAdmissionYear());
        s.setIsActive(true);

        if (dto.getStudentClasseId() != null) {
            classRepo.findById(dto.getStudentClasseId()).ifPresent(s::setStudentClass);
        }
    }

    private StudentListDTO toListDTO(Student s) {
        StudentListDTO d = new StudentListDTO();
        d.setId(s.getId());
        d.setCode(s.getCode());
        d.setFullName(s.getFullName());
        d.setDateOfBirth(s.getDateOfBirth());
        d.setStatus(s.getStatus());
        if (s.getStudentClass() != null) d.setStudentClassName(s.getStudentClass().getName());
        return d;
    }

    private StudentDetailDTO toDetailDTO(Student s) {
        StudentDetailDTO d = new StudentDetailDTO();
        d.setId(s.getId());
        d.setCode(s.getCode());
        d.setFullName(s.getFullName());
        d.setDateOfBirth(s.getDateOfBirth());
        d.setGender(s.getGender());
        d.setPersonalIdentificationNumber(s.getPersonalIdentificationNumber());
        d.setDateOfIssue(s.getDateOfIssue());
        d.setCardPlace(s.getCardPlace());
        d.setAddress(s.getAddress());
        d.setCurrentAddress(s.getCurrentAddress());
        d.setStatus(s.getStatus());
        d.setAdmissionYear(s.getAdmissionYear());
        d.setIsActive(s.getIsActive());
        if (s.getDepartment() != null) { d.setDepartmentId(s.getDepartment().getId()); d.setDepartmentName(s.getDepartment().getName()); }
        if (s.getMajor() != null) { d.setMajorId(s.getMajor().getId()); d.setMajorName(s.getMajor().getMajorName()); }
        if (s.getAcademicYear() != null) { d.setAcademicYearId(s.getAcademicYear().getId()); d.setAcademicYear(s.getAcademicYear().getAcademicYear()); }
        if (s.getStudentClass() != null) { d.setStudentClasseId(s.getStudentClass().getId()); d.setStudentClassName(s.getStudentClass().getName()); }
        return d;
    }
}
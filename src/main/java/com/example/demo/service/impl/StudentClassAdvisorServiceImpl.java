package com.example.demo.service.impl;

import com.example.demo.dto.advisorsection.*;
import com.example.demo.entity.AdvisorClassSection;
import com.example.demo.entity.Employee;
import com.example.demo.entity.StudentClass;
import com.example.demo.repository.AdvisorClassSectionRepository;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.StudentClassRepository;
import com.example.demo.service.AdvisorClassSectionService;
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
public class StudentClassAdvisorServiceImpl implements AdvisorClassSectionService {

    private final AdvisorClassSectionRepository advisorSectionRepo;
    private final EmployeeRepository employeeRepo;
    private final StudentClassRepository classRepo;

    @Override
    public List<AdvisorSectionListDTO> getByClass(UUID classId) {
        return advisorSectionRepo.findByStudentClass_IdOrderByStartDateDesc(classId)
                .stream().map(this::toListDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void assignAdvisor(AdvisorSectionSaveDTO dto, UUID createdBy) {
        Objects.requireNonNull(dto.getStudentClassId(), "Mã lớp không được để trống");
        Objects.requireNonNull(dto.getEmployeeId(), "Mã nhân viên không được để trống");

        // Đóng phân công cũ nếu có
        advisorSectionRepo.findByStudentClass_IdAndIsActiveTrueAndEndDateIsNull(dto.getStudentClassId())
                .ifPresent(old -> {
                    old.setEndDate(LocalDateTime.now());
                    old.setIsActive(false);
                    old.setUpdatedBy(createdBy);
                    advisorSectionRepo.save(old);
                });

        Employee emp = employeeRepo.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));
        StudentClass sc = classRepo.findById(dto.getStudentClassId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp"));

        AdvisorClassSection section = AdvisorClassSection.builder()
                .employee(emp)
                .studentClass(sc)
                .startDate(LocalDateTime.now())
                .isActive(true)
                .createdBy(createdBy)
                .build();
        
        advisorSectionRepo.save(section);
    }

    @Override
    @Transactional
    public void endAssignment(UUID sectionId, UUID updatedBy) {
        AdvisorClassSection section = advisorSectionRepo.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bản ghi phân công"));
        section.setEndDate(LocalDateTime.now());
        section.setIsActive(false);
        section.setUpdatedBy(updatedBy);
        advisorSectionRepo.save(section);
    }

    private AdvisorSectionListDTO toListDTO(AdvisorClassSection acs) {
        AdvisorSectionListDTO d = new AdvisorSectionListDTO();
        d.setId(acs.getId());
        if (acs.getEmployee() != null) {
            d.setEmployeeId(acs.getEmployee().getId());
            d.setEmployeeName(acs.getEmployee().getFullName());
            d.setEmployeeCode(acs.getEmployee().getEmployeeCode());
        }
        if (acs.getStudentClass() != null) {
            d.setStudentClassId(acs.getStudentClass().getId());
            d.setStudentClassName(acs.getStudentClass().getName());
        }
        d.setStartDate(acs.getStartDate());
        d.setEndDate(acs.getEndDate());
        return d;
    }
}
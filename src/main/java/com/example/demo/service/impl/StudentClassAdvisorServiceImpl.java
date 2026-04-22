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
    public List<AdvisorSectionListDTO> getAll() {
        return advisorSectionRepo.findAllByDeletedAtIsNull()
                .stream().map(this::toListDTO).collect(Collectors.toList());
    }

    @Override
    public List<AdvisorSectionListDTO> getByClass(UUID classId) {
        return advisorSectionRepo.findByStudentClass_IdOrderByStartDateDesc(classId)
                .stream().map(this::toListDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    @SuppressWarnings("null")
    public void assignAdvisor(AdvisorSectionSaveDTO dto, UUID createdBy) {
        Objects.requireNonNull(dto.getStudentClassId(), "Mã lớp không được để trống");
        Objects.requireNonNull(dto.getEmployeeId(), "Mã nhân viên không được để trống");

        // Kiểm tra phân công hiện tại
        var currentActive = advisorSectionRepo.findByStudentClass_IdAndIsActiveTrueAndEndDateIsNull(dto.getStudentClassId());
        
        if (currentActive.isPresent()) {
            AdvisorClassSection old = currentActive.get();
            // Nếu là cùng một nhân viên thì không làm gì cả
            if (old.getEmployee() != null && old.getEmployee().getId().equals(dto.getEmployeeId())) {
                return;
            }
            
            // Nếu là nhân viên khác, đóng phân công cũ
            old.setEndDate(LocalDateTime.now());
            old.setIsActive(false);
            old.setUpdatedBy(createdBy);
            advisorSectionRepo.save(old);
        }

        Employee emp = employeeRepo.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));
        StudentClass sc = classRepo.findById(dto.getStudentClassId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp"));

        AdvisorClassSection section = AdvisorClassSection.builder()
                .employee(emp)
                .studentClass(sc)
                .startDate(dto.getStartDate() != null ? dto.getStartDate().atStartOfDay() : LocalDateTime.now())
                .endDate(dto.getEndDate() != null ? dto.getEndDate().atStartOfDay() : null)
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .createdBy(createdBy)
                .build();
        
        advisorSectionRepo.save(section);
    }

    @Override
    @Transactional
    @SuppressWarnings("null")
    public void updateAssignment(UUID id, AdvisorSectionSaveDTO dto, UUID updatedBy) {
        AdvisorClassSection section = advisorSectionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bản ghi phân công"));

        Employee emp = employeeRepo.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));
        StudentClass sc = classRepo.findById(dto.getStudentClassId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp"));

        section.setEmployee(emp);
        section.setStudentClass(sc);
        if (dto.getStartDate() != null) {
            section.setStartDate(dto.getStartDate().atStartOfDay());
        }
        section.setEndDate(dto.getEndDate() != null ? dto.getEndDate().atStartOfDay() : null);
        section.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        section.setUpdatedBy(updatedBy);
        
        advisorSectionRepo.save(section);
    }

    @Override
    @Transactional
    @SuppressWarnings("null")
    public void endAssignment(AdvisorSectionEndDTO dto, UUID updatedBy) {
        AdvisorClassSection section = advisorSectionRepo.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bản ghi phân công"));
        
        LocalDateTime endDateTime = dto.getEndDate() != null 
                ? dto.getEndDate().atStartOfDay() 
                : LocalDateTime.now();
                
        section.setEndDate(endDateTime);
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
        d.setStartDate(acs.getStartDate() != null ? acs.getStartDate().toLocalDate() : null);
        d.setEndDate(acs.getEndDate() != null ? acs.getEndDate().toLocalDate() : null);
        d.setIsActive(acs.getIsActive());
        d.setCreatedAt(acs.getCreatedAt());
        d.setCreatedBy(acs.getCreatedBy());
        d.setUpdatedAt(acs.getUpdatedAt());
        d.setUpdatedBy(acs.getUpdatedBy());
        return d;
    }
}
package com.example.demo.service;

import com.example.demo.dto.advisorsection.AdvisorSectionSaveDTO;
import com.example.demo.entity.AdvisorClassSection;
import com.example.demo.entity.Employee;
import com.example.demo.entity.StudentClass;
import com.example.demo.repository.AdvisorClassSectionRepository;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.StudentClassRepository;
import com.example.demo.service.impl.StudentClassAdvisorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
public class AdvisorServiceTest {

    @Mock private AdvisorClassSectionRepository advisorSectionRepo;
    @Mock private EmployeeRepository employeeRepo;
    @Mock private StudentClassRepository classRepo;

    @InjectMocks
    private StudentClassAdvisorServiceImpl advisorService;

    private UUID classId;
    private UUID employeeId;
    private AdvisorSectionSaveDTO saveDTO;

    @BeforeEach
    void setUp() {
        classId = UUID.randomUUID();
        employeeId = UUID.randomUUID();
        saveDTO = new AdvisorSectionSaveDTO();
        saveDTO.setStudentClassId(classId);
        saveDTO.setEmployeeId(employeeId);
    }

    @Test
    void assignAdvisor_ShouldCloseOldAndSaveNew() {
        AdvisorClassSection oldSection = new AdvisorClassSection();
        oldSection.setIsActive(true);

        when(advisorSectionRepo.findByStudentClass_IdAndIsActiveTrueAndEndDateIsNull(classId))
                .thenReturn(Optional.of(oldSection));
        when(employeeRepo.findById(employeeId)).thenReturn(Optional.of(new Employee()));
        when(classRepo.findById(classId)).thenReturn(Optional.of(new StudentClass()));

        advisorService.assignAdvisor(saveDTO, null);

        assertThat(oldSection.getIsActive()).isFalse();
        assertThat(oldSection.getEndDate()).isNotNull();
        verify(advisorSectionRepo, times(2)).save(any(AdvisorClassSection.class));
    }

    @Test
    void assignAdvisor_ShouldNotCreateNew_WhenSameAdvisorAlreadyActive() {
        // This test detects a bug where re-assigning the same advisor 
        // creates a new history record unnecessarily.
        
        Employee currentEmp = new Employee(); currentEmp.setId(employeeId);
        AdvisorClassSection activeSection = new AdvisorClassSection();
        activeSection.setEmployee(currentEmp);
        activeSection.setIsActive(true);

        when(advisorSectionRepo.findByStudentClass_IdAndIsActiveTrueAndEndDateIsNull(classId))
                .thenReturn(Optional.of(activeSection));
        
        // If it's the same advisor, we should probably just do nothing or return.
        
        advisorService.assignAdvisor(saveDTO, null);
        
        assertThat(activeSection.getIsActive()).isTrue(); 
        verify(advisorSectionRepo, never()).save(any(AdvisorClassSection.class));
    }
}
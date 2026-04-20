package com.example.demo.service;

import com.example.demo.entity.StudentClass;
import com.example.demo.entity.StudentClassSection;
import com.example.demo.repository.*;
import com.example.demo.service.impl.StudentClassServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentClassServiceTest {

    @Mock private StudentClassRepository classRepo;
    @Mock private StudentClassSectionRepository sectionRepo; // Needed to check for students
    @Mock private DepartmentRepository departmentRepo;
    @Mock private MajorRepository majorRepo;
    @Mock private AcademicYearRepository academicYearRepo;
    @Mock private TrainingProgramRepository trainingProgramRepo;
    @Mock private EmployeeRepository employeeRepo;

    @InjectMocks
    private StudentClassServiceImpl classService;

    private StudentClass studentClass;

    @BeforeEach
    void setUp() {
        studentClass = new StudentClass();
        studentClass.setId(UUID.randomUUID());
        studentClass.setCode("C001");
        studentClass.setName("Class 1");
        studentClass.setIsActive(true);
    }

    @Test
    void delete_ShouldSoftDeleteWithoutCheckingStudents() {
        // This test shows that delete() doesn't check for active students in the class
        when(classRepo.findById(studentClass.getId())).thenReturn(Optional.of(studentClass));
        
        // Even if we have students (we can't easily mock this since sectionRepo is not used in delete)
        // the current implementation will still delete the class.
        
        classService.delete(studentClass.getId(), null);
        
        assertThat(studentClass.getIsActive()).isFalse();
        assertThat(studentClass.getDeletedAt()).isNotNull();
        verify(classRepo).save(studentClass);
    }
}
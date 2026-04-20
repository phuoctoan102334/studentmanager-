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
@SuppressWarnings("null")
public class StudentClassServiceTest {

    @Mock private StudentClassRepository classRepo;
    @Mock private StudentRepository studentRepo;
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
    void delete_ShouldThrowException_WhenStudentsExist() {
        when(classRepo.findById(studentClass.getId())).thenReturn(Optional.of(studentClass));
        
        // Giả lập có 1 section đang hoạt động trong lớp
        StudentClassSection section = new StudentClassSection();
        when(sectionRepo.findByStudentClass_IdAndIsActiveTrueOrderByStudent_Code(studentClass.getId()))
                .thenReturn(Collections.singletonList(section));
        
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> classService.delete(studentClass.getId(), null))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Không thể xóa lớp đang có sinh viên hoạt động");
        
        verify(classRepo, never()).save(any(StudentClass.class));
    }

    @Test
    void delete_ShouldSoftDelete_WhenNoActiveStudents() {
        when(classRepo.findById(studentClass.getId())).thenReturn(Optional.of(studentClass));
        when(sectionRepo.findByStudentClass_IdAndIsActiveTrueOrderByStudent_Code(studentClass.getId()))
                .thenReturn(Collections.emptyList());
        
        classService.delete(studentClass.getId(), null);
        
        assertThat(studentClass.getIsActive()).isFalse();
        assertThat(studentClass.getDeletedAt()).isNotNull();
        verify(classRepo).save(studentClass);
    }
}
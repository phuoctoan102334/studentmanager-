package com.example.demo.service;

import com.example.demo.dto.student.StudentSaveDTO;
import com.example.demo.dto.studentsection.StudentTransferDTO;
import com.example.demo.entity.Student;
import com.example.demo.entity.StudentClass;
import com.example.demo.entity.StudentClassSection;
import com.example.demo.repository.*;
import com.example.demo.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
public class StudentServiceTest {

    @Mock private StudentRepository studentRepo;
    @Mock private StudentClassRepository classRepo;
    @Mock private StudentClassSectionRepository sectionRepo;
    @Mock private DepartmentRepository departmentRepo;
    @Mock private MajorRepository majorRepo;
    @Mock private AcademicYearRepository academicYearRepo;
    @Mock private TrainingProgramRepository trainingProgramRepo;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student student;
    private StudentClass studentClass;
    private StudentSaveDTO saveDTO;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(UUID.randomUUID());
        student.setCode("S001");
        student.setFullName("John Doe");

        studentClass = new StudentClass();
        studentClass.setId(UUID.randomUUID());
        studentClass.setName("Class A");

        saveDTO = new StudentSaveDTO();
        saveDTO.setCode("S001");
        saveDTO.setFullName("John Doe");
        saveDTO.setStudentClasseId(studentClass.getId());
    }

    @Test
    void create_ShouldSaveStudentAndSection_WhenValid() {
        when(studentRepo.existsByCode("S001")).thenReturn(false);
        when(classRepo.findById(studentClass.getId())).thenReturn(Optional.of(studentClass));
        when(studentRepo.save(any(Student.class))).thenAnswer(i -> i.getArgument(0));

        studentService.create(saveDTO, null);

        verify(studentRepo).save(any(Student.class));
        verify(sectionRepo).save(any(StudentClassSection.class));
    }

    @Test
    void create_ShouldThrow_WhenCodeExists() {
        when(studentRepo.existsByCode("S001")).thenReturn(true);
        
        assertThatThrownBy(() -> studentService.create(saveDTO, null))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Mã sinh viên đã tồn tại");
        
        verify(studentRepo, never()).save(any(Student.class));
    }

    @Test
    void transfer_ShouldCloseOldSectionAndOpenNewOne() {
        UUID oldClassId = UUID.randomUUID();
        UUID newClassId = UUID.randomUUID();
        StudentClass oldClass = new StudentClass(); oldClass.setId(oldClassId);
        StudentClass newClass = new StudentClass(); newClass.setId(newClassId);

        StudentTransferDTO transferDTO = new StudentTransferDTO();
        transferDTO.setStudentId(student.getId());
        transferDTO.setNewClassId(newClassId);
        transferDTO.setReason("Moving");

        StudentClassSection oldSection = StudentClassSection.builder()
                .student(student)
                .studentClass(oldClass)
                .isActive(true)
                .build();

        when(studentRepo.findById(student.getId())).thenReturn(Optional.of(student));
        when(classRepo.findById(newClassId)).thenReturn(Optional.of(newClass));
        when(sectionRepo.findByStudent_IdAndIsActiveTrueAndEndDateIsNull(student.getId()))
                .thenReturn(List.of(oldSection));

        studentService.transfer(transferDTO, null);

        assertThat(oldSection.getIsActive()).isFalse();
        assertThat(oldSection.getEndDate()).isNotNull();
        assertThat(oldSection.getStatus()).isEqualTo("completed");

        verify(sectionRepo, times(2)).save(any(StudentClassSection.class)); // 1 for old, 1 for new
        assertThat(student.getStudentClass()).isEqualTo(newClass);
    }

    @Test
    void update_ShouldMaintainSectionHistory_WhenClassIsChanged() {
        UUID newClassId = UUID.randomUUID();
        StudentClass newClass = new StudentClass(); newClass.setId(newClassId);
        
        saveDTO.setStudentClasseId(newClassId);
        
        StudentClassSection oldSection = StudentClassSection.builder()
                .student(student)
                .isActive(true)
                .build();

        when(studentRepo.findById(student.getId())).thenReturn(Optional.of(student));
        when(classRepo.findById(newClassId)).thenReturn(Optional.of(newClass));
        when(sectionRepo.findByStudent_IdAndIsActiveTrueAndEndDateIsNull(student.getId()))
                .thenReturn(List.of(oldSection));
        when(studentRepo.save(any(Student.class))).thenAnswer(i -> i.getArgument(0));

        studentService.update(student.getId(), saveDTO, null);

        assertThat(student.getStudentClass()).isEqualTo(newClass);
        assertThat(oldSection.getIsActive()).isFalse();
        assertThat(oldSection.getEndDate()).isNotNull();
        verify(sectionRepo, times(2)).save(any(StudentClassSection.class)); // 1 for old close, 1 for new open
    }
}
// StudentClassSectionRepository.java
package com.example.demo.repository;

import com.example.demo.dto.studentsection.StudentSectionDTO;
import com.example.demo.entity.StudentClassSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StudentClassSectionRepository extends JpaRepository<StudentClassSection, UUID> {

    // Bản ghi đang active (đang học)
    List<StudentClassSection> findByStudent_IdAndIsActiveTrueAndEndDateIsNull(UUID studentId);

    List<StudentClassSection> findByStudentClass_IdAndIsActiveTrueOrderByStudent_Code(UUID classId);

    @Query("""
        SELECT new com.example.demo.dto.studentsection.StudentSectionDTO(
            scs.id, s.id, s.code, s.fullName, sc.id, sc.name, 
            COALESCE(tp.programName, '---'), 
            scs.status, scs.note, scs.startDate, scs.endDate, scs.isActive
        )
        FROM StudentClassSection scs
        JOIN scs.student s
        JOIN scs.studentClass sc
        LEFT JOIN s.trainingProgram tp
        WHERE sc.id = :classId
          AND scs.isActive = true
          AND (:status IS NULL OR scs.status = :status)
        ORDER BY s.code
    """)
    List<StudentSectionDTO> findByClassAndStatus(
        @Param("classId") UUID classId,
        @Param("status") String status
    );
}
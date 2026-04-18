// StudentClassSectionRepository.java
package com.example.demo.repository;

import com.example.demo.entity.StudentClassSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentClassSectionRepository extends JpaRepository<StudentClassSection, UUID> {

    // Bản ghi đang active (đang học)
    Optional<StudentClassSection> findByStudent_IdAndIsActiveTrueAndEndDateIsNull(UUID studentId);

    List<StudentClassSection> findByStudentClass_IdAndIsActiveTrueOrderByStudent_Code(UUID classId);

    @Query("""
        SELECT scs FROM StudentClassSection scs
        JOIN FETCH scs.student s
        JOIN FETCH scs.studentClass sc
        WHERE scs.studentClass.id = :classId
          AND scs.isActive = true
          AND (:status IS NULL OR scs.status = :status)
        ORDER BY s.code
    """)
    List<StudentClassSection> findByClassAndStatus(
        @Param("classId") UUID classId,
        @Param("status") String status
    );
}
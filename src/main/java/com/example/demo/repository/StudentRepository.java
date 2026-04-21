// StudentRepository.java
package com.example.demo.repository;

import com.example.demo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {

    Optional<Student> findByCodeAndDeletedAtIsNull(String code);

    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.studentClass WHERE s.isActive = true AND s.deletedAt IS NULL ORDER BY s.code")
    List<Student> findByIsActiveTrueAndDeletedAtIsNull();

    @Query("""
        SELECT s FROM Student s
        LEFT JOIN FETCH s.studentClass sc
        LEFT JOIN FETCH s.department d
        LEFT JOIN FETCH s.major m
        WHERE s.isActive = true
          AND s.deletedAt IS NULL
          AND (:keyword IS NULL
               OR LOWER(s.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(s.code) LIKE LOWER(CONCAT('%', :keyword, '%')))
          AND (:departmentId IS NULL OR s.department.id = :departmentId)
          AND (:majorId IS NULL OR s.major.id = :majorId)
          AND (:status IS NULL OR s.status = :status)
          AND (:classId IS NULL OR s.studentClass.id = :classId)
        ORDER BY s.code
    """)
    List<Student> search(
        @Param("keyword") String keyword,
        @Param("departmentId") UUID departmentId,
        @Param("majorId") UUID majorId,
        @Param("status") String status,
        @Param("classId") UUID classId
    );

    boolean existsByCode(String code);
}
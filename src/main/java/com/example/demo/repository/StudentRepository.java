// StudentRepository.java
package com.example.demo.repository;

import com.example.demo.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query(value = "SELECT s FROM Student s LEFT JOIN FETCH s.studentClass WHERE s.isActive = true AND s.deletedAt IS NULL ORDER BY s.code",
           countQuery = "SELECT count(s) FROM Student s WHERE s.isActive = true AND s.deletedAt IS NULL")
    List<Student> findByIsActiveTrueAndDeletedAtIsNull();

    @Query(value = "SELECT s FROM Student s LEFT JOIN FETCH s.studentClass WHERE s.isActive = true AND s.deletedAt IS NULL",
           countQuery = "SELECT count(s) FROM Student s WHERE s.isActive = true AND s.deletedAt IS NULL")
    Page<Student> findByIsActiveTrueAndDeletedAtIsNull(Pageable pageable);

    @Query(value = """
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
    """, countQuery = """
        SELECT count(s) FROM Student s
        WHERE s.isActive = true
          AND s.deletedAt IS NULL
          AND (:keyword IS NULL
               OR LOWER(s.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(s.code) LIKE LOWER(CONCAT('%', :keyword, '%')))
          AND (:departmentId IS NULL OR s.department.id = :departmentId)
          AND (:majorId IS NULL OR s.major.id = :majorId)
          AND (:status IS NULL OR s.status = :status)
          AND (:classId IS NULL OR s.studentClass.id = :classId)
    """)
    List<Student> search(
        @Param("keyword") String keyword,
        @Param("departmentId") UUID departmentId,
        @Param("majorId") UUID majorId,
        @Param("status") String status,
        @Param("classId") UUID classId
    );

    @Query(value = """
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
    """, countQuery = """
        SELECT count(s) FROM Student s
        WHERE s.isActive = true
          AND s.deletedAt IS NULL
          AND (:keyword IS NULL
               OR LOWER(s.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(s.code) LIKE LOWER(CONCAT('%', :keyword, '%')))
          AND (:departmentId IS NULL OR s.department.id = :departmentId)
          AND (:majorId IS NULL OR s.major.id = :majorId)
          AND (:status IS NULL OR s.status = :status)
          AND (:classId IS NULL OR s.studentClass.id = :classId)
    """)
    Page<Student> searchPaged(
        @Param("keyword") String keyword,
        @Param("departmentId") UUID departmentId,
        @Param("majorId") UUID majorId,
        @Param("status") String status,
        @Param("classId") UUID classId,
        Pageable pageable
    );

    boolean existsByCode(String code);
}
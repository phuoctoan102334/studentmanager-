package com.example.demo.repository;

import com.example.demo.entity.StudentClass;
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
public interface StudentClassRepository extends JpaRepository<StudentClass, UUID> {

    Optional<StudentClass> findByCodeAndDeletedAtIsNull(String code);

    @Query(value = "SELECT sc FROM StudentClass sc LEFT JOIN FETCH sc.department LEFT JOIN FETCH sc.major LEFT JOIN FETCH sc.academicYear LEFT JOIN FETCH sc.employee WHERE sc.isActive = true AND sc.deletedAt IS NULL ORDER BY sc.code",
           countQuery = "SELECT count(sc) FROM StudentClass sc WHERE sc.isActive = true AND sc.deletedAt IS NULL")
    List<StudentClass> findByIsActiveTrueAndDeletedAtIsNull();

    @Query(value = "SELECT sc FROM StudentClass sc LEFT JOIN FETCH sc.department LEFT JOIN FETCH sc.major LEFT JOIN FETCH sc.academicYear LEFT JOIN FETCH sc.employee WHERE sc.isActive = true AND sc.deletedAt IS NULL",
           countQuery = "SELECT count(sc) FROM StudentClass sc WHERE sc.isActive = true AND sc.deletedAt IS NULL")
    Page<StudentClass> findByIsActiveTrueAndDeletedAtIsNull(Pageable pageable);

    @Query(value = """
        SELECT sc FROM StudentClass sc
        LEFT JOIN FETCH sc.department d
        LEFT JOIN FETCH sc.major m
        LEFT JOIN FETCH sc.academicYear ay
        LEFT JOIN FETCH sc.employee e
        WHERE sc.isActive = true
          AND sc.deletedAt IS NULL
          AND (:keyword IS NULL
               OR LOWER(sc.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(sc.code) LIKE LOWER(CONCAT('%', :keyword, '%')))
          AND (:departmentId IS NULL OR sc.department.id = :departmentId)
          AND (:majorId IS NULL OR sc.major.id = :majorId)
        ORDER BY sc.code
    """, countQuery = """
        SELECT count(sc) FROM StudentClass sc
        WHERE sc.isActive = true
          AND sc.deletedAt IS NULL
          AND (:keyword IS NULL
               OR LOWER(sc.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(sc.code) LIKE LOWER(CONCAT('%', :keyword, '%')))
          AND (:departmentId IS NULL OR sc.department.id = :departmentId)
          AND (:majorId IS NULL OR sc.major.id = :majorId)
    """)
    List<StudentClass> search(
        @Param("keyword") String keyword,
        @Param("departmentId") UUID departmentId,
        @Param("majorId") UUID majorId
    );

    @Query(value = """
        SELECT sc FROM StudentClass sc
        LEFT JOIN FETCH sc.department d
        LEFT JOIN FETCH sc.major m
        LEFT JOIN FETCH sc.academicYear ay
        LEFT JOIN FETCH sc.employee e
        WHERE sc.isActive = true
          AND sc.deletedAt IS NULL
          AND (:keyword IS NULL
               OR LOWER(sc.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(sc.code) LIKE LOWER(CONCAT('%', :keyword, '%')))
          AND (:departmentId IS NULL OR sc.department.id = :departmentId)
          AND (:majorId IS NULL OR sc.major.id = :majorId)
        ORDER BY sc.code
    """, countQuery = """
        SELECT count(sc) FROM StudentClass sc
        WHERE sc.isActive = true
          AND sc.deletedAt IS NULL
          AND (:keyword IS NULL
               OR LOWER(sc.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(sc.code) LIKE LOWER(CONCAT('%', :keyword, '%')))
          AND (:departmentId IS NULL OR sc.department.id = :departmentId)
          AND (:majorId IS NULL OR sc.major.id = :majorId)
    """)
    Page<StudentClass> searchPaged(
        @Param("keyword") String keyword,
        @Param("departmentId") UUID departmentId,
        @Param("majorId") UUID majorId,
        Pageable pageable
    );

    boolean existsByCode(String code);
}
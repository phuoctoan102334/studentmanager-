-- ============================================================
--  NHÓM II – QUẢN LÝ SINH VIÊN
--  Nguồn: IT23M_ReviewFinal_v4.xlsx
--  SQL Server 2019+
--
--  Bảng chính (Nhóm II):
--    students, student_classes,
--    student_classe_sections, advisor_classe_sections
--
--  Bảng stub phụ thuộc (tối thiểu, không có logic):
--    users, departments, majors, training_programs,
--    academic_years, employees
--
--  Khi ghép với nhóm khác:
--    Xóa phần stub của bảng nào đã được nhóm khác tạo rồi.
--    Tất cả stub đã có đủ cột audit nên ghép trực tiếp được.
--
--  Lưu ý: cột "academic_year_year" trong Excel bị lỗi đánh máy,
--          đã chuẩn hóa thành "academic_year_id" trong file này.
-- ============================================================

USE master;
GO

IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = N'QuanLyDaoTao')
    CREATE DATABASE QuanLyDaoTao COLLATE Vietnamese_CI_AS;
GO

USE QuanLyDaoTao;
GO


-- ============================================================
--  PHẦN 1 – BẢNG STUB (phụ thuộc tối thiểu)
-- ============================================================

-- ---- users ----
IF OBJECT_ID('dbo.users', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.users (
        id            UNIQUEIDENTIFIER NOT NULL DEFAULT NEWSEQUENTIALID() CONSTRAINT PK_users PRIMARY KEY,
        username      NVARCHAR(50)     NOT NULL CONSTRAINT UQ_users_username UNIQUE,
        password_hash NVARCHAR(255)    NOT NULL,
        full_name     NVARCHAR(100)    NOT NULL,
        email         NVARCHAR(100)    NULL CONSTRAINT UQ_users_email UNIQUE,
        is_active     BIT              NOT NULL DEFAULT 1,
        created_at    DATETIME2        NOT NULL DEFAULT SYSDATETIME(),
        updated_at    DATETIME2        NULL,
        created_by    UNIQUEIDENTIFIER NULL,
        updated_by    UNIQUEIDENTIFIER NULL,
        deleted_at    DATETIME2        NULL,
        deleted_by    UNIQUEIDENTIFIER NULL
    );
    PRINT 'Created stub: users';
END
GO

-- ---- departments ----
IF OBJECT_ID('dbo.departments', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.departments (
        id         UNIQUEIDENTIFIER NOT NULL DEFAULT NEWSEQUENTIALID() CONSTRAINT PK_departments PRIMARY KEY,
        code       VARCHAR(20)      NOT NULL CONSTRAINT UQ_dept_code UNIQUE,
        name       NVARCHAR(150)    NOT NULL,
        is_active  BIT              NOT NULL DEFAULT 1,
        created_at DATETIME2        NOT NULL DEFAULT SYSDATETIME(),
        updated_at DATETIME2        NULL,
        created_by UNIQUEIDENTIFIER NULL,
        updated_by UNIQUEIDENTIFIER NULL,
        deleted_at DATETIME2        NULL,
        deleted_by UNIQUEIDENTIFIER NULL
    );
    PRINT 'Created stub: departments';
END
GO

-- ---- majors ----
IF OBJECT_ID('dbo.majors', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.majors (
        id            UNIQUEIDENTIFIER NOT NULL DEFAULT NEWSEQUENTIALID() CONSTRAINT PK_majors PRIMARY KEY,
        department_id UNIQUEIDENTIFIER NOT NULL CONSTRAINT FK_majors_dept REFERENCES dbo.departments(id),
        major_code    VARCHAR(20)      NOT NULL CONSTRAINT UQ_majors_code UNIQUE,
        major_name    NVARCHAR(255)    NOT NULL,
        is_active     BIT              NOT NULL DEFAULT 1,
        created_at    DATETIME2        NOT NULL DEFAULT SYSDATETIME(),
        updated_at    DATETIME2        NULL,
        created_by    UNIQUEIDENTIFIER NULL,
        updated_by    UNIQUEIDENTIFIER NULL,
        deleted_at    DATETIME2        NULL,
        deleted_by    UNIQUEIDENTIFIER NULL
    );
    PRINT 'Created stub: majors';
END
GO

-- ---- training_programs ----
IF OBJECT_ID('dbo.training_programs', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.training_programs (
        id           UNIQUEIDENTIFIER NOT NULL DEFAULT NEWSEQUENTIALID() CONSTRAINT PK_training_programs PRIMARY KEY,
        program_code VARCHAR(50)      NOT NULL CONSTRAINT UQ_tp_code UNIQUE,
        program_name NVARCHAR(255)    NOT NULL,
        major_id     UNIQUEIDENTIFIER NOT NULL CONSTRAINT FK_tp_major REFERENCES dbo.majors(id),
        is_active    BIT              NOT NULL DEFAULT 1,
        created_at   DATETIME2        NOT NULL DEFAULT SYSDATETIME(),
        updated_at   DATETIME2        NULL,
        created_by   UNIQUEIDENTIFIER NULL,
        updated_by   UNIQUEIDENTIFIER NULL,
        deleted_at   DATETIME2        NULL,
        deleted_by   UNIQUEIDENTIFIER NULL
    );
    PRINT 'Created stub: training_programs';
END
GO

-- ---- academic_years ----
IF OBJECT_ID('dbo.academic_years', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.academic_years (
        id            UNIQUEIDENTIFIER NOT NULL DEFAULT NEWSEQUENTIALID() CONSTRAINT PK_academic_years PRIMARY KEY,
        academic_code NVARCHAR(50)     NOT NULL CONSTRAINT UQ_ay_code UNIQUE,
        academic_name NVARCHAR(100)    NULL,
        academic_year NVARCHAR(20)     NULL,
        start_date    DATE             NULL,
        end_date      DATE             NULL,
        is_active     BIT              NOT NULL DEFAULT 1,
        created_at    DATETIME2        NOT NULL DEFAULT SYSDATETIME(),
        updated_at    DATETIME2        NULL,
        created_by    UNIQUEIDENTIFIER NULL,
        updated_by    UNIQUEIDENTIFIER NULL,
        deleted_at    DATETIME2        NULL,
        deleted_by    UNIQUEIDENTIFIER NULL
    );
    PRINT 'Created stub: academic_years';
END
GO

-- ---- employees ----
IF OBJECT_ID('dbo.employees', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.employees (
        id            UNIQUEIDENTIFIER NOT NULL DEFAULT NEWSEQUENTIALID() CONSTRAINT PK_employees PRIMARY KEY,
        user_id       UNIQUEIDENTIFIER NULL CONSTRAINT FK_emp_user REFERENCES dbo.users(id),
        employee_code VARCHAR(20)      NOT NULL CONSTRAINT UQ_emp_code UNIQUE,
        full_name     NVARCHAR(100)    NOT NULL,
        department_id UNIQUEIDENTIFIER NULL CONSTRAINT FK_emp_dept REFERENCES dbo.departments(id),
        is_active     BIT              NOT NULL DEFAULT 1,
        created_at    DATETIME2        NOT NULL DEFAULT SYSDATETIME(),
        updated_at    DATETIME2        NULL,
        created_by    UNIQUEIDENTIFIER NULL,
        updated_by    UNIQUEIDENTIFIER NULL,
        deleted_at    DATETIME2        NULL,
        deleted_by    UNIQUEIDENTIFIER NULL
    );
    PRINT 'Created stub: employees';
END
GO


-- ============================================================
--  PHẦN 2 – NHÓM II: QUẢN LÝ SINH VIÊN
-- ============================================================

-- ---- 2.1 student_classes ----
--  Tạo TRƯỚC students vì students.student_classe_id → student_classes.id
IF OBJECT_ID('dbo.student_classes', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.student_classes (
        id                  UNIQUEIDENTIFIER NOT NULL DEFAULT NEWSEQUENTIALID() CONSTRAINT PK_student_classes PRIMARY KEY,
        code                NVARCHAR(100)    NOT NULL CONSTRAINT UQ_sc_code UNIQUE,  -- VD: IT23M
        name                NVARCHAR(255)    NOT NULL,
        academic_year_id    UNIQUEIDENTIFIER NULL CONSTRAINT FK_sc_ay      REFERENCES dbo.academic_years(id),
        department_id       UNIQUEIDENTIFIER NULL CONSTRAINT FK_sc_dept    REFERENCES dbo.departments(id),
        major_id            UNIQUEIDENTIFIER NULL CONSTRAINT FK_sc_major   REFERENCES dbo.majors(id),
        training_program_id UNIQUEIDENTIFIER NULL CONSTRAINT FK_sc_tp      REFERENCES dbo.training_programs(id),
        employee_id         UNIQUEIDENTIFIER NULL CONSTRAINT FK_sc_emp     REFERENCES dbo.employees(id),  -- Cố vấn học tập
        created_at          DATETIME2        NOT NULL DEFAULT SYSDATETIME(),
        updated_at          DATETIME2        NULL,
        created_by          UNIQUEIDENTIFIER NULL,
        updated_by          UNIQUEIDENTIFIER NULL,
        deleted_at          DATETIME2        NULL,
        deleted_by          UNIQUEIDENTIFIER NULL,
        is_active           BIT              NOT NULL DEFAULT 1
    );
    PRINT 'Created: student_classes';
END
GO

-- ---- 2.2 students ----
IF OBJECT_ID('dbo.students', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.students (
        id                             UNIQUEIDENTIFIER NOT NULL DEFAULT NEWSEQUENTIALID() CONSTRAINT PK_students PRIMARY KEY,
        user_id                        UNIQUEIDENTIFIER NULL CONSTRAINT FK_stu_user  REFERENCES dbo.users(id),
        code                           VARCHAR(100)     NOT NULL CONSTRAINT UQ_stu_code UNIQUE,  -- Mã số sinh viên
        full_name                      NVARCHAR(255)    NOT NULL,
        date_of_birth                  DATE             NULL,
        gender                         NVARCHAR(10)     NULL CONSTRAINT CK_stu_gender CHECK (gender IN (N'1', N'2', N'0')),
        personal_identification_number VARCHAR(20)      NULL CONSTRAINT UQ_stu_pin   UNIQUE,     -- CMND/CCCD
        date_of_issue                  DATE             NULL,
        card_place                     NVARCHAR(100)    NULL,
        address                        NVARCHAR(300)    NULL,
        current_address                NVARCHAR(300)    NULL,
        academic_year_id               UNIQUEIDENTIFIER NULL CONSTRAINT FK_stu_ay    REFERENCES dbo.academic_years(id),
        department_id                  UNIQUEIDENTIFIER NULL CONSTRAINT FK_stu_dept  REFERENCES dbo.departments(id),
        major_id                       UNIQUEIDENTIFIER NULL CONSTRAINT FK_stu_major REFERENCES dbo.majors(id),
        training_program_id            UNIQUEIDENTIFIER NULL CONSTRAINT FK_stu_tp    REFERENCES dbo.training_programs(id),
        student_classe_id              UNIQUEIDENTIFIER NULL CONSTRAINT FK_stu_class REFERENCES dbo.student_classes(id),
        status                         VARCHAR(50)      NOT NULL DEFAULT 'studying'
                                                                CONSTRAINT CK_stu_status CHECK (status IN ('studying', 'reserved', 'dropout', 'graduated')),
        admission_year                 DATETIME2        NULL,
        created_at                     DATETIME2        NOT NULL DEFAULT SYSDATETIME(),
        updated_at                     DATETIME2        NULL,
        created_by                     UNIQUEIDENTIFIER NULL,
        updated_by                     UNIQUEIDENTIFIER NULL,
        deleted_at                     DATETIME2        NULL,
        deleted_by                     UNIQUEIDENTIFIER NULL,
        is_active                      BIT              NOT NULL DEFAULT 1
    );
    PRINT 'Created: students';
END
GO

-- ---- 2.3 student_classe_sections ----
--  Lịch sử SV trong từng lớp hành chính (hỗ trợ chuyển lớp)
IF OBJECT_ID('dbo.student_classe_sections', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.student_classe_sections (
        id                UNIQUEIDENTIFIER NOT NULL DEFAULT NEWSEQUENTIALID() CONSTRAINT PK_scs PRIMARY KEY,
        student_id        UNIQUEIDENTIFIER NOT NULL CONSTRAINT FK_scs_student REFERENCES dbo.students(id),
        student_classe_id UNIQUEIDENTIFIER NOT NULL CONSTRAINT FK_scs_class   REFERENCES dbo.student_classes(id),
        status            VARCHAR(50)      NOT NULL DEFAULT 'studying'
                                                    CONSTRAINT CK_scs_status CHECK (status IN ('studying', 'completed', 'dropped')),
        note              NVARCHAR(255)    NULL,
        start_date        DATETIME2        NULL,
        end_date          DATETIME2        NULL,  -- NULL = đang học
        created_at        DATETIME2        NOT NULL DEFAULT SYSDATETIME(),
        updated_at        DATETIME2        NULL,
        created_by        UNIQUEIDENTIFIER NULL,
        updated_by        UNIQUEIDENTIFIER NULL,
        deleted_at        DATETIME2        NULL,
        deleted_by        UNIQUEIDENTIFIER NULL,
        is_active         BIT              NOT NULL DEFAULT 1
    );
    PRINT 'Created: student_classe_sections';
END
GO

-- ---- 2.4 advisor_classe_sections ----
--  Cố vấn học tập (CVHT) phụ trách lớp hành chính
IF OBJECT_ID('dbo.advisor_classe_sections', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.advisor_classe_sections (
        id                UNIQUEIDENTIFIER NOT NULL DEFAULT NEWSEQUENTIALID() CONSTRAINT PK_acs PRIMARY KEY,
        employee_id       UNIQUEIDENTIFIER NOT NULL CONSTRAINT FK_acs_emp   REFERENCES dbo.employees(id),
        student_classe_id UNIQUEIDENTIFIER NOT NULL CONSTRAINT FK_acs_class REFERENCES dbo.student_classes(id),
        start_date        DATETIME2        NULL,
        end_date          DATETIME2        NULL,  -- NULL = đang phụ trách
        created_at        DATETIME2        NOT NULL DEFAULT SYSDATETIME(),
        updated_at        DATETIME2        NULL,
        created_by        UNIQUEIDENTIFIER NULL,
        updated_by        UNIQUEIDENTIFIER NULL,
        deleted_at        DATETIME2        NULL,
        deleted_by        UNIQUEIDENTIFIER NULL,
        is_active         BIT              NOT NULL DEFAULT 1
    );
    PRINT 'Created: advisor_classe_sections';
END
GO


-- ============================================================
--  PHẦN 3 – INDEX
-- ============================================================

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE object_id = OBJECT_ID('dbo.students') AND name = 'IX_stu_code')
    CREATE INDEX IX_stu_code     ON dbo.students(code);
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE object_id = OBJECT_ID('dbo.students') AND name = 'IX_stu_user')
    CREATE INDEX IX_stu_user     ON dbo.students(user_id);
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE object_id = OBJECT_ID('dbo.students') AND name = 'IX_stu_class')
    CREATE INDEX IX_stu_class    ON dbo.students(student_classe_id);
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE object_id = OBJECT_ID('dbo.students') AND name = 'IX_stu_dept')
    CREATE INDEX IX_stu_dept     ON dbo.students(department_id);
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE object_id = OBJECT_ID('dbo.students') AND name = 'IX_stu_status')
    CREATE INDEX IX_stu_status   ON dbo.students(status) INCLUDE (full_name, code);
GO

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE object_id = OBJECT_ID('dbo.student_classes') AND name = 'IX_sc_ay')
    CREATE INDEX IX_sc_ay        ON dbo.student_classes(academic_year_id);
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE object_id = OBJECT_ID('dbo.student_classes') AND name = 'IX_sc_dept')
    CREATE INDEX IX_sc_dept      ON dbo.student_classes(department_id);
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE object_id = OBJECT_ID('dbo.student_classes') AND name = 'IX_sc_emp')
    CREATE INDEX IX_sc_emp       ON dbo.student_classes(employee_id);
GO

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE object_id = OBJECT_ID('dbo.student_classe_sections') AND name = 'IX_scs_student')
    CREATE INDEX IX_scs_student  ON dbo.student_classe_sections(student_id);
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE object_id = OBJECT_ID('dbo.student_classe_sections') AND name = 'IX_scs_class')
    CREATE INDEX IX_scs_class    ON dbo.student_classe_sections(student_classe_id);
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE object_id = OBJECT_ID('dbo.student_classe_sections') AND name = 'IX_scs_status')
    CREATE INDEX IX_scs_status   ON dbo.student_classe_sections(status, is_active);
GO

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE object_id = OBJECT_ID('dbo.advisor_classe_sections') AND name = 'IX_acs_emp')
    CREATE INDEX IX_acs_emp      ON dbo.advisor_classe_sections(employee_id);
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE object_id = OBJECT_ID('dbo.advisor_classe_sections') AND name = 'IX_acs_class')
    CREATE INDEX IX_acs_class    ON dbo.advisor_classe_sections(student_classe_id);
GO

PRINT 'All indexes created.';
GO


-- ============================================================
--  PHẦN 4 – STORED PROCEDURES
-- ============================================================

-- ---- 4.1 Lấy sinh viên theo lớp ----
IF OBJECT_ID('dbo.sp_GetStudentsByClass', 'P') IS NOT NULL
    DROP PROCEDURE dbo.sp_GetStudentsByClass;
GO
CREATE PROCEDURE dbo.sp_GetStudentsByClass
    @class_id UNIQUEIDENTIFIER,
    @status   VARCHAR(50) = NULL
AS
BEGIN
    SET NOCOUNT ON;
    SELECT
        s.code          AS student_code,
        s.full_name,
        s.date_of_birth,
        s.gender,
        s.status        AS student_status,
        scs.status      AS section_status,
        scs.start_date,
        scs.end_date,
        sc.code         AS class_code,
        sc.name         AS class_name
    FROM       dbo.student_classe_sections scs
    INNER JOIN dbo.students                s  ON s.id  = scs.student_id
    INNER JOIN dbo.student_classes         sc ON sc.id = scs.student_classe_id
    WHERE scs.student_classe_id = @class_id
      AND scs.is_active         = 1
      AND s.is_active           = 1
      AND (@status IS NULL OR scs.status = @status)
    ORDER BY s.code;
END
GO

-- ---- 4.2 Chuyển lớp sinh viên ----
IF OBJECT_ID('dbo.sp_TransferStudent', 'P') IS NOT NULL
    DROP PROCEDURE dbo.sp_TransferStudent;
GO
CREATE PROCEDURE dbo.sp_TransferStudent
    @student_id   UNIQUEIDENTIFIER,
    @new_class_id UNIQUEIDENTIFIER,
    @reason       NVARCHAR(255)    = NULL,
    @done_by      UNIQUEIDENTIFIER = NULL
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRANSACTION;
    BEGIN TRY
        -- Đóng lớp cũ
        UPDATE dbo.student_classe_sections
        SET    end_date   = SYSDATETIME(),
               status     = 'completed',
               note       = ISNULL(@reason, note),
               updated_at = SYSDATETIME(),
               updated_by = @done_by,
               is_active  = 0
        WHERE  student_id = @student_id
          AND  is_active  = 1
          AND  end_date   IS NULL;

        -- Ghi vào lớp mới
        INSERT INTO dbo.student_classe_sections
               (id, student_id, student_classe_id, status, note, start_date, is_active, created_by)
        VALUES (NEWID(), @student_id, @new_class_id, 'studying', @reason, SYSDATETIME(), 1, @done_by);

        -- Cập nhật lớp hiện tại trên students
        UPDATE dbo.students
        SET    student_classe_id = @new_class_id,
               updated_at        = SYSDATETIME(),
               updated_by        = @done_by
        WHERE  id = @student_id;

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        ROLLBACK TRANSACTION;
        THROW;
    END CATCH
END
GO

-- ---- 4.3 Tìm kiếm sinh viên ----
IF OBJECT_ID('dbo.sp_SearchStudents', 'P') IS NOT NULL
    DROP PROCEDURE dbo.sp_SearchStudents;
GO
CREATE PROCEDURE dbo.sp_SearchStudents
    @keyword       NVARCHAR(100)    = NULL,
    @department_id UNIQUEIDENTIFIER = NULL,
    @major_id      UNIQUEIDENTIFIER = NULL,
    @status        VARCHAR(50)      = NULL,
    @class_id      UNIQUEIDENTIFIER = NULL
AS
BEGIN
    SET NOCOUNT ON;
    SELECT
        s.id,
        s.code          AS student_code,
        s.full_name,
        s.date_of_birth,
        s.gender,
        s.status,
        sc.code         AS class_code,
        sc.name         AS class_name,
        d.name          AS department_name,
        m.major_name
    FROM       dbo.students        s
    LEFT JOIN  dbo.student_classes sc ON sc.id = s.student_classe_id
    LEFT JOIN  dbo.departments     d  ON d.id  = s.department_id
    LEFT JOIN  dbo.majors          m  ON m.id  = s.major_id
    WHERE s.is_active  = 1
      AND s.deleted_at IS NULL
      AND (@keyword       IS NULL OR s.full_name LIKE N'%' + @keyword + '%'
                                  OR s.code      LIKE   '%' + @keyword + '%')
      AND (@department_id IS NULL OR s.department_id     = @department_id)
      AND (@major_id      IS NULL OR s.major_id          = @major_id)
      AND (@status        IS NULL OR s.status            = @status)
      AND (@class_id      IS NULL OR s.student_classe_id = @class_id)
    ORDER BY s.code;
END
GO


-- ============================================================
--  PHẦN 5 – VIEWS
-- ============================================================

-- ---- 5.1 Sinh viên đang học kèm thông tin đầy đủ ----
IF OBJECT_ID('dbo.vw_ActiveStudents', 'V') IS NOT NULL
    DROP VIEW dbo.vw_ActiveStudents;
GO
CREATE VIEW dbo.vw_ActiveStudents AS
SELECT
    s.id,
    s.code          AS student_code,
    s.full_name,
    s.date_of_birth,
    s.gender,
    s.status,
    s.admission_year,
    u.username,
    u.email,
    sc.code         AS class_code,
    sc.name         AS class_name,
    d.name          AS department_name,
    m.major_name,
    ay.academic_year
FROM       dbo.students          s
LEFT JOIN  dbo.users             u  ON u.id  = s.user_id
LEFT JOIN  dbo.student_classes   sc ON sc.id = s.student_classe_id
LEFT JOIN  dbo.departments       d  ON d.id  = s.department_id
LEFT JOIN  dbo.majors            m  ON m.id  = s.major_id
LEFT JOIN  dbo.academic_years    ay ON ay.id = s.academic_year_id
WHERE s.is_active  = 1
  AND s.deleted_at IS NULL;
GO

-- ---- 5.2 Tổng hợp lớp hành chính ----
IF OBJECT_ID('dbo.vw_ClassSummary', 'V') IS NOT NULL
    DROP VIEW dbo.vw_ClassSummary;
GO
CREATE VIEW dbo.vw_ClassSummary AS
SELECT
    sc.id,
    sc.code                                                  AS class_code,
    sc.name                                                  AS class_name,
    d.name                                                   AS department_name,
    m.major_name,
    ay.academic_year,
    e.full_name                                              AS advisor_name,
    COUNT(s.id)                                              AS total_students,
    SUM(CASE WHEN s.status = 'studying'  THEN 1 ELSE 0 END) AS studying_count,
    SUM(CASE WHEN s.status = 'graduated' THEN 1 ELSE 0 END) AS graduated_count,
    SUM(CASE WHEN s.status = 'dropout'   THEN 1 ELSE 0 END) AS dropout_count,
    SUM(CASE WHEN s.status = 'reserved'  THEN 1 ELSE 0 END) AS reserved_count
FROM       dbo.student_classes  sc
LEFT JOIN  dbo.departments      d  ON d.id  = sc.department_id
LEFT JOIN  dbo.majors           m  ON m.id  = sc.major_id
LEFT JOIN  dbo.academic_years   ay ON ay.id = sc.academic_year_id
LEFT JOIN  dbo.employees        e  ON e.id  = sc.employee_id
LEFT JOIN  dbo.students         s  ON s.student_classe_id = sc.id AND s.is_active = 1
WHERE sc.is_active = 1
GROUP BY
    sc.id, sc.code, sc.name,
    d.name, m.major_name, ay.academic_year, e.full_name;
GO


-- ============================================================
PRINT N'';
PRINT N'===== Nhom II tao thanh cong! =====';
PRINT N'  Stub   : users, departments, majors, training_programs, academic_years, employees';
PRINT N'  Nhom II: student_classes, students, student_classe_sections, advisor_classe_sections';
PRINT N'';
PRINT N'  Luu y: "academic_year_year" trong Excel da duoc sua thanh "academic_year_id"';
GO

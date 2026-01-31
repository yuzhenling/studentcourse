-- 学生选课系统 数据库初始化脚本
-- PostgreSQL 16+

-- 用户账号表（登录与角色）
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,  -- STUDENT, TEACHER, ADMIN
    ref_id BIGINT,              -- 关联 students.id 或 teachers.id，管理员为空
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 班级表
CREATE TABLE IF NOT EXISTS classes (
    id BIGSERIAL PRIMARY KEY,
    class_name VARCHAR(50) NOT NULL,
    major VARCHAR(100),
    enrollment_year INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 学生表（班级字段改为班级表 id，密码用于学生登录）
CREATE TABLE IF NOT EXISTS students (
    id BIGSERIAL PRIMARY KEY,
    student_id VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(50) NOT NULL,
    gender VARCHAR(10),
    birth_date DATE,
    email VARCHAR(100),
    phone VARCHAR(20),
    major VARCHAR(100),
    enrollment_year INTEGER,
    class_id BIGINT,
    password_hash VARCHAR(100),
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 教师表（密码用于教师登录）
CREATE TABLE IF NOT EXISTS teachers (
    id BIGSERIAL PRIMARY KEY,
    teacher_id VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(50) NOT NULL,
    title VARCHAR(50),
    department VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(20),
    office VARCHAR(100),
    research_field TEXT,
    password_hash VARCHAR(100),
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 学期表
CREATE TABLE IF NOT EXISTS semesters (
    id BIGSERIAL PRIMARY KEY,
    semester VARCHAR(20) UNIQUE NOT NULL,  -- 2024-2025-1
    name VARCHAR(100),
    start_date DATE,
    end_date DATE,
    selection_start TIMESTAMP,
    selection_end TIMESTAMP,
    is_current BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 课程表
CREATE TABLE IF NOT EXISTS courses (
    id BIGSERIAL PRIMARY KEY,
    course_code VARCHAR(20) UNIQUE NOT NULL,
    course_name VARCHAR(200) NOT NULL,
    credits NUMERIC(3,1) NOT NULL,
    course_hours INTEGER,
    course_type VARCHAR(50),
    description TEXT,
    max_capacity INTEGER DEFAULT 100,
    current_enrollment INTEGER DEFAULT 0,
    semester VARCHAR(20),
    year INTEGER,
    teacher_id BIGINT,
    schedule_info TEXT,
    status VARCHAR(20) DEFAULT 'OPEN',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 课程时间表
CREATE TABLE IF NOT EXISTS course_schedules (
    id BIGSERIAL PRIMARY KEY,
    course_id BIGINT,
    day_of_week INTEGER,
    start_time TIME,
    end_time TIME,
    classroom VARCHAR(100),
    UNIQUE(course_id, day_of_week, start_time)
);

-- 选课记录表
CREATE TABLE IF NOT EXISTS course_selections (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT,
    course_id BIGINT,
    semester VARCHAR(20) NOT NULL,
    selection_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    selection_type VARCHAR(20) DEFAULT 'NORMAL',
    status VARCHAR(20) DEFAULT 'SELECTED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(student_id, course_id, semester)
);

-- 先修课程表
CREATE TABLE IF NOT EXISTS prerequisites (
    id BIGSERIAL PRIMARY KEY,
    course_id BIGINT,
    prerequisite_id BIGINT,
    is_mandatory BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 成绩表
CREATE TABLE IF NOT EXISTS grades (
    id BIGSERIAL PRIMARY KEY,
    selection_id BIGINT,
    regular_score NUMERIC(5,2),
    midterm_score NUMERIC(5,2),
    final_score NUMERIC(5,2),
    total_score NUMERIC(5,2),
    grade_point NUMERIC(3,2),
    letter_grade VARCHAR(5),
    is_passed BOOLEAN,
    teacher_comment TEXT,
    published BOOLEAN DEFAULT FALSE,
    published_date TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 索引
CREATE INDEX IF NOT EXISTS idx_sys_user_username ON sys_user(username);
CREATE INDEX IF NOT EXISTS idx_student_id ON students(student_id);
CREATE INDEX IF NOT EXISTS idx_students_status ON students(status);
CREATE INDEX IF NOT EXISTS idx_students_class_id ON students(class_id);
CREATE INDEX IF NOT EXISTS idx_teacher_id ON teachers(teacher_id);
CREATE INDEX IF NOT EXISTS idx_course_semester ON courses(semester, status);
CREATE INDEX IF NOT EXISTS idx_course_teacher ON courses(teacher_id);
CREATE INDEX IF NOT EXISTS idx_selection_student ON course_selections(student_id, semester);
CREATE INDEX IF NOT EXISTS idx_selection_course ON course_selections(course_id);
CREATE INDEX IF NOT EXISTS idx_grade_selection ON grades(selection_id);

-- 初始管理员由应用启动时 DataSeeder 创建（用户名 admin，密码 admin123）

-- 初始班级（示例）
INSERT INTO classes (class_name, major, enrollment_year) VALUES
    ('计算机2025-1班', '计算机科学与技术', 2025),
    ('计算机2025-2班', '计算机科学与技术', 2025),
    ('工商管理2025-1班', '工商管理', 2025);
    ('工商管理2025-2班', '工商管理', 2025);
    ('工商管理2025-3班', '工商管理', 2025);

-- 初始学期
INSERT INTO semesters (semester, name, is_current) VALUES ('2024-2025-1', '2024-2025学年第一学期', TRUE)
ON CONFLICT (semester) DO NOTHING;
INSERT INTO semesters (semester, name, is_current) VALUES ('2024-2025-2', '2024-2025学年第二学期', TRUE)
ON CONFLICT (semester) DO NOTHING;
INSERT INTO semesters (semester, name, is_current) VALUES ('2025-2026-1', '2025-2026学年第一学期', TRUE)
ON CONFLICT (semester) DO NOTHING;
INSERT INTO semesters (semester, name, is_current) VALUES ('2025-2026-2', '2025-2026学年第二学期', TRUE)
ON CONFLICT (semester) DO NOTHING;
-- 迁移脚本：已有库时执行（新增班级表、学生表改为 class_id）
-- 若从未执行过 init.sql，请直接使用 init.sql，不要执行本文件。

-- 1. 创建班级表（若不存在）
CREATE TABLE IF NOT EXISTS classes (
    id BIGSERIAL PRIMARY KEY,
    class_name VARCHAR(50) NOT NULL,
    major VARCHAR(100),
    enrollment_year INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. 学生表增加 class_id（若不存在该列）
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_name = 'students' AND column_name = 'class_id'
  ) THEN
    ALTER TABLE students ADD COLUMN class_id BIGINT;
  END IF;
END $$;

-- 3. 可选：迁移旧 class_name 到 classes 并设置 class_id 后，再删除 class_name
-- 先插入班级再更新 students.class_id，最后执行：
-- ALTER TABLE students DROP COLUMN IF EXISTS class_name;

-- 4. 学生表、教师表增加密码列（用于学生/教师登录，默认密码 123456 需在应用层 BCrypt 写入）
DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'students' AND column_name = 'password_hash') THEN
    ALTER TABLE students ADD COLUMN password_hash VARCHAR(100);
  END IF;
  IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'teachers' AND column_name = 'password_hash') THEN
    ALTER TABLE teachers ADD COLUMN password_hash VARCHAR(100);
  END IF;
END $$;

-- 继续补充班级数据
INSERT INTO classes (class_name, major, enrollment_year) VALUES
-- 计算机科学与技术专业 (20个班)
('计算机2021-1班', '计算机科学与技术', 2021),
('计算机2021-2班', '计算机科学与技术', 2021),
('计算机2022-1班', '计算机科学与技术', 2022),
('计算机2022-2班', '计算机科学与技术', 2022),
('计算机2023-1班', '计算机科学与技术', 2023),
('计算机2023-2班', '计算机科学与技术', 2023),
('计算机2023-3班', '计算机科学与技术', 2023),
('计算机2024-1班', '计算机科学与技术', 2024),
('计算机2024-2班', '计算机科学与技术', 2024),
('计算机2025-3班', '计算机科学与技术', 2025),
('计算机2025-4班', '计算机科学与技术', 2025),
('计算机2026-1班', '计算机科学与技术', 2026),
('计算机2026-2班', '计算机科学与技术', 2026),

-- 软件工程专业 (15个班)
('软件2021-1班', '软件工程', 2021),
('软件2021-2班', '软件工程', 2021),
('软件2022-1班', '软件工程', 2022),
('软件2022-2班', '软件工程', 2022),
('软件2023-1班', '软件工程', 2023),
('软件2023-2班', '软件工程', 2023),
('软件2024-1班', '软件工程', 2024),
('软件2024-2班', '软件工程', 2024),
('软件2025-1班', '软件工程', 2025),
('软件2025-2班', '软件工程', 2025),

-- 电子信息工程专业 (15个班)
('电子2021-1班', '电子信息工程', 2021),
('电子2021-2班', '电子信息工程', 2021),
('电子2022-1班', '电子信息工程', 2022),
('电子2022-2班', '电子信息工程', 2022),
('电子2023-1班', '电子信息工程', 2023),
('电子2023-2班', '电子信息工程', 2023),
('电子2024-1班', '电子信息工程', 2024),
('电子2024-2班', '电子信息工程', 2024),
('电子2025-1班', '电子信息工程', 2025),
('电子2025-2班', '电子信息工程', 2025),

-- 工商管理专业 (已有5个班，再补充10个班)
('工商管理2021-1班', '工商管理', 2021),
('工商管理2021-2班', '工商管理', 2021),
('工商管理2022-1班', '工商管理', 2022),
('工商管理2022-2班', '工商管理', 2022),
('工商管理2023-1班', '工商管理', 2023),
('工商管理2023-2班', '工商管理', 2023),
('工商管理2024-1班', '工商管理', 2024),
('工商管理2024-2班', '工商管理', 2024),

-- 金融学专业 (10个班)
('金融2021-1班', '金融学', 2021),
('金融2021-2班', '金融学', 2021),
('金融2022-1班', '金融学', 2022),
('金融2022-2班', '金融学', 2022),
('金融2023-1班', '金融学', 2023),
('金融2023-2班', '金融学', 2023),
('金融2024-1班', '金融学', 2024),
('金融2024-2班', '金融学', 2024),

-- 机械工程专业 (10个班)
('机械2021-1班', '机械工程', 2021),
('机械2021-2班', '机械工程', 2021),
('机械2022-1班', '机械工程', 2022),
('机械2022-2班', '机械工程', 2022),
('机械2023-1班', '机械工程', 2023),
('机械2023-2班', '机械工程', 2023),
('机械2024-1班', '机械工程', 2024),
('机械2024-2班', '机械工程', 2024),

-- 外国语学院 (10个班)
('英语2021-1班', '英语', 2021),
('英语2021-2班', '英语', 2021),
('英语2022-1班', '英语', 2022),
('英语2022-2班', '英语', 2022),
('英语2023-1班', '英语', 2023),
('英语2023-2班', '英语', 2023),
('英语2024-1班', '英语', 2024),
('英语2024-2班', '英语', 2024),

-- 数学与应用数学 (10个班)
('数学2021-1班', '数学与应用数学', 2021),
('数学2021-2班', '数学与应用数学', 2021),
('数学2022-1班', '数学与应用数学', 2022),
('数学2022-2班', '数学与应用数学', 2022),
('数学2023-1班', '数学与应用数学', 2023),
('数学2023-2班', '数学与应用数学', 2023),
('数学2024-1班', '数学与应用数学', 2024),
('数学2024-2班', '数学与应用数学', 2024),

-- 物理学 (5个班)
('物理2021-1班', '物理学', 2021),
('物理2022-1班', '物理学', 2022),
('物理2023-1班', '物理学', 2023),
('物理2024-1班', '物理学', 2024),
('物理2025-1班', '物理学', 2025);


-- 生成100个学生数据
DO $$ 
DECLARE
    class_ids INT[];
    i INT;
    student_id_prefix VARCHAR;
    class_idx INT;
    major_name VARCHAR;
    enrollment_year INT;
    class_id_val INT;
BEGIN
    -- 获取所有班级ID
    SELECT array_agg(id) INTO class_ids FROM classes;
    
    -- 生成100个学生
    FOR i IN 1..100 LOOP
        -- 随机选择一个班级
        class_idx := (random() * (array_length(class_ids, 1) - 1))::INT + 1;
        class_id_val := class_ids[class_idx];
        
        -- 根据班级ID获取专业和入学年份
        SELECT major, enrollment_year INTO major_name, enrollment_year 
        FROM classes WHERE id = class_id_val;
        
        -- 生成学号（年级+专业代码+序号）
        student_id_prefix := enrollment_year || 
            CASE major_name
                WHEN '计算机科学与技术' THEN '0101'
                WHEN '软件工程' THEN '0102'
                WHEN '电子信息工程' THEN '0201'
                WHEN '工商管理' THEN '0301'
                WHEN '金融学' THEN '0302'
                WHEN '机械工程' THEN '0401'
                WHEN '英语' THEN '0501'
                WHEN '数学与应用数学' THEN '0601'
                WHEN '物理学' THEN '0602'
                ELSE '0000'
            END;
        
        INSERT INTO students (
            student_id, name, gender, birth_date, email, 
            phone, major, enrollment_year, class_id, password_hash, status
        ) VALUES (
            student_id_prefix || LPAD(i::TEXT, 4, '0'),  -- 学号
            CASE floor(random() * 5)
                WHEN 0 THEN '张' || (ARRAY['伟','强','勇','军','磊','涛','超','杰','鹏','飞'])[floor(random() * 10) + 1]
                WHEN 1 THEN '王' || (ARRAY['芳','秀','丽','敏','静','燕','娜','婷','丹','雪'])[floor(random() * 10) + 1]
                WHEN 2 THEN '李' || (ARRAY['明','华','勇','伟','强','军','涛','杰','峰','超'])[floor(random() * 10) + 1]
                WHEN 3 THEN '刘' || (ARRAY['洋','静','敏','佳','慧','欣','悦','娜','婷','雨'])[floor(random() * 10) + 1]
                ELSE '陈' || (ARRAY['旭','晨','浩','鑫','博','宇','阳','帆','瑞','昊'])[floor(random() * 10) + 1]
            END,  -- 姓名
            CASE floor(random() * 2) WHEN 0 THEN '男' ELSE '女' END,  -- 性别
            DATE '1998-01-01' + (floor(random() * 1460))::INT,  -- 出生日期 (1998-2002)
            'student' || i || '@university.edu.cn',  -- 邮箱
            '138' || LPAD((floor(random() * 100000000))::TEXT, 8, '0'),  -- 手机号
            major_name,  -- 专业
            enrollment_year,  -- 入学年份
            class_id_val,  -- 班级ID
            '$2a$10$abcdefghijklmnopqrstuv',  -- 密码哈希 (示例)
            CASE floor(random() * 20) WHEN 0 THEN 'INACTIVE' ELSE 'ACTIVE' END  -- 状态
        );
    END LOOP;
END $$;


-- 生成100个教师数据
DO $$ 
DECLARE
    departments VARCHAR[] := ARRAY[
        '计算机科学与技术学院', '软件学院', '电子信息工程学院', 
        '经济管理学院', '机械工程学院', '外国语学院', 
        '理学院', '文法学院', '艺术设计学院', '体育部'
    ];
    titles VARCHAR[] := ARRAY['讲师', '副教授', '教授', '助理教授', '特聘教授'];
    i INT;
BEGIN
    FOR i IN 1..100 LOOP
        INSERT INTO teachers (
            teacher_id, name, title, department, email, 
            phone, office, research_field, password_hash, status
        ) VALUES (
            'T' || LPAD(i::TEXT, 4, '0'),  -- 教师ID
            CASE floor(random() * 10)
                WHEN 0 THEN '王' || (ARRAY['建国','建军','志强','伟','明','涛','强','军','华','勇'])[floor(random() * 10) + 1]
                WHEN 1 THEN '李' || (ARRAY['建国','建军','志强','伟','明','涛','强','军','华','勇'])[floor(random() * 10) + 1]
                WHEN 2 THEN '张' || (ARRAY['建国','建军','志强','伟','明','涛','强','军','华','勇'])[floor(random() * 10) + 1]
                WHEN 3 THEN '刘' || (ARRAY['建国','建军','志强','伟','明','涛','强','军','华','勇'])[floor(random() * 10) + 1]
                WHEN 4 THEN '陈' || (ARRAY['建国','建军','志强','伟','明','涛','强','军','华','勇'])[floor(random() * 10) + 1]
                WHEN 5 THEN '杨' || (ARRAY['建国','建军','志强','伟','明','涛','强','军','华','勇'])[floor(random() * 10) + 1]
                WHEN 6 THEN '赵' || (ARRAY['建国','建军','志强','伟','明','涛','强','军','华','勇'])[floor(random() * 10) + 1]
                WHEN 7 THEN '黄' || (ARRAY['建国','建军','志强','伟','明','涛','强','军','华','勇'])[floor(random() * 10) + 1]
                WHEN 8 THEN '周' || (ARRAY['建国','建军','志强','伟','明','涛','强','军','华','勇'])[floor(random() * 10) + 1]
                ELSE '吴' || (ARRAY['建国','建军','志强','伟','明','涛','强','军','华','勇'])[floor(random() * 10) + 1]
            END,  -- 姓名
            titles[floor(random() * array_length(titles, 1)) + 1],  -- 职称
            departments[floor(random() * array_length(departments, 1)) + 1],  -- 部门
            'teacher' || i || '@university.edu.cn',  -- 邮箱
            '139' || LPAD((floor(random() * 100000000))::TEXT, 8, '0'),  -- 手机号
            departments[floor(random() * array_length(departments, 1)) + 1] || floor(random() * 10)::TEXT + '0' || '室',  -- 办公室
            CASE floor(random() * 10)
                WHEN 0 THEN '人工智能与机器学习'
                WHEN 1 THEN '大数据分析与处理'
                WHEN 2 THEN '计算机网络与安全'
                WHEN 3 THEN '软件工程与开发'
                WHEN 4 THEN '数字信号处理'
                WHEN 5 THEN '金融工程与风险管理'
                WHEN 6 THEN '企业管理与战略'
                WHEN 7 THEN '机械设计与制造'
                WHEN 8 THEN '应用语言学'
                ELSE '基础数学研究'
            END,  -- 研究方向
            '$2a$10$abcdefghijklmnopqrstuv',  -- 密码哈希
            CASE floor(random() * 20) WHEN 0 THEN 'INACTIVE' ELSE 'ACTIVE' END  -- 状态
        );
    END LOOP;
END $$;


-- 生成系统用户数据（关联学生和教师）
-- 先为所有学生创建账号
INSERT INTO sys_user (username, password_hash, role, ref_id, status)
SELECT 
    s.student_id as username,
    '$2a$10$abcdefghijklmnopqrstuv' as password_hash,
    'STUDENT' as role,
    s.id as ref_id,
    s.status
FROM students s
LIMIT 50;  -- 先插入50个学生账号

-- 再为所有教师创建账号
INSERT INTO sys_user (username, password_hash, role, ref_id, status)
SELECT 
    t.teacher_id as username,
    '$2a$10$abcdefghijklmnopqrstuv' as password_hash,
    'TEACHER' as role,
    t.id as ref_id,
    t.status
FROM teachers t
LIMIT 45;  -- 插入45个教师账号

-- 创建管理员账号（5个）
INSERT INTO sys_user (username, password_hash, role, ref_id, status) VALUES
('admin', '$2a$10$abcdefghijklmnopqrstuv', 'ADMIN', NULL, 'ACTIVE'),
('admin001', '$2a$10$abcdefghijklmnopqrstuv', 'ADMIN', NULL, 'ACTIVE'),
('admin002', '$2a$10$abcdefghijklmnopqrstuv', 'ADMIN', NULL, 'ACTIVE'),
('sysadmin', '$2a$10$abcdefghijklmnopqrstuv', 'ADMIN', NULL, 'ACTIVE'),
('superadmin', '$2a$10$abcdefghijklmnopqrstuv', 'ADMIN', NULL, 'ACTIVE');

-- 生成100门课程数据
DO $$ 
DECLARE
    semesters VARCHAR[] := ARRAY['2024-2025-1', '2024-2025-2', '2025-2026-1', '2025-2026-2'];
    course_types VARCHAR[] := ARRAY['公共必修', '专业必修', '专业选修', '通识选修', '实践课程'];
    teacher_ids INT[];
    i INT;
    semester_val VARCHAR;
    year_val INT;
    teacher_id_val INT;
    course_code_prefix VARCHAR;
BEGIN
    -- 获取所有教师ID
    SELECT array_agg(id) INTO teacher_ids FROM teachers;
    
    FOR i IN 1..100 LOOP
        -- 随机选择学期和年份
        semester_val := semesters[floor(random() * array_length(semesters, 1)) + 1];
        year_val := substring(semester_val from 1 for 4)::INT;
        
        -- 随机选择教师
        teacher_id_val := teacher_ids[floor(random() * array_length(teacher_ids, 1)) + 1];
        
        -- 根据课程类型生成课程代码
        CASE floor(random() * 5)
            WHEN 0 THEN course_code_prefix := 'COMP';  -- 计算机
            WHEN 1 THEN course_code_prefix := 'MATH';  -- 数学
            WHEN 2 THEN course_code_prefix := 'ENGL';  -- 英语
            WHEN 3 THEN course_code_prefix := 'PHYS';  -- 物理
            WHEN 4 THEN course_code_prefix := 'ECON';  -- 经济
            ELSE course_code_prefix := 'GENE';  -- 通识
        END CASE;
        
        INSERT INTO courses (
            course_code, course_name, credits, course_hours, course_type,
            description, max_capacity, current_enrollment, semester, year,
            teacher_id, schedule_info, status
        ) VALUES (
            course_code_prefix || LPAD(i::TEXT, 4, '0'),  -- 课程代码
            CASE floor(random() * 20)
                WHEN 0 THEN '高等数学A'
                WHEN 1 THEN '线性代数'
                WHEN 2 THEN '概率论与数理统计'
                WHEN 3 THEN '大学英语（一）'
                WHEN 4 THEN '计算机导论'
                WHEN 5 THEN 'C语言程序设计'
                WHEN 6 THEN '数据结构'
                WHEN 7 THEN '操作系统'
                WHEN 8 THEN '数据库系统'
                WHEN 9 THEN '计算机网络'
                WHEN 10 THEN '人工智能导论'
                WHEN 11 THEN '机器学习'
                WHEN 12 THEN '数字电路'
                WHEN 13 THEN '模拟电路'
                WHEN 14 THEN '信号与系统'
                WHEN 15 THEN '微观经济学'
                WHEN 16 THEN '宏观经济学'
                WHEN 17 THEN '管理学原理'
                WHEN 18 THEN '会计学'
                WHEN 19 THEN '市场营销学'
                ELSE '大学物理'
            END || '（' || semester_val || '）',  -- 课程名称
            (floor(random() * 4) + 1)::NUMERIC(3,1),  -- 学分 1-5
            (floor(random() * 4) + 1) * 16,  -- 课时 16-80
            course_types[floor(random() * array_length(course_types, 1)) + 1],  -- 课程类型
            '本课程主要介绍相关领域的理论知识、实践技能和应用案例，适合相关专业学生学习。',  -- 描述
            (floor(random() * 3) + 2) * 50,  -- 最大容量 100-200
            floor(random() * 100),  -- 当前选课人数
            semester_val,  -- 学期
            year_val,  -- 年份
            teacher_id_val,  -- 教师ID
            '每周' || CASE floor(random() * 5)
                WHEN 0 THEN '一、三'
                WHEN 1 THEN '二、四'
                WHEN 2 THEN '一、三、五'
                WHEN 3 THEN '二'
                ELSE '四'
            END || '上课',  -- 安排信息
            CASE floor(random() * 10) 
                WHEN 0 THEN 'CLOSED' 
                WHEN 1 THEN 'CANCELLED' 
                ELSE 'OPEN' 
            END  -- 状态
        );
    END LOOP;
END $$;

-- 为每门课程生成1-3个上课时间
DO $$ 
DECLARE
    course_ids INT[];
    course_id_val INT;
    i INT;
    j INT;
    schedule_count INT;
BEGIN
    -- 获取所有课程ID
    SELECT array_agg(id) INTO course_ids FROM courses;
    
    -- 为每门课程生成时间安排
    FOR i IN 1..array_length(course_ids, 1) LOOP
        course_id_val := course_ids[i];
        schedule_count := floor(random() * 3) + 1;  -- 1-3个时间段
        
        FOR j IN 1..schedule_count LOOP
            INSERT INTO course_schedules (
                course_id, day_of_week, start_time, end_time, classroom
            ) VALUES (
                course_id_val,
                floor(random() * 5) + 1,  -- 星期1-5
                CASE floor(random() * 5)
                    WHEN 0 THEN '08:00:00'
                    WHEN 1 THEN '10:00:00'
                    WHEN 2 THEN '13:30:00'
                    WHEN 3 THEN '15:30:00'
                    ELSE '18:30:00'
                END,
                CASE floor(random() * 5)
                    WHEN 0 THEN '09:40:00'
                    WHEN 1 THEN '11:40:00'
                    WHEN 2 THEN '15:00:00'
                    WHEN 3 THEN '17:00:00'
                    ELSE '20:00:00'
                END,
                CASE floor(random() * 10)
                    WHEN 0 THEN '第一教学楼101'
                    WHEN 1 THEN '第一教学楼102'
                    WHEN 2 THEN '第二教学楼201'
                    WHEN 3 THEN '第二教学楼202'
                    WHEN 4 THEN '计算机中心301'
                    WHEN 5 THEN '计算机中心302'
                    WHEN 6 THEN '理学院101'
                    WHEN 7 THEN '经管楼201'
                    WHEN 8 THEN '外语楼101'
                    ELSE '综合实验楼301'
                END
            );
        END LOOP;
    END LOOP;
END $$;


-- 生成选课记录
DO $$ 
DECLARE
    student_ids INT[];
    course_ids INT[];
    student_id_val INT;
    course_id_val INT;
    i INT;
    semester_val VARCHAR;
BEGIN
    -- 获取学生和课程ID
    SELECT array_agg(id) INTO student_ids FROM students WHERE status = 'ACTIVE';
    SELECT array_agg(id) INTO course_ids FROM courses WHERE status = 'OPEN';
    
    -- 生成100条选课记录
    FOR i IN 1..100 LOOP
        student_id_val := student_ids[floor(random() * array_length(student_ids, 1)) + 1];
        course_id_val := course_ids[floor(random() * array_length(course_ids, 1)) + 1];
        
        -- 获取课程的学期
        SELECT semester INTO semester_val FROM courses WHERE id = course_id_val;
        
        -- 避免重复选课
        BEGIN
            INSERT INTO course_selections (
                student_id, course_id, semester, selection_type, status
            ) VALUES (
                student_id_val,
                course_id_val,
                semester_val,
                CASE floor(random() * 10) WHEN 0 THEN 'RETRY' ELSE 'NORMAL' END,
                CASE floor(random() * 20) 
                    WHEN 0 THEN 'WITHDRAWN' 
                    WHEN 1 THEN 'FAILED' 
                    ELSE 'SELECTED' 
                END
            );
        EXCEPTION WHEN unique_violation THEN
            -- 如果重复则跳过
            CONTINUE;
        END;
    END LOOP;
END $$;



-- 为选课记录生成成绩 - 修正版，5%不及格，95%呈正态分布
DO $$ 
DECLARE
    selection_ids INT[];
    selection_id_val INT;
    i INT;
    selection_count INT;
    regular_score_val NUMERIC(5,2);
    midterm_score_val NUMERIC(5,2);
    final_score_val NUMERIC(5,2);
    total_score_val NUMERIC(5,2);
    letter_grade_val VARCHAR(5);
    grade_point_val NUMERIC(3,2);
    is_passed_val BOOLEAN;
    random_val NUMERIC;
    z_score NUMERIC;
    normal_score NUMERIC;
BEGIN
    -- 获取选课记录ID
    SELECT array_agg(id) INTO selection_ids FROM course_selections 
    WHERE status IN ('SELECTED', 'FAILED');
    
    selection_count := array_length(selection_ids, 1);
    
    -- 为每条选课记录生成成绩
    FOR i IN 1..selection_count LOOP
        selection_id_val := selection_ids[i];
        
        -- 生成正态分布的成绩（均值为75，标准差为10）
        -- Box-Muller变换生成正态分布随机数
        random_val := random();
        z_score := sqrt(-2.0 * ln(random_val)) * cos(2 * pi() * random());
        normal_score := 75 + z_score * 10;  -- 均值75，标准差10
        
        -- 5%的学生不及格（强制设置低分）
        IF random() < 0.05 THEN
            -- 不及格成绩：50-59分
            normal_score := 50 + random() * 9;
        END IF;
        
        -- 确保成绩在0-100范围内
        normal_score := GREATEST(0, LEAST(100, normal_score));
        
        -- 基于正态分布生成各项成绩
        -- 平时成绩略高（学生通常平时表现较好）
        regular_score_val := ROUND(GREATEST(0, LEAST(100, normal_score + (random() * 10 - 5))), 2);
        -- 期中成绩
        midterm_score_val := ROUND(GREATEST(0, LEAST(100, normal_score + (random() * 15 - 7.5))), 2);
        -- 期末成绩
        final_score_val := ROUND(GREATEST(0, LEAST(100, normal_score + (random() * 20 - 10))), 2);
        
        -- 计算总成绩（平时30% + 期中30% + 期末40%）
        total_score_val := ROUND(
            regular_score_val * 0.3 + 
            midterm_score_val * 0.3 + 
            final_score_val * 0.4, 
            2
        );
        
        -- 确保总成绩在0-100范围内
        total_score_val := GREATEST(0, LEAST(100, total_score_val));
        
        -- 确定等级和绩点
        IF total_score_val >= 90 THEN
            letter_grade_val := 'A';
            grade_point_val := 4.0;
            is_passed_val := TRUE;
        ELSIF total_score_val >= 85 THEN
            letter_grade_val := 'A-';
            grade_point_val := 3.7;
            is_passed_val := TRUE;
        ELSIF total_score_val >= 82 THEN
            letter_grade_val := 'B+';
            grade_point_val := 3.3;
            is_passed_val := TRUE;
        ELSIF total_score_val >= 78 THEN
            letter_grade_val := 'B';
            grade_point_val := 3.0;
            is_passed_val := TRUE;
        ELSIF total_score_val >= 75 THEN
            letter_grade_val := 'B-';
            grade_point_val := 2.7;
            is_passed_val := TRUE;
        ELSIF total_score_val >= 72 THEN
            letter_grade_val := 'C+';
            grade_point_val := 2.3;
            is_passed_val := TRUE;
        ELSIF total_score_val >= 68 THEN
            letter_grade_val := 'C';
            grade_point_val := 2.0;
            is_passed_val := TRUE;
        ELSIF total_score_val >= 64 THEN
            letter_grade_val := 'C-';
            grade_point_val := 1.7;
            is_passed_val := TRUE;
        ELSIF total_score_val >= 60 THEN
            letter_grade_val := 'D';
            grade_point_val := 1.0;
            is_passed_val := TRUE;
        ELSE
            letter_grade_val := 'F';
            grade_point_val := 0.0;
            is_passed_val := FALSE;
        END IF;
        
        -- 插入成绩记录
        INSERT INTO grades (
            selection_id, regular_score, midterm_score, final_score,
            total_score, grade_point, letter_grade, is_passed,
            teacher_comment, published, published_date
        ) VALUES (
            selection_id_val,
            regular_score_val,
            midterm_score_val,
            final_score_val,
            total_score_val,
            grade_point_val,
            letter_grade_val,
            is_passed_val,
            CASE 
                WHEN total_score_val >= 90 THEN '成绩优异，表现突出'
                WHEN total_score_val >= 80 THEN '表现良好，继续努力'
                WHEN total_score_val >= 70 THEN '达到要求，有待提高'
                WHEN total_score_val >= 60 THEN '勉强及格，需要加强学习'
                ELSE '不及格，建议重修'
            END,
            random() > 0.2,  -- 80%的成绩已发布
            CASE WHEN random() > 0.2 THEN 
                CURRENT_TIMESTAMP - (floor(random() * 30))::INT * INTERVAL '1 day'
            ELSE NULL END
        );
    END LOOP;
END $$;
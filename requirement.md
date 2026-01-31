学生选课系统需求文档
1. 系统概述
项目名称：学生选课管理系统
目标用户：学生、教师、系统管理员
核心功能：课程管理、选课管理、成绩管理、用户管理

2. 用户角色与权限
2.1 学生
查看可选课程列表

选择/退选课程

查看已选课程

查看个人成绩

修改个人信息

2.2 教师
发布课程信息

管理自己开设的课程

录入/修改学生成绩

查看选课学生名单

导出成绩单

2.3 管理员
管理所有用户账号

审核课程开设申请

管理学期设置

系统数据统计分析

系统参数配置

3. 功能模块详细需求
3.1 用户认证模块
功能列表：
用户登录/登出

JWT Token认证

密码修改

忘记密码（邮箱验证）

角色权限验证

API接口：
java
POST /api/auth/login          // 用户登录
POST /api/auth/logout         // 用户登出
PUT  /api/auth/password       // 修改密码
POST /api/auth/reset-password // 重置密码
3.2 学生管理模块
实体设计：
sql
-- 学生表
CREATE TABLE students (
    id BIGSERIAL PRIMARY KEY,
    student_id VARCHAR(20) UNIQUE NOT NULL,  -- 学号
    name VARCHAR(50) NOT NULL,
    gender VARCHAR(10),
    birth_date DATE,
    email VARCHAR(100),
    phone VARCHAR(20),
    major VARCHAR(100),          -- 专业
    enrollment_year INTEGER,     -- 入学年份
    class_name VARCHAR(50),      -- 班级
    status VARCHAR(20) DEFAULT 'ACTIVE', -- 状态：ACTIVE, GRADUATED, SUSPENDED
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
功能：
学生信息CRUD

按条件查询学生

批量导入学生信息

学生状态管理

3.3 教师管理模块
实体设计：
sql
-- 教师表
CREATE TABLE teachers (
    id BIGSERIAL PRIMARY KEY,
    teacher_id VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(50) NOT NULL,
    title VARCHAR(50),           -- 职称
    department VARCHAR(100),     -- 院系
    email VARCHAR(100),
    phone VARCHAR(20),
    office VARCHAR(100),         -- 办公室
    research_field TEXT,         -- 研究方向
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
3.4 课程管理模块
实体设计：
sql
-- 课程表
CREATE TABLE courses (
    id BIGSERIAL PRIMARY KEY,
    course_code VARCHAR(20) UNIQUE NOT NULL,  -- 课程代码
    course_name VARCHAR(200) NOT NULL,
    credits NUMERIC(3,1) NOT NULL,            -- 学分
    course_hours INTEGER,                      -- 学时
    course_type VARCHAR(50),                   -- 课程类型：必修/选修/通识
    description TEXT,
    max_capacity INTEGER DEFAULT 100,         -- 最大容量
    current_enrollment INTEGER DEFAULT 0,      -- 当前选课人数
    semester VARCHAR(20),                      -- 学期：2024-2025-1
    year INTEGER,                              -- 学年
    teacher_id BIGINT REFERENCES teachers(id),
    schedule_info TEXT,                        -- 上课时间地点
    status VARCHAR(20) DEFAULT 'OPEN',        -- 状态：OPEN, CLOSED, CANCELLED
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 课程时间表
CREATE TABLE course_schedules (
    id BIGSERIAL PRIMARY KEY,
    course_id BIGINT REFERENCES courses(id),
    day_of_week INTEGER,        -- 星期几：1-7
    start_time TIME,            -- 开始时间
    end_time TIME,              -- 结束时间
    classroom VARCHAR(100),     -- 教室
    UNIQUE(course_id, day_of_week, start_time)
);
功能：
课程信息发布与编辑

课程查询（按学期、类型、教师等）

课程状态管理

课程冲突检查

课程时间表管理

3.5 选课管理模块
实体设计：
sql
-- 选课记录表
CREATE TABLE course_selections (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT REFERENCES students(id),
    course_id BIGINT REFERENCES courses(id),
    selection_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    selection_type VARCHAR(20) DEFAULT 'NORMAL', -- NORMAL, ADD, DROP
    status VARCHAR(20) DEFAULT 'SELECTED',       -- SELECTED, WITHDRAWN, COMPLETED
    UNIQUE(student_id, course_id, semester)
);

-- 选课限制表（先修课程）
CREATE TABLE prerequisites (
    id BIGSERIAL PRIMARY KEY,
    course_id BIGINT REFERENCES courses(id),
    prerequisite_id BIGINT REFERENCES courses(id),  -- 先修课程
    is_mandatory BOOLEAN DEFAULT TRUE
);
业务规则：
选课时间限制：每学期固定选课时间段

容量限制：课程选课人数不能超过最大容量

先修课程限制：必须完成先修课程才能选课

时间冲突检查：同一时间不能选择两门课

学分限制：每学期选课学分上限

选课类型限制：必修课必须选

功能：
学生选课/退课

选课冲突检测

选课结果查询

选课记录导出

选课统计报表

3.6 成绩管理模块
实体设计：
sql
-- 成绩表
CREATE TABLE grades (
    id BIGSERIAL PRIMARY KEY,
    selection_id BIGINT REFERENCES course_selections(id),
    regular_score NUMERIC(5,2),     -- 平时成绩
    midterm_score NUMERIC(5,2),     -- 期中成绩
    final_score NUMERIC(5,2),       -- 期末成绩
    total_score NUMERIC(5,2),       -- 总评成绩
    grade_point NUMERIC(3,2),       -- 绩点
    letter_grade VARCHAR(5),        -- 等级：A, B, C, D, F
    is_passed BOOLEAN,              -- 是否通过
    teacher_comment TEXT,           -- 教师评语
    published BOOLEAN DEFAULT FALSE,-- 是否已发布
    published_date TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
功能：
成绩录入与修改

成绩发布与撤销

成绩查询与统计

GPA计算

成绩单导出（PDF/Excel）

成绩分布分析

3.7 系统管理模块
功能：
学期管理

系统参数配置

用户角色权限管理

操作日志审计

数据备份与恢复

系统监控

4. 数据库设计要点
4.1 核心表关系
text
学生(1) -- (n) 选课记录(n) -- (1) 课程(1) -- (n) 课程时间
教师(1) -- (n) 课程
课程(1) -- (n) 先修课程（自关联）
4.2 索引设计
sql
-- 常用查询索引
CREATE INDEX idx_student_id ON students(student_id);
CREATE INDEX idx_course_semester ON courses(semester, status);
CREATE INDEX idx_selection_student ON course_selections(student_id, semester);
CREATE INDEX idx_selection_course ON course_selections(course_id);
CREATE INDEX idx_grade_selection ON grades(selection_id);
5. API设计规范
5.1 统一响应格式
json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {},
  "timestamp": "2024-01-30T10:30:00Z"
}
5.2 分页查询参数
java
public class PageParam {
    private Integer page = 1;
    private Integer size = 20;
    private String sortBy = "id";
    private String direction = "DESC";
}
6. 前端页面设计
6.1 页面列表
登录页面 - 用户登录

学生仪表盘 - 学生首页

课程浏览页面 - 查看可选课程

我的课程页面 - 已选课程管理

成绩查询页面 - 查看成绩

教师课程管理 - 教师课程管理

成绩录入页面 - 教师录入成绩

系统管理后台 - 管理员功能

6.2 组件设计
Layout布局组件

CourseCard课程卡片

ScheduleTable课表组件

GradeChart成绩图表

Pagination分页组件

FilterBar筛选栏

7. 非功能需求
7.1 性能需求
页面加载时间：< 3秒

并发用户：支持1000+同时在线

数据查询响应：< 2秒

7.2 安全需求
用户密码加密存储

API接口权限验证

防止SQL注入和XSS攻击

敏感数据脱敏

7.3 可用性需求
响应式设计，支持移动端

操作友好的用户界面

完善的错误提示

操作确认机制

8. 开发计划建议
Phase 1: 基础框架搭建
项目初始化

数据库设计

用户认证模块

基础CRUD功能

Phase 2: 核心功能开发
课程管理模块

选课管理模块

成绩管理模块

Phase 3: 高级功能
选课冲突检测

成绩统计分析

数据导出功能

Phase 4: 优化与测试
性能优化

安全加固

系统测试

文档完善

9. 测试用例要点
9.1 单元测试
业务逻辑验证

异常处理测试

边界条件测试

9.2 集成测试
API接口测试

数据库操作测试

用户流程测试

9.3 系统测试
选课完整流程

成绩管理流程

权限控制测试

10. 部署与维护
10.1 环境要求
Java 25

PostgreSQL 16+

Node.js 18+

Nginx（可选）

10.2 配置文件
yaml
# application.yml 示例
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/course_system
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    show-sql: true
    hibernate:
      ddl-auto: validate
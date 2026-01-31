# 学生选课系统 - 开发说明

## 项目结构

- **studentcourse**：后端（Java 25 + Spring Boot 4 + MyBatis + PostgreSQL）
- **studentcourseweb**：前端（Vite + React 18 + Ant Design + React Router + axios）

## 已完成模块

### Phase 1 基础框架
- **后端**：统一响应格式、分页参数、全局异常处理、JWT 认证、Security 配置、CORS、SpringDoc、init.sql（表结构 + 索引）
- **前端**：Vite + React、Ant Design、React Router、axios 封装、AuthContext、MainLayout、PrivateRoute

### Phase 2 用户认证
- **后端**：`POST /api/auth/login`、`PUT /api/auth/password`、SysUser、AuthPrincipal（JWT 含 refId）、DataSeeder 默认 admin/admin123
- **前端**：登录页、登录态、退出、首页

### Phase 3 业务模块
- **学生管理**：后端 `/students` CRUD、前端 StudentList（管理员）
- **教师管理**：后端 `/teachers` CRUD、前端 TeacherList（管理员）
- **课程管理**：后端 `/courses` CRUD、前端 CourseList（教师/管理员可编辑）、学期下拉
- **学期**：后端 `/semesters`、`/semesters/current`，前端下拉数据
- **选课**：后端 `/selections/my`、`/selections/select`、`/selections/{id}` 退课，前端 CourseSelect（选课）、MyCourses（我的选课）
- **成绩**：后端 `/grades/my`、`/grades` 分页、`POST /grades` 录入、`PUT /grades/{id}/publish`，前端 GradeList（学生看我的成绩、教师/管理员看全部）、GradeInput（教师录入成绩）

### 角色与菜单
- **学生**：首页、课程浏览、选课、我的选课、成绩
- **教师**：首页、课程浏览、成绩、系统管理（成绩查询 + 成绩录入 + 课程管理）
- **管理员**：全部菜单（含学生管理、教师管理）

## 启动步骤

### 1. 数据库

创建 PostgreSQL 数据库并执行初始化脚本：

```bash
createdb course_system
psql -d course_system -f studentcourse/src/main/resources/init.sql
```

或使用 Docker：

```bash
docker run -d --name pg-course -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=course_system -p 5432:5432 postgres:16
# 然后执行 init.sql
psql -h localhost -U postgres -d course_system -f studentcourse/src/main/resources/init.sql
```

### 2. 后端

```bash
cd studentcourse
# 环境变量可选：DB_HOST, DB_PORT, DB_NAME, DB_USERNAME, DB_PASSWORD, SERVER_PORT
./mvnw spring-boot:run
```

- API 根路径：`http://localhost:8080/api`
- Swagger UI：`http://localhost:8080/api/swagger-ui.html`
- 默认管理员：`admin` / `admin123`（首次启动时 DataSeeder 会创建）

### 3. 前端

```bash
cd studentcourseweb
npm install
# 开发：npm run dev 或 npm start（Vite 代理 /api 到后端）
npm run dev
# 生产构建：npm run build；预览：npm run preview
```

访问 `http://localhost:3000`，使用 `admin` / `admin123` 登录。开发时 Vite 会将 `/api` 代理到 `http://localhost:8080`，无需单独配置 `VITE_API_BASE_URL`。

## 可选后续扩展

- 学生/教师创建后自动或手动创建登录账号（sys_user 关联 ref_id）
- 选课冲突检测（时间冲突、先修课）
- 成绩导出（PDF/Excel）
- 学期管理 CRUD、选课时间段校验
- 操作日志审计

有不明之处可随时问我。

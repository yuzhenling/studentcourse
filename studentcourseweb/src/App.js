import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { ConfigProvider } from 'antd';
import zhCN from 'antd/locale/zh_CN';
import { AuthProvider } from './context/AuthContext';
import PrivateRoute from './components/PrivateRoute';
import RoleRoute from './components/RoleRoute';
import AdminOrRedirect from './components/AdminOrRedirect';
import StudentLayout from './components/StudentLayout';
import TeacherLayout from './components/TeacherLayout';
import Login from './pages/Login';
import AdminDashboard from './pages/AdminDashboard';
import CourseList from './pages/CourseList';
import CourseSelect from './pages/CourseSelect';
import MyCourses from './pages/MyCourses';
import GradeList from './pages/GradeList';
import StudentSettings from './pages/StudentSettings';
import TeacherSettings from './pages/TeacherSettings';
import TeacherCourseList from './pages/TeacherCourseList';
import TeacherDashboard from './pages/TeacherDashboard';
import GradeInput from './pages/GradeInput';
import Admin from './pages/Admin';
import './App.css';

function App() {
  return (
    <ConfigProvider locale={zhCN}>
      <BrowserRouter>
        <AuthProvider>
          <Routes>
            <Route path="/login" element={<Login />} />
            {/* 管理员主后台：/ 自动重定向学生/教师到各自端 */}
            <Route path="/" element={<PrivateRoute><AdminOrRedirect /></PrivateRoute>}>
              <Route index element={<AdminDashboard />} />
              <Route path="courses" element={<CourseList />} />
              <Route path="course-select" element={<CourseSelect />} />
              <Route path="my-courses" element={<MyCourses />} />
              <Route path="grades" element={<GradeList />} />
              <Route path="admin" element={<Admin />} />
            </Route>
            {/* 学生端：设置、选课、我的选课、成绩 */}
            <Route path="/student" element={<PrivateRoute><RoleRoute allowRoles={['STUDENT']}><StudentLayout /></RoleRoute></PrivateRoute>}>
              <Route index element={<CourseSelect />} />
              <Route path="my-courses" element={<MyCourses />} />
              <Route path="grades" element={<GradeList />} />
              <Route path="settings" element={<StudentSettings />} />
            </Route>
            {/* 教师端：首页统计、我的课程、成绩录入、设置 */}
            <Route path="/teacher" element={<PrivateRoute><RoleRoute allowRoles={['TEACHER']}><TeacherLayout /></RoleRoute></PrivateRoute>}>
              <Route index element={<TeacherDashboard />} />
              <Route path="courses" element={<TeacherCourseList />} />
              <Route path="grades" element={<GradeInput />} />
              <Route path="settings" element={<TeacherSettings />} />
            </Route>
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </AuthProvider>
      </BrowserRouter>
    </ConfigProvider>
  );
}

export default App;

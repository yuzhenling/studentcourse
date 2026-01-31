/**
 * 按角色重定向：学生只能访问 /student/*，教师只能访问 /teacher/*，管理员访问 /*
 */
import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function RoleRoute({ children, allowRoles }) {
  const { user, isAuthenticated } = useAuth();
  const location = useLocation();

  if (!isAuthenticated) {
    return <Navigate to="/login" state={{ from: location }} replace />;
  }
  const role = user?.role;
  if (allowRoles && role && !allowRoles.includes(role)) {
    if (role === 'STUDENT') return <Navigate to="/student" replace />;
    if (role === 'TEACHER') return <Navigate to="/teacher" replace />;
    return <Navigate to="/" replace />;
  }
  return children;
}

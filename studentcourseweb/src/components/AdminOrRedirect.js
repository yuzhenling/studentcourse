/**
 * 管理员进主后台，学生/教师重定向到各自端
 */
import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import MainLayout from './MainLayout';

export default function AdminOrRedirect() {
  const { user } = useAuth();
  if (user?.role === 'STUDENT') return <Navigate to="/student" replace />;
  if (user?.role === 'TEACHER') return <Navigate to="/teacher" replace />;
  return <MainLayout />;
}

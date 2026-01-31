import React, { useState } from 'react';
import { Tabs } from 'antd';
import StudentList from './StudentList';
import TeacherList from './TeacherList';
import CourseList from './CourseList';

const items = [
  { key: 'students', label: '学生管理', children: <StudentList /> },
  { key: 'teachers', label: '教师管理', children: <TeacherList /> },
  { key: 'courses', label: '课程管理', children: <CourseList /> },
];

export default function Admin() {
  const [activeKey, setActiveKey] = useState('students');
  return <Tabs activeKey={activeKey} onChange={setActiveKey} items={items} />;
}

import React, { useState, useEffect } from 'react';
import { Table, Button, Select, Space, message } from 'antd';
import { gradeApi } from '../api/grade';
import { semesterApi } from '../api/semester';
import { useAuth } from '../context/AuthContext';

export default function GradeList() {
  const { user } = useAuth();
  const [list, setList] = useState([]);
  const [loading, setLoading] = useState(false);
  const [semester, setSemester] = useState(null);
  const [semesters, setSemesters] = useState([]);
  const isStudent = user?.role === 'STUDENT';

  const loadSemesters = async () => {
    try {
      const res = await semesterApi.list();
      setSemesters(Array.isArray(res.data) ? res.data : []);
    } catch (_) {}
  };

  const load = async () => {
    setLoading(true);
    try {
      if (isStudent) {
        const res = await gradeApi.my(semester || undefined);
        setList(res.data ?? []);
      } else {
        const res = await gradeApi.page({ page: 1, size: 100, semester: semester || undefined });
        setList(res.data?.list ?? []);
      }
    } catch (e) {
      message.error(e.message || '加载失败');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadSemesters();
  }, []);

  useEffect(() => {
    load();
  }, [semester, isStudent]);

  const columns = [
    { title: '课程代码', dataIndex: 'courseCode', width: 120 },
    { title: '课程名称', dataIndex: 'courseName', ellipsis: true },
    { title: '平时', dataIndex: 'regularScore', width: 80 },
    { title: '期中', dataIndex: 'midtermScore', width: 80 },
    { title: '期末', dataIndex: 'finalScore', width: 80 },
    { title: '总评', dataIndex: 'totalScore', width: 80 },
    { title: '等级', dataIndex: 'letterGrade', width: 70 },
    { title: '是否通过', dataIndex: 'isPassed', width: 90, render: (v) => (v ? '是' : '否') },
  ];
  if (!isStudent) {
    columns.splice(2, 0, { title: '学生', dataIndex: 'studentName', width: 100 });
    columns.push({ title: '已发布', dataIndex: 'published', width: 80, render: (v) => (v ? '是' : '否') });
  }

  return (
    <div>
      <Space style={{ marginBottom: 16 }}>
        <span>学期：</span>
        <Select
          value={semester}
          onChange={setSemester}
          options={semesters.map((s) => ({ label: s.semester, value: s.semester }))}
          style={{ width: 200 }}
          allowClear
          placeholder="全部"
        />
        <Button onClick={load}>刷新</Button>
      </Space>
      <Table rowKey="id" columns={columns} dataSource={list} loading={loading} pagination={false} />
    </div>
  );
}

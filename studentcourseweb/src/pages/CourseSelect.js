import React, { useState, useEffect } from 'react';
import { Table, Button, Form, Input, Select, message } from 'antd';
import { courseApi } from '../api/course';
import { selectionApi } from '../api/selection';
import { semesterApi } from '../api/semester';

const statusOptions = [{ label: '开放', value: 'OPEN' }];

export default function CourseSelect() {
  const [list, setList] = useState([]);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(1);
  const [size] = useState(10);
  const [filters, setFilters] = useState({ status: 'OPEN' });
  const [semesters, setSemesters] = useState([]);
  const [form] = Form.useForm();

  const loadSemesters = async () => {
    try {
      const res = await semesterApi.list();
      setSemesters(Array.isArray(res.data) ? res.data : []);
    } catch (_) {}
  };

  const load = async () => {
    setLoading(true);
    try {
      const res = await courseApi.page({ page, size, ...filters });
      setList(res.data?.list ?? []);
      setTotal(res.data?.total ?? 0);
    } catch (e) {
      message.error(e.message || '加载失败');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadSemesters();
    load();
  }, []);

  useEffect(() => {
    load();
  }, [page, size, JSON.stringify(filters)]);

  const onSearch = (v) => setFilters({ ...filters, ...v });

  const onSelect = async (record) => {
    try {
      await selectionApi.select(record.id, filters.semester || undefined);
      message.success('选课成功');
      load();
    } catch (e) {
      message.error(e.message || '选课失败');
    }
  };

  const columns = [
    { title: '课程代码', dataIndex: 'courseCode', width: 120 },
    { title: '课程名称', dataIndex: 'courseName', ellipsis: true },
    { title: '学分', dataIndex: 'credits', width: 70 },
    { title: '学期', dataIndex: 'semester', width: 110 },
    { title: '教师', dataIndex: 'teacherName', width: 100 },
    { title: '选课人数', dataIndex: 'currentEnrollment', width: 90, render: (n, r) => `${n ?? 0}/${r.maxCapacity ?? 0}` },
    {
      title: '操作',
      width: 100,
      render: (_, record) => (
        <Button type="primary" size="small" onClick={() => onSelect(record)} disabled={record.currentEnrollment >= (record.maxCapacity || 0)}>
          选课
        </Button>
      ),
    },
  ];

  return (
    <div>
      <Form layout="inline" onFinish={onSearch} style={{ marginBottom: 16 }}>
        <Form.Item name="courseCode"><Input placeholder="课程代码" allowClear /></Form.Item>
        <Form.Item name="courseName"><Input placeholder="课程名称" allowClear /></Form.Item>
        <Form.Item name="semester">
          <Select placeholder="学期" allowClear options={semesters.map((s) => ({ label: s.semester, value: s.semester }))} style={{ width: 140 }} />
        </Form.Item>
        <Form.Item name="status" initialValue="OPEN"><Select options={statusOptions} style={{ width: 100 }} /></Form.Item>
        <Form.Item><Button type="primary" htmlType="submit">查询</Button></Form.Item>
      </Form>
      <Table
        rowKey="id"
        columns={columns}
        dataSource={list}
        loading={loading}
        pagination={{ current: page, pageSize: size, total, showSizeChanger: false, onChange: setPage }}
      />
    </div>
  );
}

import React, { useState, useEffect } from 'react';
import { Table, Button, Space, Select, message, Popconfirm } from 'antd';
import { selectionApi } from '../api/selection';
import { semesterApi } from '../api/semester';

export default function MyCourses() {
  const [list, setList] = useState([]);
  const [loading, setLoading] = useState(false);
  const [semester, setSemester] = useState(null);
  const [semesters, setSemesters] = useState([]);

  const loadSemesters = async () => {
    try {
      const res = await semesterApi.list();
      setSemesters(Array.isArray(res.data) ? res.data : []);
      const cur = await semesterApi.current();
      if (cur?.data?.semester) setSemester(cur.data.semester);
    } catch (_) {}
  };

  const load = async () => {
    setLoading(true);
    try {
      const res = await selectionApi.my(semester || undefined);
      setList(res.data ?? []);
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
  }, [semester]);

  const onWithdraw = async (id) => {
    try {
      await selectionApi.withdraw(id);
      message.success('退课成功');
      load();
    } catch (e) {
      message.error(e.message || '退课失败');
    }
  };

  const columns = [
    { title: '课程代码', dataIndex: 'courseCode', width: 120 },
    { title: '课程名称', dataIndex: 'courseName', ellipsis: true },
    { title: '学期', dataIndex: 'semester', width: 120 },
    { title: '选课时间', dataIndex: 'selectionDate', width: 180, render: (t) => t ? new Date(t).toLocaleString() : '-' },
    {
      title: '操作',
      width: 100,
      render: (_, record) => (
        <Popconfirm title="确定退课？" onConfirm={() => onWithdraw(record.id)}>
          <Button type="link" danger size="small">退课</Button>
        </Popconfirm>
      ),
    },
  ];

  return (
    <div>
      <Space style={{ marginBottom: 16 }}>
        <span>学期：</span>
        <Select
          value={semester}
          onChange={setSemester}
          options={semesters.map((s) => ({ label: s.semester + (s.name ? ` ${s.name}` : ''), value: s.semester }))}
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

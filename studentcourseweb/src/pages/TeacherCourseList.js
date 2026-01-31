/**
 * 教师端 - 我的课程：仅当前教师的课程，增删改查
 */
import React, { useState, useEffect } from 'react';
import { Table, Button, Space, Form, Input, Select, Modal, message, Popconfirm } from 'antd';
import { courseApi } from '../api/course';
import { semesterApi } from '../api/semester';
import { useAuth } from '../context/AuthContext';

const statusOptions = [{ label: '开放', value: 'OPEN' }, { label: '关闭', value: 'CLOSED' }, { label: '取消', value: 'CANCELLED' }];

export default function TeacherCourseList() {
  const { user } = useAuth();
  const teacherId = user?.refId;
  const [list, setList] = useState([]);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(1);
  const [size] = useState(10);
  const [filters, setFilters] = useState({});
  const [modalOpen, setModalOpen] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [semesters, setSemesters] = useState([]);
  const [form] = Form.useForm();

  const loadSemesters = async () => {
    try {
      const res = await semesterApi.list();
      setSemesters(Array.isArray(res.data) ? res.data : []);
    } catch (_) {}
  };

  const load = async () => {
    if (!teacherId) return;
    setLoading(true);
    try {
      // 始终传当前教师 ID，只查本教师的课程（teacherId 放最后避免被 filters 覆盖）
      const res = await courseApi.page({ page, size, ...filters, teacherId });
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
  }, []);

  useEffect(() => {
    load();
  }, [page, size, teacherId, JSON.stringify(filters)]);

  const onSearch = (v) => setFilters(v || {});

  const openAdd = () => {
    setEditingId(null);
    form.resetFields();
    form.setFieldsValue({ teacherId });
    setModalOpen(true);
  };

  const openEdit = (record) => {
    setEditingId(record.id);
    form.setFieldsValue({
      courseCode: record.courseCode,
      courseName: record.courseName,
      credits: record.credits,
      courseHours: record.courseHours,
      courseType: record.courseType,
      description: record.description,
      maxCapacity: record.maxCapacity,
      semester: record.semester,
      year: record.year,
      teacherId: record.teacherId,
      scheduleInfo: record.scheduleInfo,
      status: record.status,
    });
    setModalOpen(true);
  };

  const onOk = async () => {
    try {
      const values = await form.validateFields();
      values.teacherId = teacherId;
      if (editingId) {
        await courseApi.update(editingId, values);
        message.success('更新成功');
      } else {
        await courseApi.create(values);
        message.success('新增成功');
      }
      setModalOpen(false);
      load();
    } catch (e) {
      if (e.errorFields) return;
      message.error(e.message || '操作失败');
    }
  };

  const onDelete = async (id) => {
    try {
      await courseApi.delete(id);
      message.success('删除成功');
      load();
    } catch (e) {
      message.error(e.message || '删除失败');
    }
  };

  const columns = [
    { title: '课程代码', dataIndex: 'courseCode', width: 120 },
    { title: '课程名称', dataIndex: 'courseName', ellipsis: true },
    { title: '学分', dataIndex: 'credits', width: 70 },
    { title: '学期', dataIndex: 'semester', width: 110 },
    { title: '选课人数', dataIndex: 'currentEnrollment', width: 90, render: (n, r) => `${n ?? 0}/${r.maxCapacity ?? 0}` },
    { title: '状态', dataIndex: 'status', width: 80, render: (s) => statusOptions.find((o) => o.value === s)?.label ?? s },
    {
      title: '操作',
      width: 160,
      render: (_, record) => (
        <Space>
          <Button type="link" size="small" onClick={() => openEdit(record)}>编辑</Button>
          <Popconfirm title="确定删除？" onConfirm={() => onDelete(record.id)}>
            <Button type="link" danger size="small">删除</Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  return (
    <div>
      <Form layout="inline" onFinish={onSearch} style={{ marginBottom: 16 }}>
        <Form.Item name="courseCode"><Input placeholder="课程代码" allowClear /></Form.Item>
        <Form.Item name="courseName"><Input placeholder="课程名称" allowClear /></Form.Item>
        <Form.Item name="semester"><Select placeholder="学期" allowClear options={semesters.map((s) => ({ label: s.semester, value: s.semester }))} style={{ width: 140 }} /></Form.Item>
        <Form.Item name="status"><Select placeholder="状态" allowClear options={statusOptions} style={{ width: 100 }} /></Form.Item>
        <Form.Item>
          <Space>
            <Button type="primary" htmlType="submit">查询</Button>
            <Button onClick={() => setFilters({})}>重置</Button>
            <Button type="primary" onClick={openAdd}>新增课程</Button>
          </Space>
        </Form.Item>
      </Form>
      <Table rowKey="id" columns={columns} dataSource={list} loading={loading} pagination={{ current: page, pageSize: size, total, showSizeChanger: false, onChange: setPage }} />
      <Modal title={editingId ? '编辑课程' : '新增课程'} open={modalOpen} onOk={onOk} onCancel={() => setModalOpen(false)} width={600} destroyOnClose>
        <Form form={form} layout="vertical" style={{ marginTop: 16 }}>
          <Form.Item name="courseCode" label="课程代码" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="courseName" label="课程名称" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="credits" label="学分" rules={[{ required: true }]}><Input type="number" step={0.5} /></Form.Item>
          <Form.Item name="courseHours" label="学时"><Input type="number" /></Form.Item>
          <Form.Item name="courseType" label="课程类型"><Input placeholder="必修/选修/通识" /></Form.Item>
          <Form.Item name="description" label="简介"><Input.TextArea rows={2} /></Form.Item>
          <Form.Item name="maxCapacity" label="最大容量"><Input type="number" /></Form.Item>
          <Form.Item name="semester" label="学期"><Select allowClear options={semesters.map((s) => ({ label: s.semester, value: s.semester }))} /></Form.Item>
          <Form.Item name="year" label="学年"><Input type="number" placeholder="如 2024" /></Form.Item>
          <Form.Item name="teacherId" hidden><Input type="hidden" /></Form.Item>
          <Form.Item name="scheduleInfo" label="上课时间地点"><Input.TextArea rows={2} /></Form.Item>
          <Form.Item name="status" label="状态"><Select allowClear options={statusOptions} /></Form.Item>
        </Form>
      </Modal>
    </div>
  );
}

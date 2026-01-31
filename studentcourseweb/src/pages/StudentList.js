import React, { useState, useEffect } from 'react';
import { Table, Button, Space, Form, Input, Select, Modal, message, Popconfirm } from 'antd';
import { studentApi } from '../api/student';
import { classApi } from '../api/class';

const statusOptions = [
  { label: '在读', value: 'ACTIVE' },
  { label: '已毕业', value: 'GRADUATED' },
  { label: '休学', value: 'SUSPENDED' },
];

export default function StudentList() {
  const [list, setList] = useState([]);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(1);
  const [size] = useState(10);
  const [filters, setFilters] = useState({});
  const [modalOpen, setModalOpen] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [classes, setClasses] = useState([]);
  const [form] = Form.useForm();

  const loadClasses = async () => {
    try {
      const res = await classApi.list();
      setClasses(Array.isArray(res.data) ? res.data : []);
    } catch (_) {}
  };

  useEffect(() => {
    loadClasses();
  }, []);

  const load = async () => {
    setLoading(true);
    try {
      const res = await studentApi.page({
        page,
        size,
        ...filters,
      });
      setList(res.data?.list ?? []);
      setTotal(res.data?.total ?? 0);
    } catch (e) {
      message.error(e.message || '加载失败');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, [page, size, JSON.stringify(filters)]);

  const onSearch = (v) => setFilters(v || {});

  const openAdd = () => {
    setEditingId(null);
    form.resetFields();
    setModalOpen(true);
  };

  const openEdit = (record) => {
    setEditingId(record.id);
    form.setFieldsValue({
      studentId: record.studentId,
      name: record.name,
      gender: record.gender,
      birthDate: record.birthDate,
      email: record.email,
      phone: record.phone,
      enrollmentYear: record.enrollmentYear,
      classId: record.classId,
      status: record.status,
    });
    setModalOpen(true);
  };

  const onOk = async () => {
    try {
      const values = await form.validateFields();
      if (editingId) {
        await studentApi.update(editingId, values);
        message.success('更新成功');
      } else {
        await studentApi.create(values);
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
      await studentApi.delete(id);
      message.success('删除成功');
      load();
    } catch (e) {
      message.error(e.message || '删除失败');
    }
  };

  const columns = [
    { title: '学号', dataIndex: 'studentId', width: 120 },
    { title: '姓名', dataIndex: 'name', width: 100 },
    { title: '性别', dataIndex: 'gender', width: 60 },
    { title: '入学年份', dataIndex: 'enrollmentYear', width: 100 },
    { title: '班级', dataIndex: 'className', width: 120 },
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
        <Form.Item name="studentId"><Input placeholder="学号" allowClear /></Form.Item>
        <Form.Item name="name"><Input placeholder="姓名" allowClear /></Form.Item>
        <Form.Item name="status"><Select placeholder="状态" allowClear options={statusOptions} style={{ width: 120 }} /></Form.Item>
        <Form.Item>
          <Space>
            <Button type="primary" htmlType="submit">查询</Button>
            <Button onClick={() => setFilters({})}>重置</Button>
            <Button type="primary" onClick={openAdd}>新增学生</Button>
          </Space>
        </Form.Item>
      </Form>
      <Table
        rowKey="id"
        columns={columns}
        dataSource={list}
        loading={loading}
        pagination={{
          current: page,
          pageSize: size,
          total,
          showSizeChanger: false,
          onChange: setPage,
        }}
      />
      <Modal title={editingId ? '编辑学生' : '新增学生'} open={modalOpen} onOk={onOk} onCancel={() => setModalOpen(false)} width={560} destroyOnClose>
        <Form form={form} layout="vertical" style={{ marginTop: 16 }}>
          <Form.Item name="studentId" label="学号" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="name" label="姓名" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="gender" label="性别"><Select allowClear options={[{ label: '男', value: '男' }, { label: '女', value: '女' }]} /></Form.Item>
          <Form.Item name="birthDate" label="出生日期"><Input type="date" /></Form.Item>
          <Form.Item name="email" label="邮箱"><Input /></Form.Item>
          <Form.Item name="phone" label="电话"><Input /></Form.Item>
          <Form.Item name="enrollmentYear" label="入学年份" rules={[{ required: true }]}><Input type="number" /></Form.Item>
          <Form.Item name="classId" label="班级">
            <Select
              allowClear
              placeholder="请选择班级"
              options={classes.map((c) => ({ label: c.className + (c.major ? ` (${c.major})` : ''), value: c.id }))}
              showSearch
              optionFilterProp="label"
            />
          </Form.Item>
          <Form.Item name="status" label="状态"><Select allowClear options={statusOptions} /></Form.Item>
        </Form>
      </Modal>
    </div>
  );
}

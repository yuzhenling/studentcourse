import React, { useState, useEffect } from 'react';
import { Table, Button, Space, Form, Input, Select, Modal, message, Popconfirm } from 'antd';
import { teacherApi } from '../api/teacher';

const statusOptions = [{ label: '在职', value: 'ACTIVE' }, { label: '离职', value: 'INACTIVE' }];

export default function TeacherList() {
  const [list, setList] = useState([]);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(1);
  const [size] = useState(10);
  const [filters, setFilters] = useState({});
  const [modalOpen, setModalOpen] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [form] = Form.useForm();

  const load = async () => {
    setLoading(true);
    try {
      const res = await teacherApi.page({ page, size, ...filters });
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
      teacherId: record.teacherId,
      name: record.name,
      title: record.title,
      department: record.department,
      email: record.email,
      phone: record.phone,
      office: record.office,
      researchField: record.researchField,
      status: record.status,
    });
    setModalOpen(true);
  };

  const onOk = async () => {
    try {
      const values = await form.validateFields();
      if (editingId) {
        await teacherApi.update(editingId, values);
        message.success('更新成功');
      } else {
        await teacherApi.create(values);
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
      await teacherApi.delete(id);
      message.success('删除成功');
      load();
    } catch (e) {
      message.error(e.message || '删除失败');
    }
  };

  const columns = [
    { title: '工号', dataIndex: 'teacherId', width: 120 },
    { title: '姓名', dataIndex: 'name', width: 100 },
    { title: '职称', dataIndex: 'title', width: 100 },
    { title: '院系', dataIndex: 'department', ellipsis: true },
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
        <Form.Item name="teacherId"><Input placeholder="工号" allowClear /></Form.Item>
        <Form.Item name="name"><Input placeholder="姓名" allowClear /></Form.Item>
        <Form.Item name="department"><Input placeholder="院系" allowClear /></Form.Item>
        <Form.Item name="status"><Select placeholder="状态" allowClear options={statusOptions} style={{ width: 120 }} /></Form.Item>
        <Form.Item>
          <Space>
            <Button type="primary" htmlType="submit">查询</Button>
            <Button onClick={() => setFilters({})}>重置</Button>
            <Button type="primary" onClick={openAdd}>新增教师</Button>
          </Space>
        </Form.Item>
      </Form>
      <Table
        rowKey="id"
        columns={columns}
        dataSource={list}
        loading={loading}
        pagination={{ current: page, pageSize: size, total, showSizeChanger: false, onChange: setPage }}
      />
      <Modal title={editingId ? '编辑教师' : '新增教师'} open={modalOpen} onOk={onOk} onCancel={() => setModalOpen(false)} width={560} destroyOnClose>
        <Form form={form} layout="vertical" style={{ marginTop: 16 }}>
          <Form.Item name="teacherId" label="工号" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="name" label="姓名" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="title" label="职称"><Input /></Form.Item>
          <Form.Item name="department" label="院系"><Input /></Form.Item>
          <Form.Item name="email" label="邮箱"><Input /></Form.Item>
          <Form.Item name="phone" label="电话"><Input /></Form.Item>
          <Form.Item name="office" label="办公室"><Input /></Form.Item>
          <Form.Item name="researchField" label="研究方向"><Input.TextArea rows={2} /></Form.Item>
          <Form.Item name="status" label="状态"><Select allowClear options={statusOptions} /></Form.Item>
        </Form>
      </Modal>
    </div>
  );
}

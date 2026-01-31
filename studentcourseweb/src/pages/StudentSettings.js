/**
 * 学生 - 设置：修改自己的密码
 */
import React, { useState } from 'react';
import { Form, Input, Button, Card, message } from 'antd';
import { authApi } from '../api/auth';

export default function StudentSettings() {
  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm();

  const onFinish = async (values) => {
    if (values.newPassword !== values.confirmPassword) {
      message.error('两次输入的新密码不一致');
      return;
    }
    setLoading(true);
    try {
      await authApi.updatePassword(values.oldPassword, values.newPassword);
      message.success('密码修改成功');
      form.resetFields();
    } catch (e) {
      message.error(e.message || '修改失败');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Card title="修改密码">
      <Form form={form} layout="vertical" onFinish={onFinish} style={{ maxWidth: 400 }}>
        <Form.Item name="oldPassword" label="原密码" rules={[{ required: true }]}>
          <Input.Password placeholder="请输入原密码" />
        </Form.Item>
        <Form.Item name="newPassword" label="新密码" rules={[{ required: true, min: 6, message: '至少6位' }]}>
          <Input.Password placeholder="请输入新密码" />
        </Form.Item>
        <Form.Item name="confirmPassword" label="确认新密码" rules={[{ required: true }]}>
          <Input.Password placeholder="请再次输入新密码" />
        </Form.Item>
        <Form.Item>
          <Button type="primary" htmlType="submit" loading={loading}>确定</Button>
        </Form.Item>
      </Form>
    </Card>
  );
}

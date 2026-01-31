/**
 * 登录页：管理员/学生/教师 统一入口，按身份提交 type
 */
import React, { useState } from 'react';
import { Form, Input, Button, Card, Radio, message } from 'antd';
import { UserOutlined, LockOutlined } from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { authApi } from '../api/auth';

const TYPES = [
  { label: '管理员', value: 'ADMIN' },
  { label: '学生', value: 'STUDENT' },
  { label: '教师', value: 'TEACHER' },
];

const PLACEHOLDERS = { ADMIN: '用户名', STUDENT: '学号', TEACHER: '工号' };

export default function Login() {
  const [type, setType] = useState('ADMIN');
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  const onFinish = async (values) => {
    const loginType = values.type || type;
    setLoading(true);
    try {
      const res = await authApi.login(loginType, values.username, values.password);
      login(res);
      message.success('登录成功');
      if (loginType === 'STUDENT') navigate('/student', { replace: true });
      else if (loginType === 'TEACHER') navigate('/teacher', { replace: true });
      else navigate('/', { replace: true });
    } catch (e) {
      message.error(e.message || '登录失败');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ minHeight: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center', background: '#f0f2f5' }}>
      <Card title="学生选课管理系统" style={{ width: 400 }}>
        <Form name="login" onFinish={onFinish} autoComplete="off" size="large" initialValues={{ type: 'ADMIN' }}>
          <Form.Item label="身份" name="type">
            <Radio.Group options={TYPES} onChange={(e) => setType(e.target.value)} optionType="button" />
          </Form.Item>
          <Form.Item name="username" rules={[{ required: true, message: `请输入${PLACEHOLDERS[type]}` }]}>
            <Input prefix={<UserOutlined />} placeholder={PLACEHOLDERS[type]} />
          </Form.Item>
          <Form.Item name="password" rules={[{ required: true, message: '请输入密码' }]}>
            <Input.Password prefix={<LockOutlined />} placeholder="密码" />
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" loading={loading} block>
              登录
            </Button>
          </Form.Item>
        </Form>
      </Card>
    </div>
  );
}

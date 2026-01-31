/**
 * 主布局：顶栏 + 侧栏/内容区
 */
import React, { useState } from 'react';
import { Layout, Menu, Dropdown, Avatar, Space } from 'antd';
import { Outlet, useNavigate, useLocation } from 'react-router-dom';
import {
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  UserOutlined,
  LogoutOutlined,
  BookOutlined,
  TeamOutlined,
  DashboardOutlined,
} from '@ant-design/icons';
import { useAuth } from '../context/AuthContext';

const { Header, Sider, Content } = Layout;

const allMenuItems = [
  { key: '/', icon: <DashboardOutlined />, label: '首页', roles: ['STUDENT', 'TEACHER', 'ADMIN'] },
  { key: '/courses', icon: <BookOutlined />, label: '课程浏览', roles: ['STUDENT', 'TEACHER', 'ADMIN'] },
  { key: '/course-select', icon: <BookOutlined />, label: '选课', roles: ['STUDENT'] },
  { key: '/my-courses', icon: <BookOutlined />, label: '我的选课', roles: ['STUDENT'] },
  { key: '/grades', icon: <BookOutlined />, label: '成绩', roles: ['STUDENT', 'TEACHER', 'ADMIN'] },
  { key: '/admin', icon: <TeamOutlined />, label: '系统管理', roles: ['ADMIN', 'TEACHER'] },
];

export default function MainLayout() {
  const [collapsed, setCollapsed] = useState(false);
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const menuItems = allMenuItems.filter((item) => item.roles.includes(user?.role));

  const userMenu = {
    items: [
      { key: 'logout', icon: <LogoutOutlined />, label: '退出登录', onClick: () => { logout(); navigate('/login'); } },
    ],
  };

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Sider trigger={null} collapsible collapsed={collapsed}>
        <div style={{ height: 32, margin: 16, color: '#fff', textAlign: 'center', lineHeight: '32px' }}>
          {collapsed ? '选课' : '学生选课系统'}
        </div>
        <Menu
          theme="dark"
          mode="inline"
          selectedKeys={[location.pathname]}
          items={menuItems}
          onClick={({ key }) => navigate(key)}
        />
      </Sider>
      <Layout>
        <Header style={{ padding: '0 16px', background: '#fff', display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
          <span onClick={() => setCollapsed(!collapsed)} style={{ cursor: 'pointer', fontSize: 18 }}>
            {collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
          </span>
          <Dropdown menu={userMenu} placement="bottomRight">
            <Space style={{ cursor: 'pointer' }}>
              <Avatar icon={<UserOutlined />} />
              <span>{user?.username} ({user?.role})</span>
            </Space>
          </Dropdown>
        </Header>
        <Content style={{ margin: '24px 16px', padding: 24, background: '#fff', borderRadius: 8 }}>
          <Outlet />
        </Content>
      </Layout>
    </Layout>
  );
}

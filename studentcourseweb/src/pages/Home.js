/**
 * 首页 / 仪表盘
 */
import React from 'react';
import { Card, Row, Col, Typography } from 'antd';
import { useAuth } from '../context/AuthContext';

const { Title, Paragraph } = Typography;

export default function Home() {
  const { user } = useAuth();

  return (
    <div>
      <Title level={4}>欢迎，{user?.username}</Title>
      <Paragraph type="secondary">角色：{user?.role}</Paragraph>
      <Row gutter={16}>
        <Col span={8}>
          <Card title="快捷入口" size="small">
            课程浏览、我的选课、成绩查询 等入口将在后续模块中接入。
          </Card>
        </Col>
        <Col span={8}>
          <Card title="通知" size="small">
            暂无新通知
          </Card>
        </Col>
        <Col span={8}>
          <Card title="学期" size="small">
            当前学期：2024-2025-1
          </Card>
        </Col>
      </Row>
    </div>
  );
}

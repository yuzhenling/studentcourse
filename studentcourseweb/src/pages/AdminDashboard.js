/**
 * 管理员首页统计
 */
import React, { useState, useEffect } from 'react';
import { Card, Row, Col, Table, Statistic, Typography, message } from 'antd';
import { statsApi } from '../api/stats';

const { Title } = Typography;

export default function AdminDashboard() {
  const [loading, setLoading] = useState(true);
  const [data, setData] = useState(null);

  useEffect(() => {
    const load = async () => {
      setLoading(true);
      try {
        const res = await statsApi.adminDashboard();
        setData(res.data ?? null);
      } catch (e) {
        message.error(e.message || '加载失败');
      } finally {
        setLoading(false);
      }
    };
    load();
  }, []);

  if (loading || !data) {
    return <Card loading={loading}>加载中...</Card>;
  }

  const passRate = data.passRateOverall || {};
  const rate = passRate.rate != null ? Number(passRate.rate) : 0;

  return (
    <div>
      <Title level={4}>统计概览</Title>
      <Row gutter={16} style={{ marginBottom: 24 }}>
        <Col span={8}>
          <Card title="成绩通过率（整体）" size="small">
            <Statistic
              title="已发布成绩"
              value={rate}
              suffix="%"
            />
            <div style={{ marginTop: 8, color: '#666' }}>
              通过 {passRate.passed ?? 0} / 总数 {passRate.total ?? 0}
            </div>
          </Card>
        </Col>
      </Row>

      <Row gutter={16}>
        <Col span={12}>
          <Card title="各院系开设课程数" size="small" style={{ marginBottom: 16 }}>
            <Table
              size="small"
              rowKey="department"
              dataSource={data.coursesByDepartment || []}
              columns={[
                { title: '院系', dataIndex: 'department', key: 'department' },
                { title: '课程数', dataIndex: 'count', key: 'count', width: 100 },
              ]}
              pagination={false}
            />
          </Card>
        </Col>
        <Col span={12}>
          <Card title="按学年学生总数" size="small" style={{ marginBottom: 16 }}>
            <Table
              size="small"
              rowKey="year"
              dataSource={data.studentsByYear || []}
              columns={[
                { title: '入学学年', dataIndex: 'year', key: 'year', width: 120 },
                { title: '学生数', dataIndex: 'count', key: 'count', width: 100 },
              ]}
              pagination={false}
            />
          </Card>
        </Col>
      </Row>

      <Row gutter={16}>
        <Col span={12}>
          <Card title="按教师开设课程数" size="small" style={{ marginBottom: 16 }}>
            <Table
              size="small"
              rowKey="teacherId"
              dataSource={data.coursesByTeacher || []}
              columns={[
                { title: '教师', dataIndex: 'teacherName', key: 'teacherName' },
                { title: '课程数', dataIndex: 'count', key: 'count', width: 100 },
              ]}
              pagination={false}
            />
          </Card>
        </Col>
        <Col span={12}>
          <Card title="按院系学生通过率" size="small" style={{ marginBottom: 16 }}>
            <Table
              size="small"
              rowKey="name"
              dataSource={data.passRateByDepartment || []}
              columns={[
                { title: '院系', dataIndex: 'name', key: 'name' },
                { title: '通过率', dataIndex: 'rate', key: 'rate', width: 90, render: (r) => (r != null ? `${Number(r)}%` : '-') },
                { title: '通过/总数', key: 'pass', render: (_, r) => `${r.passed ?? 0} / ${r.total ?? 0}` },
              ]}
              pagination={false}
            />
          </Card>
        </Col>
      </Row>

      <Card title="按教师学生通过率" size="small">
        <Table
          size="small"
          rowKey="name"
          dataSource={data.passRateByTeacher || []}
          columns={[
            { title: '教师', dataIndex: 'name', key: 'name' },
            { title: '通过率', dataIndex: 'rate', key: 'rate', width: 90, render: (r) => (r != null ? `${Number(r)}%` : '-') },
            { title: '通过/总数', key: 'pass', render: (_, r) => `${r.passed ?? 0} / ${r.total ?? 0}` },
          ]}
          pagination={false}
        />
      </Card>
    </div>
  );
}

/**
 * 教师首页统计：总体通过率、按课程通过率
 */
import React, { useState, useEffect } from 'react';
import { Card, Table, Statistic, Typography, message } from 'antd';
import { statsApi } from '../api/stats';

const { Title } = Typography;

export default function TeacherDashboard() {
  const [loading, setLoading] = useState(true);
  const [data, setData] = useState(null);

  useEffect(() => {
    const load = async () => {
      setLoading(true);
      try {
        const res = await statsApi.teacherDashboard();
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
      <Title level={4}>我的教学统计</Title>
      <Card title="总体学生通过率" size="small" style={{ marginBottom: 16 }}>
        <Statistic title="已发布成绩通过率" value={rate} suffix="%" />
        <div style={{ marginTop: 8, color: '#666' }}>
          通过 {passRate.passed ?? 0} / 总数 {passRate.total ?? 0}
        </div>
      </Card>
      <Card title="按课程统计通过率" size="small">
        <Table
          size="small"
          rowKey="courseId"
          dataSource={data.passRateByCourse || []}
          columns={[
            { title: '课程', dataIndex: 'courseName', key: 'courseName' },
            { title: '通过率', dataIndex: 'rate', key: 'rate', width: 90, render: (r) => (r != null ? `${Number(r)}%` : '-') },
            { title: '通过/总数', key: 'pass', render: (_, r) => `${r.passed ?? 0} / ${r.total ?? 0}` },
          ]}
          pagination={false}
        />
      </Card>
    </div>
  );
}

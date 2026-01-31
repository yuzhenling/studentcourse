import React, { useState, useEffect } from 'react';
import { Table, Button, Select, Modal, Form, InputNumber, Input, message } from 'antd';
import { selectionApi } from '../api/selection';
import { courseApi } from '../api/course';
import { gradeApi } from '../api/grade';
import { semesterApi } from '../api/semester';

export default function GradeInput() {
  const [courses, setCourses] = useState([]);
  const [semesters, setSemesters] = useState([]);
  const [courseId, setCourseId] = useState(null);
  const [semester, setSemester] = useState(null);
  const [list, setList] = useState([]);
  const [loading, setLoading] = useState(false);
  const [modalOpen, setModalOpen] = useState(false);
  const [currentSelection, setCurrentSelection] = useState(null);
  const [form] = Form.useForm();

  const loadCourses = async () => {
    try {
      const res = await courseApi.page({ page: 1, size: 500 });
      setCourses(res.data?.list ?? []);
    } catch (_) {}
  };
  const loadSemesters = async () => {
    try {
      const res = await semesterApi.list();
      setSemesters(Array.isArray(res.data) ? res.data : []);
    } catch (_) {}
  };

  const loadSelections = async () => {
    if (!courseId) {
      setList([]);
      return;
    }
    setLoading(true);
    try {
      const [selRes, gradeRes] = await Promise.all([
        selectionApi.page({ page: 1, size: 200, courseId, semester: semester || undefined, status: 'SELECTED' }),
        gradeApi.page({ page: 1, size: 500, courseId, semester: semester || undefined }),
      ]);
      const selections = selRes.data?.list ?? [];
      const grades = gradeRes.data?.list ?? [];
      const gradeBySelectionId = Object.fromEntries((grades).map((g) => [g.selectionId, g]));
      const merged = selections.map((s) => ({ ...s, grade: gradeBySelectionId[s.id] ?? null }));
      setList(merged);
    } catch (e) {
      message.error(e.message || '加载失败');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadCourses();
    loadSemesters();
  }, []);

  useEffect(() => {
    loadSelections();
  }, [courseId, semester]);

  const openInput = (record) => {
    setCurrentSelection(record);
    form.resetFields();
    const grade = record.grade;
    form.setFieldsValue({
      selectionId: record.id,
      regularScore: grade?.regularScore ?? undefined,
      midtermScore: grade?.midtermScore ?? undefined,
      finalScore: grade?.finalScore ?? undefined,
      teacherComment: grade?.teacherComment ?? undefined,
    });
    setModalOpen(true);
  };

  const onSave = async () => {
    try {
      const values = await form.validateFields();
      await gradeApi.save(values);
      message.success('保存成功');
      setModalOpen(false);
      loadSelections();
    } catch (e) {
      if (e.errorFields) return;
      message.error(e.message || '保存失败');
    }
  };

  const columns = [
    { title: '学号/学生', dataIndex: 'studentName', width: 120 },
    { title: '课程', dataIndex: 'courseName', ellipsis: true, width: 140 },
    { title: '学期', dataIndex: 'semester', width: 100 },
    {
      title: '平时成绩',
      width: 90,
      align: 'center',
      render: (_, record) => (record.grade?.regularScore != null ? record.grade.regularScore : '—'),
    },
    {
      title: '期中成绩',
      width: 90,
      align: 'center',
      render: (_, record) => (record.grade?.midtermScore != null ? record.grade.midtermScore : '—'),
    },
    {
      title: '期末成绩',
      width: 90,
      align: 'center',
      render: (_, record) => (record.grade?.finalScore != null ? record.grade.finalScore : '—'),
    },
    {
      title: '总评',
      width: 80,
      align: 'center',
      render: (_, record) => (record.grade?.totalScore != null ? record.grade.totalScore : '—'),
    },
    {
      title: '是否通过',
      width: 88,
      align: 'center',
      render: (_, record) => {
        if (record.grade?.isPassed == null) return '—';
        return record.grade.isPassed ? '通过' : '未通过';
      },
    },
    {
      title: '发布状态',
      width: 88,
      align: 'center',
      render: (_, record) => (record.grade?.published ? '已发布' : record.grade ? '未发布' : '—'),
    },
    {
      title: '操作',
      width: 100,
      fixed: 'right',
      render: (_, record) => <Button type="primary" size="small" onClick={() => openInput(record)}>录入/修改</Button>,
    },
  ];

  return (
    <div>
      <div style={{ marginBottom: 16 }}>
        <Select
          placeholder="选择课程"
          value={courseId}
          onChange={setCourseId}
          options={courses.map((c) => ({ label: `${c.courseCode} ${c.courseName}`, value: c.id }))}
          style={{ width: 280 }}
          allowClear
        />
        <Select
          placeholder="学期"
          value={semester}
          onChange={setSemester}
          options={semesters.map((s) => ({ label: s.semester, value: s.semester }))}
          style={{ width: 140, marginLeft: 8 }}
          allowClear
        />
      </div>
      <Table rowKey="id" columns={columns} dataSource={list} loading={loading} pagination={false} scroll={{ x: 'max-content' }} />
      <Modal title="录入成绩" open={modalOpen} onOk={onSave} onCancel={() => setModalOpen(false)} destroyOnClose>
        <Form form={form} layout="vertical" style={{ marginTop: 16 }}>
          <Form.Item name="selectionId" hidden><input type="hidden" /></Form.Item>
          <Form.Item name="regularScore" label="平时成绩"><InputNumber min={0} max={100} style={{ width: '100%' }} /></Form.Item>
          <Form.Item name="midtermScore" label="期中成绩"><InputNumber min={0} max={100} style={{ width: '100%' }} /></Form.Item>
          <Form.Item name="finalScore" label="期末成绩"><InputNumber min={0} max={100} style={{ width: '100%' }} /></Form.Item>
          <Form.Item name="teacherComment" label="评语"><Input.TextArea rows={2} /></Form.Item>
        </Form>
      </Modal>
    </div>
  );
}

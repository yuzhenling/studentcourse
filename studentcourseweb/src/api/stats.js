import { request } from './request';

export const statsApi = {
  adminDashboard() {
    return request.get('/stats/admin/dashboard');
  },
  teacherDashboard() {
    return request.get('/stats/teacher/dashboard');
  },
};

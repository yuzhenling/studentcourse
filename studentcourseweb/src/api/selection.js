import { request } from './request';

export const selectionApi = {
  my(semester) {
    return request.get('/selections/my', { params: semester ? { semester } : {} });
  },
  page(params) {
    return request.get('/selections', { params });
  },
  select(courseId, semester) {
    return request.post('/selections/select', { courseId, semester });
  },
  withdraw(id) {
    return request.delete(`/selections/${id}`);
  },
  listByStudent(studentId, semester) {
    return request.get(`/selections/student/${studentId}`, { params: semester ? { semester } : {} });
  },
};

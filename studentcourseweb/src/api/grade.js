import { request } from './request';

export const gradeApi = {
  my(semester) {
    return request.get('/grades/my', { params: semester ? { semester } : {} });
  },
  page(params) {
    return request.get('/grades', { params });
  },
  getById(id) {
    return request.get(`/grades/${id}`);
  },
  save(data) {
    return request.post('/grades', data);
  },
  publish(id, published) {
    return request.put(`/grades/${id}/publish`, null, { params: { published } });
  },
};

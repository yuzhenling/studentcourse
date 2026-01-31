import { request } from './request';

export const courseApi = {
  page(params) {
    return request.get('/courses', { params });
  },
  getById(id) {
    return request.get(`/courses/${id}`);
  },
  create(data) {
    return request.post('/courses', data);
  },
  update(id, data) {
    return request.put(`/courses/${id}`, data);
  },
  delete(id) {
    return request.delete(`/courses/${id}`);
  },
};

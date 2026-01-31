import { request } from './request';

export const teacherApi = {
  page(params) {
    return request.get('/teachers', { params });
  },
  getById(id) {
    return request.get(`/teachers/${id}`);
  },
  create(data) {
    return request.post('/teachers', data);
  },
  update(id, data) {
    return request.put(`/teachers/${id}`, data);
  },
  delete(id) {
    return request.delete(`/teachers/${id}`);
  },
};

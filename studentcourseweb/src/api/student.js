import { request } from './request';

export const studentApi = {
  page(params) {
    return request.get('/students', { params });
  },
  getById(id) {
    return request.get(`/students/${id}`);
  },
  create(data) {
    return request.post('/students', data);
  },
  update(id, data) {
    return request.put(`/students/${id}`, data);
  },
  delete(id) {
    return request.delete(`/students/${id}`);
  },
};

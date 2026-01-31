/**
 * 认证相关 API
 */
import { request } from './request';

export const authApi = {
  login(type, username, password) {
    return request.post('/auth/login', { type: type || 'ADMIN', username, password });
  },

  logout() {
    return request.post('/auth/logout');
  },

  updatePassword(oldPassword, newPassword) {
    return request.put('/auth/password', { oldPassword, newPassword });
  },
};

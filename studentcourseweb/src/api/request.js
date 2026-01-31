/**
 * Axios 实例：baseURL、请求/响应拦截、Token 注入
 */
import axios from 'axios';

const baseURL = import.meta.env.VITE_API_BASE_URL || '/api';

export const request = axios.create({
  baseURL,
  timeout: 10000,
  headers: { 'Content-Type': 'application/json' },
});

request.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

request.interceptors.response.use(
  (res) => {
    const { data } = res;
    if (data && typeof data.success === 'boolean' && !data.success) {
      return Promise.reject(new Error(data.message || '请求失败'));
    }
    return res.data;
  },
  (err) => {
    if (err.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    const message = err.response?.data?.message || err.message || '网络错误';
    return Promise.reject(new Error(message));
  }
);

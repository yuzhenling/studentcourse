import { request } from './request';

export const classApi = {
  list() {
    return request.get('/classes');
  },
};

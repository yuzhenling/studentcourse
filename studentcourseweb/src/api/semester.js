import { request } from './request';

export const semesterApi = {
  list() {
    return request.get('/semesters');
  },
  current() {
    return request.get('/semesters/current');
  },
};

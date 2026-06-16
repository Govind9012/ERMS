import axios from 'axios';

const API_URL = 'http://localhost:8080/api/auth';

export const authApi = {
  login: async (identifier: string, password: string) => {
    const res = await axios.post(`${API_URL}/login`, {
      identifier,
      password,
    });
    return res.data;
  },

  signup: async (identifier: string, password: string) => {
    const res = await axios.post(`${API_URL}/signup`, {
      identifier,
      password,
    });
    return res.data;
  },
};

import { api } from './apiClient'

export const authApi = {
  login: async (username, password) => {
    return await api.post('/api/auth/login', { username, password })
  },
}

import { api } from './apiClient'

export const studentApi = {
  getDashboard: async () => {
    return await api.get('/api/student/dashboard')
  },
  getHistory: async ({ startDate, endDate, mealType } = {}) => {
    const params = new URLSearchParams()
    if (startDate) params.append('startDate', startDate)
    if (endDate) params.append('endDate', endDate)
    if (mealType) params.append('mealType', mealType)

    const query = params.toString() ? `?${params.toString()}` : ''
    return await api.get(`/api/student/history${query}`)
  },
}

import { api } from './apiClient'

export const adminApi = {
  getDashboard: async (selectedDate) => {
    const query = selectedDate ? `?selectedDate=${selectedDate}` : ''
    return await api.get(`/api/admin/dashboard${query}`)
  },

  getStudents: async () => {
    return await api.get('/api/admin/students')
  },

  getAttendance: async ({ date, mealType } = {}) => {
    const params = new URLSearchParams()
    if (date) params.append('date', date)
    if (mealType) params.append('mealType', mealType)

    const query = params.toString() ? `?${params.toString()}` : ''
    return await api.get(`/api/admin/attendance${query}`)
  },

  recordAttendance: async ({ studentId, date, mealType, status }) => {
    return await api.post('/api/admin/attendance', {
      studentId,
      date,
      mealType,
      status,
    })
  },

  updateAttendance: async (id, status) => {
    return await api.put(`/api/admin/attendance/${id}`, { status })
  },

  deleteAttendance: async (id) => {
    return await api.delete(`/api/admin/attendance/${id}`)
  },

  getSufficiency: async ({ date, mealType } = {}) => {
    const params = new URLSearchParams()
    if (date) params.append('date', date)
    if (mealType) params.append('mealType', mealType)

    const query = params.toString() ? `?${params.toString()}` : ''
    return await api.get(`/api/admin/sufficiency${query}`)
  },

  savePreparation: async ({ date, mealType, mealsPrepared, notes }) => {
    return await api.post('/api/admin/sufficiency', {
      date,
      mealType,
      mealsPrepared: Number(mealsPrepared),
      notes: notes || '',
    })
  },
}

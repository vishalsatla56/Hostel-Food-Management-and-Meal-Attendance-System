// Base API Client using native fetch

const API_BASE = import.meta.env.VITE_API_BASE_URL || ''

export async function request(endpoint, options = {}) {
  const token = localStorage.getItem('token')

  const headers = {
    'Content-Type': 'application/json',
    ...(options.headers || {}),
  }

  if (token) {
    headers['Authorization'] = `Bearer ${token}`
  }

  const config = {
    ...options,
    headers,
  }

  const url = `${API_BASE}${endpoint}`

  try {
    const response = await fetch(url, config)

    if (response.status === 204) {
      return null
    }

    const data = await response.json().catch(() => null)

    if (!response.ok) {
      if (response.status === 401) {
        localStorage.removeItem('token')
        localStorage.removeItem('user')
      }
      const errorMessage = data?.message || data?.error || `Request failed with status ${response.status}`
      const error = new Error(errorMessage)
      error.status = response.status
      error.details = data?.details || null
      throw error
    }

    return data
  } catch (err) {
    if (err.status) {
      throw err
    }
    throw new Error(err.message || 'Network error: could not connect to server.')
  }
}

export const api = {
  get: (endpoint) => request(endpoint, { method: 'GET' }),
  post: (endpoint, body) => request(endpoint, { method: 'POST', body: JSON.stringify(body) }),
  put: (endpoint, body) => request(endpoint, { method: 'PUT', body: JSON.stringify(body) }),
  delete: (endpoint) => request(endpoint, { method: 'DELETE' }),
}

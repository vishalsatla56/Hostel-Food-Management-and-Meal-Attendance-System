import React from 'react'
import { Navigate, useLocation } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export function ProtectedRoute({ children, allowedRole }) {
  const { isAuthenticated, user } = useAuth()
  const location = useLocation()

  if (!isAuthenticated) {
    return <Navigate to="/login" state={{ from: location }} replace />
  }

  if (allowedRole && user?.role !== allowedRole) {
    if (user?.role === 'ROLE_ADMIN') {
      return <Navigate to="/admin/dashboard" replace />
    } else if (user?.role === 'ROLE_STUDENT') {
      return <Navigate to="/student/dashboard" replace />
    }
    return <Navigate to="/login" replace />
  }

  return children
}

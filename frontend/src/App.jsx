import React from 'react'
import { Routes, Route, Navigate } from 'react-router-dom'
import { useAuth } from './context/AuthContext'
import { Layout } from './components/Layout'
import { ProtectedRoute } from './components/ProtectedRoute'
import { LoginPage } from './pages/LoginPage'
import { StudentDashboardPage } from './pages/StudentDashboardPage'
import { StudentAttendanceHistoryPage } from './pages/StudentAttendanceHistoryPage'
import { AdminDashboardPage } from './pages/AdminDashboardPage'
import { AttendanceEntryPage } from './pages/AttendanceEntryPage'
import { FoodSufficiencyPage } from './pages/FoodSufficiencyPage'

function RootRedirect() {
  const { isAuthenticated, isAdmin, isStudent } = useAuth()

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />
  }

  if (isAdmin) {
    return <Navigate to="/admin/dashboard" replace />
  }

  if (isStudent) {
    return <Navigate to="/student/dashboard" replace />
  }

  return <Navigate to="/login" replace />
}

export default function App() {
  return (
    <Routes>
      {/* 1. Login Page */}
      <Route path="/login" element={<LoginPage />} />

      {/* Root redirect based on role */}
      <Route path="/" element={<RootRedirect />} />

      {/* Authenticated Dashboard Shell for exactly the 5 protected pages */}
      <Route element={<Layout />}>
        {/* 2. Student Dashboard */}
        <Route
          path="/student/dashboard"
          element={
            <ProtectedRoute allowedRole="ROLE_STUDENT">
              <StudentDashboardPage />
            </ProtectedRoute>
          }
        />

        {/* 3. Student Attendance History */}
        <Route
          path="/student/history"
          element={
            <ProtectedRoute allowedRole="ROLE_STUDENT">
              <StudentAttendanceHistoryPage />
            </ProtectedRoute>
          }
        />

        {/* 4. Admin Dashboard */}
        <Route
          path="/admin/dashboard"
          element={
            <ProtectedRoute allowedRole="ROLE_ADMIN">
              <AdminDashboardPage />
            </ProtectedRoute>
          }
        />

        {/* 5. Attendance Entry Page */}
        <Route
          path="/admin/attendance"
          element={
            <ProtectedRoute allowedRole="ROLE_ADMIN">
              <AttendanceEntryPage />
            </ProtectedRoute>
          }
        />

        {/* 6. Food Sufficiency Results Page */}
        <Route
          path="/admin/sufficiency"
          element={
            <ProtectedRoute allowedRole="ROLE_ADMIN">
              <FoodSufficiencyPage />
            </ProtectedRoute>
          }
        />
      </Route>

      {/* Wildcard redirect */}
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  )
}

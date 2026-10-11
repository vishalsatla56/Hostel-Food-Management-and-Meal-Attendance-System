import React from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export function Navbar() {
  const { user, logout, isAdmin } = useAuth()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <header className="navbar">
      <div className="navbar-brand">
        <span className="navbar-logo">🍽️</span>
        <div className="navbar-title-wrap">
          <span className="navbar-title">Hostel Food Management</span>
          <span className="navbar-subtitle">Meal Attendance System</span>
        </div>
      </div>

      <div className="navbar-user">
        <div className="user-info">
          <span className="user-name">{user?.fullName || user?.username}</span>
          <span className={`user-badge ${isAdmin ? 'badge-admin' : 'badge-student'}`}>
            {isAdmin ? 'ADMINISTRATOR' : `STUDENT • ${user?.studentCode || user?.username}`}
          </span>
        </div>
        <button onClick={handleLogout} className="btn-logout" title="Log Out">
          <span>Sign Out</span>
          <span className="logout-icon">🚪</span>
        </button>
      </div>
    </header>
  )
}

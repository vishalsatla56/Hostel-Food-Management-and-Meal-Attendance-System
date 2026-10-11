import React from 'react'
import { NavLink } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export function Sidebar() {
  const { isAdmin, isStudent } = useAuth()

  return (
    <aside className="sidebar">
      <nav className="sidebar-nav">
        {isAdmin && (
          <>
            <div className="nav-group-title">ADMINISTRATION</div>
            <NavLink
              to="/admin/dashboard"
              className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}
            >
              <span className="nav-icon">📊</span>
              <span>Admin Dashboard</span>
            </NavLink>
            <NavLink
              to="/admin/attendance"
              className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}
            >
              <span className="nav-icon">✍️</span>
              <span>Attendance Entry</span>
            </NavLink>
            <NavLink
              to="/admin/sufficiency"
              className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}
            >
              <span className="nav-icon">⚖️</span>
              <span>Food Sufficiency Results</span>
            </NavLink>
          </>
        )}

        {isStudent && (
          <>
            <div className="nav-group-title">STUDENT PORTAL</div>
            <NavLink
              to="/student/dashboard"
              className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}
            >
              <span className="nav-icon">📈</span>
              <span>Student Dashboard</span>
            </NavLink>
            <NavLink
              to="/student/history"
              className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}
            >
              <span className="nav-icon">📅</span>
              <span>Attendance History</span>
            </NavLink>
          </>
        )}
      </nav>
      <div className="sidebar-footer">
        <div className="system-pill">Spring Boot + React + MongoDB</div>
      </div>
    </aside>
  )
}

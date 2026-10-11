import React from 'react'
import { Outlet } from 'react-router-dom'
import { Navbar } from './Navbar'
import { Sidebar } from './Sidebar'

export function Layout() {
  return (
    <div className="app-shell">
      <Navbar />
      <div className="app-body">
        <Sidebar />
        <main className="main-content">
          <div className="content-container">
            <Outlet />
          </div>
        </main>
      </div>
    </div>
  )
}

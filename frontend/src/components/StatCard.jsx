import React from 'react'

export function StatCard({ title, value, subtitle, icon, color = 'blue' }) {
  return (
    <div className={`stat-card stat-${color}`}>
      <div className="stat-card-header">
        <span className="stat-card-title">{title}</span>
        {icon && <span className="stat-card-icon">{icon}</span>}
      </div>
      <div className="stat-card-value">{value}</div>
      {subtitle && <div className="stat-card-subtitle">{subtitle}</div>}
    </div>
  )
}

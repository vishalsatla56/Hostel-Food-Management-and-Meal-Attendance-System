import React from 'react'

export function EmptyState({ title = 'No records found', message = 'There are no items matching your criteria.', icon = '📋' }) {
  return (
    <div className="empty-state">
      <div className="empty-state-icon">{icon}</div>
      <h3 className="empty-state-title">{title}</h3>
      <p className="empty-state-message">{message}</p>
    </div>
  )
}

import React from 'react'

export function AlertBanner({ type = 'info', message, onClose }) {
  if (!message) return null

  const icons = {
    error: '⚠️',
    success: '✅',
    info: 'ℹ️',
    warning: '⚡',
  }

  return (
    <div className={`alert-banner alert-${type}`}>
      <span className="alert-icon">{icons[type] || 'ℹ️'}</span>
      <div className="alert-text">{message}</div>
      {onClose && (
        <button type="button" className="alert-close" onClick={onClose} aria-label="Close">
          ✕
        </button>
      )}
    </div>
  )
}

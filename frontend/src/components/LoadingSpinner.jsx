import React from 'react'

export function LoadingSpinner({ message = 'Loading data...' }) {
  return (
    <div className="spinner-container">
      <div className="spinner"></div>
      <p className="spinner-text">{message}</p>
    </div>
  )
}

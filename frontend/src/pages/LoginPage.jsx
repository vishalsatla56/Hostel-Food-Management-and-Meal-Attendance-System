import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { AlertBanner } from '../components/AlertBanner'

export function LoginPage() {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [isSubmitting, setIsSubmitting] = useState(false)

  const { login } = useAuth()
  const navigate = useNavigate()

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')

    if (!username.trim() || !password) {
      setError('Please enter both username and password.')
      return
    }

    setIsSubmitting(true)
    try {
      const data = await login(username.trim(), password)
      if (data.role === 'ROLE_ADMIN') {
        navigate('/admin/dashboard', { replace: true })
      } else if (data.role === 'ROLE_STUDENT') {
        navigate('/student/dashboard', { replace: true })
      } else {
        navigate('/', { replace: true })
      }
    } catch (err) {
      setError(err.message || 'Login failed. Please check your credentials.')
    } finally {
      setIsSubmitting(false)
    }
  }

  const fillDemo = (user, pass) => {
    setUsername(user)
    setPassword(pass)
    setError('')
  }

  return (
    <div className="login-page">
      <div className="login-card">
        <div className="login-header">
          <div className="login-icon">🍽️</div>
          <h1 className="login-title">Hostel Food Management</h1>
          <p className="login-subtitle">Meal Attendance & Sufficiency System</p>
        </div>

        {error && (
          <div className="login-alert">
            <AlertBanner type="error" message={error} onClose={() => setError('')} />
          </div>
        )}

        <form onSubmit={handleSubmit} className="login-form">
          <div className="form-group">
            <label htmlFor="username">Username</label>
            <input
              id="username"
              type="text"
              className="form-input"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="e.g. admin or john.doe"
              autoComplete="username"
              required
              disabled={isSubmitting}
            />
          </div>

          <div className="form-group">
            <label htmlFor="password">Password</label>
            <input
              id="password"
              type="password"
              className="form-input"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Enter your password"
              autoComplete="current-password"
              required
              disabled={isSubmitting}
            />
          </div>

          <button
            type="submit"
            className="btn btn-primary btn-block btn-login"
            disabled={isSubmitting}
          >
            {isSubmitting ? (
              <span className="btn-loading-content">
                <span className="btn-spinner"></span>
                <span>Authenticating...</span>
              </span>
            ) : (
              'Sign In to Dashboard'
            )}
          </button>
        </form>

        <div className="demo-accounts-box">
          <div className="demo-title">Quick Demo Credentials:</div>
          <div className="demo-buttons">
            <button
              type="button"
              className="demo-btn admin-demo"
              onClick={() => fillDemo('admin', 'admin123')}
            >
              <span>🔑 Admin:</span> <code>admin</code> / <code>admin123</code>
            </button>
            <button
              type="button"
              className="demo-btn student-demo"
              onClick={() => fillDemo('john.doe', 'student123')}
            >
              <span>🎓 Student:</span> <code>john.doe</code> / <code>student123</code>
            </button>
            <button
              type="button"
              className="demo-btn student-demo"
              onClick={() => fillDemo('jane.smith', 'student123')}
            >
              <span>🎓 Student:</span> <code>jane.smith</code> / <code>student123</code>
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}

import React, { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { studentApi } from '../api/studentApi'
import { StatCard } from '../components/StatCard'
import { LoadingSpinner } from '../components/LoadingSpinner'
import { EmptyState } from '../components/EmptyState'
import { AlertBanner } from '../components/AlertBanner'

export function StudentDashboardPage() {
  const [data, setData] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  const fetchDashboard = async () => {
    setLoading(true)
    setError('')
    try {
      const result = await studentApi.getDashboard()
      setData(result)
    } catch (err) {
      setError(err.message || 'Failed to load student dashboard.')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchDashboard()
  }, [])

  if (loading) {
    return <LoadingSpinner message="Loading your meal attendance dashboard..." />
  }

  return (
    <div className="page-container">
      <div className="page-header">
        <div>
          <h1 className="page-title">Student Dashboard</h1>
          <p className="page-subtitle">Your personal mess attendance record and meal summary</p>
        </div>
        <button onClick={fetchDashboard} className="btn btn-secondary">
          <span>🔄 Refresh</span>
        </button>
      </div>

      {error && <AlertBanner type="error" message={error} onClose={() => setError('')} />}

      {data && (
        <>
          {/* Student Profile Card */}
          <div className="profile-hero-card">
            <div className="profile-avatar">👨‍🎓</div>
            <div className="profile-details">
              <h2 className="profile-name">{data.fullName}</h2>
              <div className="profile-meta-grid">
                <span className="profile-meta-item">
                  <strong>Student ID:</strong> {data.studentCode}
                </span>
                <span className="profile-meta-item">
                  <strong>Room:</strong> {data.roomNumber}
                </span>
                <span className="profile-meta-item">
                  <strong>Hostel:</strong> {data.hostelBlock}
                </span>
                <span className="profile-meta-item">
                  <strong>Department:</strong> {data.department}
                </span>
              </div>
            </div>
          </div>

          {/* Top Metrics Row */}
          <div className="stats-grid">
            <StatCard
              title="Total Meals Attended"
              value={data.totalMealsAttended}
              subtitle="All-time present count"
              icon="🍲"
              color="indigo"
            />
            <StatCard
              title="This Month's Attendance"
              value={data.currentMonthAttendanceCount}
              subtitle="Current calendar month"
              icon="📅"
              color="blue"
            />
            <StatCard
              title="Breakfast Attended"
              value={data.breakfastCount}
              subtitle="Morning meals attended"
              icon="☕"
              color="amber"
            />
            <StatCard
              title="Lunch Attended"
              value={data.lunchCount}
              subtitle="Afternoon meals attended"
              icon="🍛"
              color="green"
            />
            <StatCard
              title="Dinner Attended"
              value={data.dinnerCount}
              subtitle="Night meals attended"
              icon="🍽️"
              color="purple"
            />
          </div>

          {/* Today's Meal Status Section */}
          <div className="card section-card">
            <div className="card-header">
              <h3 className="card-title">Today's Attendance Status</h3>
              <span className="badge badge-info">Live Today</span>
            </div>
            <div className="today-meals-grid">
              {data.todayStatus?.map((item) => {
                const statusClass =
                  item.status === 'PRESENT'
                    ? 'status-present'
                    : item.status === 'ABSENT'
                    ? 'status-absent'
                    : 'status-unrecorded'

                const icon =
                  item.mealType === 'BREAKFAST'
                    ? '☕ Breakfast'
                    : item.mealType === 'LUNCH'
                    ? '🍛 Lunch'
                    : '🍽️ Dinner'

                return (
                  <div key={item.mealType} className={`today-meal-box ${statusClass}`}>
                    <div className="today-meal-type">{icon}</div>
                    <div className="today-meal-badge">
                      <span className={`badge ${statusClass}`}>{item.status.replace('_', ' ')}</span>
                    </div>
                    <div className="today-meal-time">
                      {item.status !== 'NOT_RECORDED'
                        ? `Recorded at ${item.recordedTime}`
                        : 'Not yet recorded by mess staff'}
                    </div>
                  </div>
                )
              })}
            </div>
          </div>

          {/* Recent Attendance Records */}
          <div className="card section-card">
            <div className="card-header flex-between">
              <div>
                <h3 className="card-title">Recent Meal Attendance</h3>
                <p className="card-subtitle">Last 10 recorded meals for your account</p>
              </div>
              <Link to="/student/history" className="btn btn-outline btn-sm">
                View Full Attendance History →
              </Link>
            </div>

            {data.recentRecords?.length === 0 ? (
              <EmptyState
                title="No attendance records yet"
                message="Your mess attendance records will appear here once recorded by the admin."
              />
            ) : (
              <div className="table-responsive">
                <table className="data-table">
                  <thead>
                    <tr>
                      <th>Date</th>
                      <th>Meal</th>
                      <th>Status</th>
                      <th>Recorded At</th>
                      <th>Recorded By</th>
                    </tr>
                  </thead>
                  <tbody>
                    {data.recentRecords?.map((rec) => (
                      <tr key={rec.id}>
                        <td className="cell-date">{rec.date}</td>
                        <td>
                          <span className={`meal-tag meal-${rec.mealType.toLowerCase()}`}>
                            {rec.mealType}
                          </span>
                        </td>
                        <td>
                          <span
                            className={`badge ${
                              rec.status === 'PRESENT' ? 'badge-success' : 'badge-danger'
                            }`}
                          >
                            {rec.status}
                          </span>
                        </td>
                        <td className="cell-time">
                          {rec.recordedAt ? new Date(rec.recordedAt).toLocaleString() : '-'}
                        </td>
                        <td className="cell-admin">{rec.recordedBy || 'Admin'}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </div>
        </>
      )}
    </div>
  )
}

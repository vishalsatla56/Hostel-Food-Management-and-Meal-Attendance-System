import React, { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { adminApi } from '../api/adminApi'
import { StatCard } from '../components/StatCard'
import { LoadingSpinner } from '../components/LoadingSpinner'
import { AlertBanner } from '../components/AlertBanner'

export function AdminDashboardPage() {
  const [data, setData] = useState(null)
  const [selectedDate, setSelectedDate] = useState(() => {
    return new Date().toISOString().split('T')[0]
  })
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  const fetchStats = async (date) => {
    setLoading(true)
    setError('')
    try {
      const result = await adminApi.getDashboard(date)
      setData(result)
    } catch (err) {
      setError(err.message || 'Failed to load administrator dashboard.')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchStats(selectedDate)
  }, [selectedDate])

  return (
    <div className="page-container">
      <div className="page-header">
        <div>
          <h1 className="page-title">Administrator Dashboard</h1>
          <p className="page-subtitle">Real-time mess overview, student totals, and meal analytics</p>
        </div>

        {/* Navigation to Attendance Entry & Food Sufficiency Results */}
        <div className="page-actions">
          <Link to="/admin/attendance" className="btn btn-primary">
            <span>✍️ Attendance Entry</span>
          </Link>
          <Link to="/admin/sufficiency" className="btn btn-accent">
            <span>⚖️ Food Sufficiency</span>
          </Link>
        </div>
      </div>

      {error && <AlertBanner type="error" message={error} onClose={() => setError('')} />}

      {loading && !data ? (
        <LoadingSpinner message="Calculating dashboard statistics from MongoDB..." />
      ) : data ? (
        <>
          {/* Quick Navigation Cards */}
          <div className="action-cards-grid">
            <Link to="/admin/attendance" className="action-card card-entry">
              <div className="action-card-icon">✍️</div>
              <div className="action-card-content">
                <h3>Go to Attendance Entry</h3>
                <p>Record student meal attendance, prevent duplicates, and view actual headcounts.</p>
              </div>
              <span className="action-arrow">→</span>
            </Link>

            <Link to="/admin/sufficiency" className="action-card card-sufficiency">
              <div className="action-card-icon">⚖️</div>
              <div className="action-card-content">
                <h3>Go to Food Sufficiency Results</h3>
                <p>Compare prepared meals with actual demand, check surplus or shortage status.</p>
              </div>
              <span className="action-arrow">→</span>
            </Link>
          </div>

          {/* Core System Metrics */}
          <div className="section-title-wrap">
            <h2 className="section-title">Hostel & Student Overview</h2>
          </div>

          <div className="stats-grid">
            <StatCard
              title="Registered Students"
              value={data.totalRegisteredStudents}
              subtitle="Total student accounts"
              icon="👥"
              color="blue"
            />
            <StatCard
              title="Active Students"
              value={data.totalActiveStudents}
              subtitle="Eligible for hostel meals"
              icon="✅"
              color="green"
            />
            <StatCard
              title="Total Meal Attendance"
              value={data.totalMealAttendance}
              subtitle="All-time present records"
              icon="🍲"
              color="purple"
            />
          </div>

          {/* Today's Live Attendance */}
          <div className="section-title-wrap">
            <h2 className="section-title">Today's Meal Headcount</h2>
            <span className="live-indicator">● Real-time MongoDB Count</span>
          </div>

          <div className="stats-grid">
            <StatCard
              title="Today's Breakfast"
              value={data.todayBreakfastCount}
              subtitle="Present for morning meal"
              icon="☕"
              color="amber"
            />
            <StatCard
              title="Today's Lunch"
              value={data.todayLunchCount}
              subtitle="Present for afternoon meal"
              icon="🍛"
              color="green"
            />
            <StatCard
              title="Today's Dinner"
              value={data.todayDinnerCount}
              subtitle="Present for evening meal"
              icon="🍽️"
              color="indigo"
            />
            <StatCard
              title="Today's Total Attendance"
              value={data.todayBreakfastCount + data.todayLunchCount + data.todayDinnerCount}
              subtitle="Combined meals today"
              icon="📊"
              color="blue"
            />
          </div>

          {/* Selected Date Inspector */}
          <div className="card section-card">
            <div className="card-header flex-between">
              <div>
                <h3 className="card-title">Attendance Statistics for Selected Date</h3>
                <p className="card-subtitle">
                  Inspect historical or specific day's meal attendance records
                </p>
              </div>
              <div className="date-picker-inline">
                <label htmlFor="inspectorDate" className="date-label">
                  Select Date:
                </label>
                <input
                  id="inspectorDate"
                  type="date"
                  className="form-input"
                  value={selectedDate}
                  onChange={(e) => setSelectedDate(e.target.value)}
                />
              </div>
            </div>

            <div className="stats-grid stats-grid-4">
              <StatCard
                title={`Breakfast (${selectedDate})`}
                value={data.selectedDateStats?.breakfastCount ?? 0}
                subtitle="Students present"
                icon="☕"
                color="amber"
              />
              <StatCard
                title={`Lunch (${selectedDate})`}
                value={data.selectedDateStats?.lunchCount ?? 0}
                subtitle="Students present"
                icon="🍛"
                color="green"
              />
              <StatCard
                title={`Dinner (${selectedDate})`}
                value={data.selectedDateStats?.dinnerCount ?? 0}
                subtitle="Students present"
                icon="🍽️"
                color="indigo"
              />
              <StatCard
                title={`Total for ${selectedDate}`}
                value={data.selectedDateStats?.totalAttendance ?? 0}
                subtitle="All meals on this date"
                icon="📈"
                color="purple"
              />
            </div>
          </div>
        </>
      ) : null}
    </div>
  )
}

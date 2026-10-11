import React, { useState, useEffect } from 'react'
import { studentApi } from '../api/studentApi'
import { LoadingSpinner } from '../components/LoadingSpinner'
import { EmptyState } from '../components/EmptyState'
import { AlertBanner } from '../components/AlertBanner'

export function StudentAttendanceHistoryPage() {
  const [records, setRecords] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  // Filter states
  const [startDate, setStartDate] = useState('')
  const [endDate, setEndDate] = useState('')
  const [mealType, setMealType] = useState('')

  const fetchHistory = async () => {
    setLoading(true)
    setError('')
    try {
      const data = await studentApi.getHistory({
        startDate: startDate || undefined,
        endDate: endDate || undefined,
        mealType: mealType || undefined,
      })
      setRecords(data)
    } catch (err) {
      setError(err.message || 'Failed to fetch attendance history.')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchHistory()
  }, [])

  const handleApplyFilters = (e) => {
    e.preventDefault()
    fetchHistory()
  }

  const handleResetFilters = () => {
    setStartDate('')
    setEndDate('')
    setMealType('')
    // Trigger fetch with empty filters
    studentApi.getHistory()
      .then((data) => setRecords(data))
      .catch((err) => setError(err.message))
  }

  const presentCount = records.filter((r) => r.status === 'PRESENT').length
  const absentCount = records.filter((r) => r.status === 'ABSENT').length

  return (
    <div className="page-container">
      <div className="page-header">
        <div>
          <h1 className="page-title">Attendance History</h1>
          <p className="page-subtitle">Complete log of your meal attendance (newest records first)</p>
        </div>
        <button onClick={fetchHistory} className="btn btn-secondary">
          <span>🔄 Refresh Records</span>
        </button>
      </div>

      {error && <AlertBanner type="error" message={error} onClose={() => setError('')} />}

      {/* Filter Card */}
      <div className="card filter-card">
        <form onSubmit={handleApplyFilters} className="filter-form">
          <div className="filter-group">
            <label htmlFor="startDate">From Date</label>
            <input
              id="startDate"
              type="date"
              className="form-input"
              value={startDate}
              onChange={(e) => setStartDate(e.target.value)}
            />
          </div>

          <div className="filter-group">
            <label htmlFor="endDate">To Date</label>
            <input
              id="endDate"
              type="date"
              className="form-input"
              value={endDate}
              onChange={(e) => setEndDate(e.target.value)}
            />
          </div>

          <div className="filter-group">
            <label htmlFor="mealType">Meal Type</label>
            <select
              id="mealType"
              className="form-select"
              value={mealType}
              onChange={(e) => setMealType(e.target.value)}
            >
              <option value="">All Meals</option>
              <option value="BREAKFAST">Breakfast</option>
              <option value="LUNCH">Lunch</option>
              <option value="DINNER">Dinner</option>
            </select>
          </div>

          <div className="filter-actions">
            <button type="submit" className="btn btn-primary">
              Filter Records
            </button>
            <button type="button" onClick={handleResetFilters} className="btn btn-outline">
              Reset
            </button>
          </div>
        </form>
      </div>

      {/* Filter Summary Stats */}
      <div className="filter-summary-row">
        <span className="summary-pill total">
          Total Records: <strong>{records.length}</strong>
        </span>
        <span className="summary-pill present">
          Present: <strong>{presentCount}</strong>
        </span>
        <span className="summary-pill absent">
          Absent: <strong>{absentCount}</strong>
        </span>
      </div>

      {/* Records Table */}
      <div className="card">
        {loading ? (
          <LoadingSpinner message="Retrieving attendance history from database..." />
        ) : records.length === 0 ? (
          <EmptyState
            title="No attendance records match your criteria"
            message="Try widening your date range or selecting another meal type."
          />
        ) : (
          <div className="table-responsive">
            <table className="data-table">
              <thead>
                <tr>
                  <th>Date</th>
                  <th>Meal Type</th>
                  <th>Attendance Status</th>
                  <th>Recorded Time</th>
                  <th>Recorded By</th>
                </tr>
              </thead>
              <tbody>
                {records.map((rec) => (
                  <tr key={rec.id}>
                    <td className="cell-date font-medium">{rec.date}</td>
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
    </div>
  )
}

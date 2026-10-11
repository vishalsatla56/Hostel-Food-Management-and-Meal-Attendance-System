import React, { useState, useEffect } from 'react'
import { adminApi } from '../api/adminApi'
import { LoadingSpinner } from '../components/LoadingSpinner'
import { EmptyState } from '../components/EmptyState'
import { AlertBanner } from '../components/AlertBanner'

export function AttendanceEntryPage() {
  const [date, setDate] = useState(() => new Date().toISOString().split('T')[0])
  const [mealType, setMealType] = useState('LUNCH')
  const [students, setStudents] = useState([])
  const [selectedStudentId, setSelectedStudentId] = useState('')
  const [status, setStatus] = useState('PRESENT')

  const [attendanceData, setAttendanceData] = useState(null)
  const [loading, setLoading] = useState(true)
  const [submitting, setSubmitting] = useState(false)
  const [alert, setAlert] = useState(null) // { type: 'success'|'error', message: '' }

  // Load students list once
  useEffect(() => {
    adminApi.getStudents()
      .then((data) => {
        setStudents(data)
        if (data.length > 0) {
          setSelectedStudentId(data[0].id)
        }
      })
      .catch((err) => {
        setAlert({ type: 'error', message: 'Failed to load students: ' + err.message })
      })
  }, [])

  // Load attendance list whenever date or mealType changes
  const fetchAttendanceList = async () => {
    setLoading(true)
    try {
      const data = await adminApi.getAttendance({ date, mealType })
      setAttendanceData(data)
    } catch (err) {
      setAlert({ type: 'error', message: err.message || 'Failed to fetch attendance records.' })
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchAttendanceList()
  }, [date, mealType])

  // Record new attendance
  const handleRecordAttendance = async (e) => {
    e.preventDefault()
    setAlert(null)

    if (!selectedStudentId) {
      setAlert({ type: 'error', message: 'Please select a student.' })
      return
    }

    setSubmitting(true)
    try {
      await adminApi.recordAttendance({
        studentId: selectedStudentId,
        date,
        mealType,
        status,
      })
      setAlert({
        type: 'success',
        message: `Attendance recorded successfully for ${date} (${mealType}).`,
      })
      await fetchAttendanceList()
    } catch (err) {
      setAlert({
        type: 'error',
        message: err.message || 'Failed to record attendance. Check for duplicates.',
      })
    } finally {
      setSubmitting(false)
    }
  }

  // Edit existing attendance record status (toggle PRESENT <-> ABSENT)
  const handleToggleStatus = async (record) => {
    const newStatus = record.status === 'PRESENT' ? 'ABSENT' : 'PRESENT'
    try {
      await adminApi.updateAttendance(record.id, newStatus)
      setAlert({
        type: 'success',
        message: `Updated ${record.studentName}'s attendance to ${newStatus}.`,
      })
      await fetchAttendanceList()
    } catch (err) {
      setAlert({ type: 'error', message: err.message || 'Failed to update attendance status.' })
    }
  }

  // Delete attendance record
  const handleDeleteRecord = async (recordId, studentName) => {
    if (!window.confirm(`Are you sure you want to remove the attendance record for ${studentName}?`)) {
      return
    }
    try {
      await adminApi.deleteAttendance(recordId)
      setAlert({ type: 'success', message: 'Attendance record deleted.' })
      await fetchAttendanceList()
    } catch (err) {
      setAlert({ type: 'error', message: err.message || 'Failed to delete attendance.' })
    }
  }

  // Check if selected student already has an attendance record for this date & meal
  const isAlreadyRecorded = attendanceData?.records?.some(
    (r) => r.studentId === selectedStudentId
  )

  return (
    <div className="page-container">
      <div className="page-header">
        <div>
          <h1 className="page-title">Attendance Entry</h1>
          <p className="page-subtitle">
            Record actual student meal attendance and manage existing entries
          </p>
        </div>
      </div>

      {alert && (
        <AlertBanner
          type={alert.type}
          message={alert.message}
          onClose={() => setAlert(null)}
        />
      )}

      {/* Date & Meal Filter Controls */}
      <div className="card control-banner-card">
        <div className="controls-row">
          <div className="control-item">
            <label htmlFor="entryDate">Target Date:</label>
            <input
              id="entryDate"
              type="date"
              className="form-input"
              value={date}
              onChange={(e) => setDate(e.target.value)}
            />
          </div>

          <div className="control-item">
            <label htmlFor="entryMeal">Meal Type:</label>
            <div className="meal-button-group">
              {['BREAKFAST', 'LUNCH', 'DINNER'].map((m) => (
                <button
                  key={m}
                  type="button"
                  className={`btn-meal-toggle ${mealType === m ? 'active' : ''}`}
                  onClick={() => setMealType(m)}
                >
                  {m === 'BREAKFAST' ? '☕ Breakfast' : m === 'LUNCH' ? '🍛 Lunch' : '🍽️ Dinner'}
                </button>
              ))}
            </div>
          </div>
        </div>
      </div>

      <div className="grid-2-cols">
        {/* Left Column: Record Attendance Form */}
        <div className="card form-card">
          <div className="card-header">
            <h3 className="card-title">Record New Attendance</h3>
            <span className="badge badge-primary">{mealType} • {date}</span>
          </div>

          <form onSubmit={handleRecordAttendance} className="attendance-form">
            <div className="form-group">
              <label htmlFor="studentSelect">Select Student</label>
              <select
                id="studentSelect"
                className="form-select"
                value={selectedStudentId}
                onChange={(e) => setSelectedStudentId(e.target.value)}
                required
              >
                {students.map((stu) => (
                  <option key={stu.id} value={stu.id}>
                    [{stu.studentCode}] {stu.fullName} — {stu.roomNumber} ({stu.hostelBlock})
                  </option>
                ))}
              </select>
            </div>

            <div className="form-group">
              <label>Attendance Status</label>
              <div className="status-radio-group">
                <label className={`radio-label ${status === 'PRESENT' ? 'radio-selected' : ''}`}>
                  <input
                    type="radio"
                    name="status"
                    value="PRESENT"
                    checked={status === 'PRESENT'}
                    onChange={(e) => setStatus(e.target.value)}
                  />
                  <span>✅ Present</span>
                </label>
                <label className={`radio-label ${status === 'ABSENT' ? 'radio-selected' : ''}`}>
                  <input
                    type="radio"
                    name="status"
                    value="ABSENT"
                    checked={status === 'ABSENT'}
                    onChange={(e) => setStatus(e.target.value)}
                  />
                  <span>❌ Absent</span>
                </label>
              </div>
            </div>

            {isAlreadyRecorded && (
              <div className="duplicate-warning">
                ⚠️ This student already has an attendance record for {date} ({mealType}). Submitting will be rejected to prevent duplicate records.
              </div>
            )}

            <button
              type="submit"
              className="btn btn-primary btn-block"
              disabled={submitting || isAlreadyRecorded}
            >
              {submitting ? 'Saving to Database...' : 'Record Attendance'}
            </button>
          </form>
        </div>

        {/* Right Column: Attendance Statistics & Headcount Summary */}
        <div className="card count-summary-card">
          <div className="card-header">
            <h3 className="card-title">Actual Attendance Count</h3>
            <span className="live-indicator">● MongoDB Count</span>
          </div>

          <div className="count-display-box">
            <div className="count-large-number">
              {attendanceData?.totalPresentCount ?? 0}
            </div>
            <div className="count-large-label">
              Actual Students Attended (PRESENT)
            </div>
            <div className="count-sub-breakdown">
              <span>Date: <strong>{date}</strong></span>
              <span>•</span>
              <span>Meal: <strong>{mealType}</strong></span>
              <span>•</span>
              <span>Absent: <strong>{attendanceData?.totalAbsentCount ?? 0}</strong></span>
            </div>
          </div>

          <div className="info-callout">
            <strong>Rule:</strong> Each student can only have one attendance record per meal per date. Only administrators are authorized to record or edit meal attendance.
          </div>
        </div>
      </div>

      {/* Full Attendance List Table */}
      <div className="card section-card">
        <div className="card-header flex-between">
          <div>
            <h3 className="card-title">
              Attendance Records for {mealType} on {date}
            </h3>
            <p className="card-subtitle">
              Total {attendanceData?.records?.length ?? 0} records logged (
              {attendanceData?.totalPresentCount ?? 0} present, {attendanceData?.totalAbsentCount ?? 0} absent)
            </p>
          </div>
          <button onClick={fetchAttendanceList} className="btn btn-outline btn-sm">
            Refresh List
          </button>
        </div>

        {loading ? (
          <LoadingSpinner message="Fetching attendance records for selected meal..." />
        ) : !attendanceData?.records?.length ? (
          <EmptyState
            title={`No attendance recorded yet for ${mealType}`}
            message={`Use the form above to record student attendance for ${date}.`}
          />
        ) : (
          <div className="table-responsive">
            <table className="data-table">
              <thead>
                <tr>
                  <th>Student ID</th>
                  <th>Student Name</th>
                  <th>Date</th>
                  <th>Meal</th>
                  <th>Status</th>
                  <th>Recorded At</th>
                  <th>Admin</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {attendanceData.records.map((rec) => (
                  <tr key={rec.id}>
                    <td>
                      <span className="code-pill">{rec.studentCode}</span>
                    </td>
                    <td className="font-medium">{rec.studentName}</td>
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
                      {rec.recordedAt ? new Date(rec.recordedAt).toLocaleTimeString() : '-'}
                    </td>
                    <td className="cell-admin">{rec.recordedBy || 'admin'}</td>
                    <td>
                      <div className="action-buttons-inline">
                        <button
                          type="button"
                          className="btn btn-sm btn-outline"
                          onClick={() => handleToggleStatus(rec)}
                          title="Edit / Toggle Status"
                        >
                          Switch to {rec.status === 'PRESENT' ? 'ABSENT' : 'PRESENT'}
                        </button>
                        <button
                          type="button"
                          className="btn btn-sm btn-danger-outline"
                          onClick={() => handleDeleteRecord(rec.id, rec.studentName)}
                          title="Delete Record"
                        >
                          ✕
                        </button>
                      </div>
                    </td>
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

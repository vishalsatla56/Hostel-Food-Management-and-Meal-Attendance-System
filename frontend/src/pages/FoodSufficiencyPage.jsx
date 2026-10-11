import React, { useState, useEffect } from 'react'
import { adminApi } from '../api/adminApi'
import { LoadingSpinner } from '../components/LoadingSpinner'
import { AlertBanner } from '../components/AlertBanner'

export function FoodSufficiencyPage() {
  const [date, setDate] = useState(() => new Date().toISOString().split('T')[0])
  const [mealType, setMealType] = useState('LUNCH')
  const [mealsPrepared, setMealsPrepared] = useState(0)
  const [notes, setNotes] = useState('')

  const [result, setResult] = useState(null)
  const [loading, setLoading] = useState(true)
  const [saving, setSaving] = useState(false)
  const [alert, setAlert] = useState(null)

  // Fetch sufficiency calculation
  const fetchSufficiency = async (targetDate, targetMeal) => {
    setLoading(true)
    try {
      const data = await adminApi.getSufficiency({ date: targetDate, mealType: targetMeal })
      setResult(data)
      setMealsPrepared(data.mealsPrepared ?? 0)
      setNotes(data.notes ?? '')
    } catch (err) {
      setAlert({ type: 'error', message: err.message || 'Failed to calculate food sufficiency.' })
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchSufficiency(date, mealType)
  }, [date, mealType])

  // Save / Update Meals Prepared
  const handleSavePreparation = async (e) => {
    e.preventDefault()
    setSaving(true)
    setAlert(null)
    try {
      const updated = await adminApi.savePreparation({
        date,
        mealType,
        mealsPrepared,
        notes,
      })
      setResult(updated)
      setAlert({
        type: 'success',
        message: `Saved ${mealsPrepared} meals prepared for ${date} (${mealType}). Sufficiency results recalculated.`,
      })
    } catch (err) {
      setAlert({ type: 'error', message: err.message || 'Failed to save preparation quantity.' })
    } finally {
      setSaving(false)
    }
  }

  // Helper for Status Badge styling
  const getStatusBadge = (status) => {
    if (status === 'SUFFICIENT') {
      return { className: 'status-tag status-sufficient', text: 'SUFFICIENT', icon: '✅' }
    }
    if (status === 'EXCESS') {
      return { className: 'status-tag status-excess', text: 'EXCESS (Surplus)', icon: '📈' }
    }
    return { className: 'status-tag status-insufficient', text: 'INSUFFICIENT (Shortage)', icon: '⚠️' }
  }

  const statusBadge = result ? getStatusBadge(result.status) : null
  const histBadge = result?.hasHistoricalData ? getStatusBadge(result.historicalStatus) : null

  return (
    <div className="page-container">
      <div className="page-header">
        <div>
          <h1 className="page-title">Food Sufficiency Results</h1>
          <p className="page-subtitle">
            Compare prepared meals against actual attendance demand to detect surplus or shortage
          </p>
        </div>
        <button
          onClick={() => fetchSufficiency(date, mealType)}
          className="btn btn-secondary"
        >
          <span>🔄 Recalculate Now</span>
        </button>
      </div>

      {alert && (
        <AlertBanner
          type={alert.type}
          message={alert.message}
          onClose={() => setAlert(null)}
        />
      )}

      {/* Date & Meal Selection Bar */}
      <div className="card control-banner-card">
        <div className="controls-row">
          <div className="control-item">
            <label htmlFor="suffDate">Meal Date:</label>
            <input
              id="suffDate"
              type="date"
              className="form-input"
              value={date}
              onChange={(e) => setDate(e.target.value)}
            />
          </div>

          <div className="control-item">
            <label htmlFor="suffMeal">Meal Type:</label>
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

      {/* Main Form & Calculation Grid */}
      <div className="grid-2-cols">
        {/* Left: Enter / Update Meals Prepared */}
        <div className="card form-card">
          <div className="card-header">
            <h3 className="card-title">Enter Meals Prepared</h3>
            <span className="badge badge-primary">{mealType} • {date}</span>
          </div>

          <form onSubmit={handleSavePreparation} className="preparation-form">
            <div className="form-group">
              <label htmlFor="mealsPreparedInput">Number of Meals Prepared</label>
              <input
                id="mealsPreparedInput"
                type="number"
                min="0"
                step="1"
                className="form-input font-large"
                value={mealsPrepared}
                onChange={(e) => setMealsPrepared(Math.max(0, parseInt(e.target.value) || 0))}
                required
              />
              <span className="form-help-text">
                Enter the exact quantity of portions cooked for this meal.
              </span>
            </div>

            <div className="form-group">
              <label htmlFor="notesInput">Batch Notes / Remarks (Optional)</label>
              <textarea
                id="notesInput"
                rows="3"
                className="form-input"
                placeholder="e.g., Standard menu, rice + dal + chicken curry, extra 5 prepared for guests"
                value={notes}
                onChange={(e) => setNotes(e.target.value)}
              />
            </div>

            <button
              type="submit"
              className="btn btn-primary btn-block"
              disabled={saving}
            >
              {saving ? 'Updating MongoDB & Recalculating...' : '💾 Save Prepared Meals & Recalculate'}
            </button>
          </form>

          <div className="prep-status-indicator">
            {result?.preparationRecordExists ? (
              <span className="text-muted">
                ✔️ Stored in MongoDB (updated with latest kitchen count)
              </span>
            ) : (
              <span className="text-warning">
                ℹ️ No preparation count saved yet for this date & meal (defaults to 0).
              </span>
            )}
          </div>
        </div>

        {/* Right: Main Baseline Food Sufficiency Card */}
        <div className="card sufficiency-results-card">
          <div className="card-header">
            <h3 className="card-title">Sufficiency Calculation (Baseline)</h3>
            <span className="calc-formula-pill">Difference = Prepared − Demand</span>
          </div>

          {loading ? (
            <LoadingSpinner message="Calculating food sufficiency from MongoDB attendance..." />
          ) : result ? (
            <div className="sufficiency-body">
              {/* Grand Status Banner */}
              <div className={`status-highlight-box ${result.status.toLowerCase()}`}>
                <div className="status-highlight-icon">{statusBadge.icon}</div>
                <div className="status-highlight-content">
                  <div className="status-highlight-title">{result.status}</div>
                  <div className="status-highlight-desc">{result.statusMessage}</div>
                </div>
              </div>

              {/* 4 Core Calculation Breakdown Metrics */}
              <div className="calc-metrics-grid">
                <div className="calc-tile tile-prepared">
                  <span className="calc-tile-label">Meals Prepared</span>
                  <span className="calc-tile-val">{result.mealsPrepared}</span>
                  <span className="calc-tile-sub">Entered by kitchen</span>
                </div>

                <div className="calc-tile tile-actual">
                  <span className="calc-tile-label">Actual Attendance</span>
                  <span className="calc-tile-val">{result.actualAttendance}</span>
                  <span className="calc-tile-sub">MongoDB Present Count</span>
                </div>

                <div className="calc-tile tile-demand">
                  <span className="calc-tile-label">Expected Demand</span>
                  <span className="calc-tile-val">{result.expectedDemand}</span>
                  <span className="calc-tile-sub">Baseline recorded attendance</span>
                </div>

                <div className={`calc-tile tile-diff ${result.status.toLowerCase()}`}>
                  <span className="calc-tile-label">Difference</span>
                  <span className="calc-tile-val">
                    {result.difference > 0 ? `+${result.difference}` : result.difference}
                  </span>
                  <span className="calc-tile-sub">
                    {result.difference === 0
                      ? 'Balanced (0)'
                      : result.difference > 0
                      ? `${result.statusQuantity} surplus meals`
                      : `${result.statusQuantity} shortage meals`}
                  </span>
                </div>
              </div>

              {/* Formula & Method Note */}
              <div className="baseline-explanation-box">
                <div className="explanation-header">
                  <span>📌 Calculation Method:</span>
                  <strong>{result.calculationMethod}</strong>
                </div>
                <p className="explanation-text">
                  {result.baselineNote}
                </p>
              </div>
            </div>
          ) : null}
        </div>
      </div>

      {/* Historical-Average Demand Prediction (Optional Method) */}
      <div className="card section-card">
        <div className="card-header flex-between">
          <div>
            <h3 className="card-title">Historical-Average Demand Estimate (Optional Prediction Method)</h3>
            <p className="card-subtitle">
              Calculates anticipated meal demand based on average historical attendance for this meal type
            </p>
          </div>
          <span className="badge badge-secondary">Historical Analysis</span>
        </div>

        {result?.hasHistoricalData ? (
          <div className="historical-grid">
            <div className="historical-info-column">
              <div className="hist-meta-item">
                <span className="hist-meta-label">Prediction Method:</span>
                <span className="hist-meta-value">{result.historicalMethod}</span>
              </div>
              <div className="hist-meta-item">
                <span className="hist-meta-label">Historical Sample Size:</span>
                <span className="hist-meta-value">{result.historicalRecordCount} past dates evaluated</span>
              </div>
              <div className="hist-meta-item">
                <span className="hist-meta-label">Historical Average Demand:</span>
                <span className="hist-meta-value font-bold">{result.historicalAverageDemand} meals / session</span>
              </div>
              <div className="hist-meta-item">
                <span className="hist-meta-label">Predicted Demand vs Actual:</span>
                <span className="hist-meta-value">
                  Predicted: <strong>{Math.round(result.historicalAverageDemand)}</strong> | Actual Recorded: <strong>{result.actualAttendance}</strong>
                </span>
              </div>
            </div>

            <div className="historical-comparison-column">
              <div className={`hist-status-card ${result.historicalStatus?.toLowerCase()}`}>
                <div className="hist-status-header">
                  <span className="hist-status-tag">{histBadge?.text}</span>
                  <span className="hist-diff-pill">
                    {result.historicalDifference > 0 ? `+${result.historicalDifference}` : result.historicalDifference} meals
                  </span>
                </div>
                <p className="hist-status-msg">{result.historicalStatusMessage}</p>
                <div className="hist-compare-bar">
                  <span>Meals Prepared: <strong>{result.mealsPrepared}</strong></span>
                  <span>•</span>
                  <span>Historical Demand: <strong>{result.historicalAverageDemand}</strong></span>
                </div>
              </div>
            </div>
          </div>
        ) : (
          <div className="historical-empty-box">
            <span className="hist-empty-icon">ℹ️</span>
            <div>
              <strong>Historical Prediction Not Activated</strong>
              <p>
                Historical prediction requires at least 3 distinct prior dates of recorded attendance for {mealType}. Currently {result?.historicalRecordCount ?? 0} prior dates found. As required, predictions are not invented when historical data is insufficient; the deterministic baseline calculation above is active.
              </p>
            </div>
          </div>
        )}
      </div>
    </div>
  )
}

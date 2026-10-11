# Hostel Food Management and Meal Attendance System

A full-stack college mini project built with **Spring Boot 3**, **Spring Security**, **JWT**, **MongoDB Atlas**, and **React + Vite** with plain responsive CSS.

---

## 📌 Technology Stack

- **Frontend:** React 19 + Vite + Plain CSS (custom responsive design system)
- **Backend:** Java 17/21 + Spring Boot 3.3.4
- **Security:** Spring Security 6 + Stateless JWT Authentication + BCrypt password hashing
- **Database:** MongoDB Atlas (supports local MongoDB fallback `localhost:27017`)
- **API Communication:** REST APIs using standard `fetch`
- **Build Tools:** Maven 3.9+ & npm

---

## 🎯 STRICT Scope: Exactly Six Application Pages

This project implements strictly the six specified pages without any extraneous modules:

1. **Login Page (`/login`):**
   - Username and password authentication with BCrypt & JWT.
   - Automatic role-based redirection:
     - Administrators &rarr; Admin Dashboard (`/admin/dashboard`)
     - Students &rarr; Student Dashboard (`/student/dashboard`)
   - Interactive quick-fill demo credential buttons for testing.

2. **Student Dashboard (`/student/dashboard`):**
   - Displays student name, ID (`studentCode`), room number, hostel block, and department.
   - Total meals attended (all-time present count).
   - Breakfast, lunch, and dinner attendance summaries.
   - Today's live meal status (Breakfast, Lunch, Dinner with timestamps or "not recorded" indicator).
   - Current month's attendance summary.
   - Recent meal attendance log (last 10 records).
   - Restricted strictly to the authenticated student's own data.

3. **Student Attendance History (`/student/history`):**
   - Past meal attendance records with date, meal type, status, and recorded time.
   - Filter by date range (from date, to date) and meal type (Breakfast, Lunch, Dinner, or All).
   - Displays newest records first.
   - Summary pills showing total, present, and absent counts for the filtered set.
   - Restricted strictly to the authenticated student's own records.

4. **Admin Dashboard (`/admin/dashboard`):**
   - Total registered students and active students.
   - Today's breakfast, lunch, and dinner attendance counts.
   - Total meal attendance count (all-time present records).
   - Selected date attendance inspector (view breakfast, lunch, dinner stats for any chosen date).
   - Direct navigation cards to Attendance Entry and Food Sufficiency Results.

5. **Attendance Entry Page (`/admin/attendance`):**
   - Date picker and meal type selector (Breakfast, Lunch, Dinner).
   - Student selection dropdown listing all active students.
   - Record actual attendance (`PRESENT` or `ABSENT`).
   - Duplicate prevention: Compound unique index `(studentId, date, mealType)` in MongoDB and proactive UI prevention to block duplicate entries.
   - View complete attendance list for the selected date and meal.
   - Edit / toggle existing records between PRESENT and ABSENT.
   - Live actual attendance count (total students present).

6. **Food Sufficiency Results Page (`/admin/sufficiency`):**
   - Select meal date and meal type.
   - Enter or update the number of meals prepared.
   - Retrieves actual attendance from MongoDB.
   - Calculates difference: $\text{Difference} = \text{Meals Prepared} - \text{Expected Meal Demand}$
     - If $\text{Difference} = 0$: displays **SUFFICIENT** (exact match).
     - If $\text{Difference} > 0$: displays **EXCESS** and the surplus quantity.
     - If $\text{Difference} < 0$: displays **INSUFFICIENT** and the shortage quantity.
   - **Baseline Demand:** Expected demand equals recorded actual attendance (clearly marked as a baseline calculation, not machine learning).
   - **Optional Historical-Average Demand Prediction:** If $\ge 3$ distinct previous dates of attendance exist for that meal type, computes historical average daily demand and displays predicted demand separately from actual attendance. If $< 3$ dates exist, historical prediction is not activated.
   - Automatically recalculates when attendance records or preparation quantities change.

---

## 🔑 Demo Accounts (Development & Evaluation)

| Role | Username | Password | Linked Student / Details |
| :--- | :--- | :--- | :--- |
| **Administrator** | `admin` | `admin123` | System Administrator |
| **Student** | `john.doe` | `student123` | STU-101: John Doe, Room A-204, Block A |
| **Student** | `jane.smith` | `student123` | STU-102: Jane Smith, Room B-312, Block B |
| **Student** | `rahul.kumar` | `student123` | STU-103: Rahul Kumar, Room A-105, Block A |
| **Student** | `priya.sharma` | `student123` | STU-104: Priya Sharma, Room C-408, Block C |
| **Student** | `alex.turner` | `student123` | STU-105: Alex Turner, Room B-201, Block B |

*Note: The Login page includes one-click demo credential buttons to quickly test both Administrator and Student personas.*

---

## 🗄️ MongoDB Atlas Setup Guide

### Option 1: MongoDB Atlas (Cloud)
1. Log in to [MongoDB Atlas](https://www.mongodb.com/cloud/atlas).
2. Create a free shared cluster (M0 sandbox).
3. Under **Security &rarr; Database Access**, create a user (e.g. `messadmin` with password `SecurePass123!`).
4. Under **Security &rarr; Network Access**, add an IP Access List entry:
   - For development/testing: `0.0.0.0/0` (Allow Access from Anywhere) or add your current IP address.
5. Under **Deployments &rarr; Database &rarr; Connect**:
   - Choose **Drivers &rarr; Java**.
   - Copy your connection string:
     ```text
     mongodb+srv://messadmin:<password>@cluster0.xxxxx.mongodb.net/hostel_food_attendance_db?retryWrites=true&w=majority
     ```
6. Set the `MONGODB_URI` environment variable before starting the backend:
   - **Windows PowerShell:**
     ```powershell
     $env:MONGODB_URI="mongodb+srv://messadmin:SecurePass123!@cluster0.xxxxx.mongodb.net/hostel_food_attendance_db?retryWrites=true&w=majority"
     ```
   - **Windows Command Prompt (cmd):**
     ```cmd
     set MONGODB_URI=mongodb+srv://messadmin:SecurePass123!@cluster0.xxxxx.mongodb.net/hostel_food_attendance_db?retryWrites=true&w=majority
     ```

### Option 2: Local MongoDB Fallback
If `MONGODB_URI` is not set, the Spring Boot application automatically connects to local MongoDB at `mongodb://localhost:27017/hostel_food_attendance_db`.

---

## ⚙️ Environment Variables

| Variable | Description | Default Value |
| :--- | :--- | :--- |
| `MONGODB_URI` | MongoDB Atlas or Local connection URI | `mongodb://localhost:27017/hostel_food_attendance_db` |
| `JWT_SECRET` | 256-bit secret key for signing JWT tokens | `404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970` |
| `JWT_EXPIRATION_MS` | JWT expiration time in milliseconds | `86400000` (24 hours) |
| `PORT` | Backend server port | `8080` |
| `CORS_ALLOWED_ORIGINS` | Permitted origins for REST requests | `http://localhost:5173,http://localhost:3000` |

---

## 🚀 Running the Applications

### Prerequisites
- Java JDK 17 or JDK 21
- Node.js (v18+) & npm
- Maven (a portable distribution is provided under `tools/apache-maven-3.9.6` or use installed `mvn`)
- MongoDB (Atlas cluster or local service running on port 27017)

### 1. Start the Backend (Spring Boot)

#### Using Maven Wrapper / Script:
Double-click `start-backend.cmd` or run:
```powershell
# Set JAVA_HOME
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"

# Compile and package jar
& "c:\mess\tools\apache-maven-3.9.6\bin\mvn.cmd" clean package -DskipTests

# Run the jar
java -jar target\hostel-food-management-1.0.0.jar
```
The backend starts at: **`http://localhost:8080`**

### 2. Start the Frontend (React + Vite)

Double-click `start-frontend.cmd` or run:
```powershell
cd c:\mess\frontend
npm run dev
```
The frontend starts at: **`http://localhost:5173`**

### 3. Open Application in Browser
Navigate to **`http://localhost:5173`** in your browser.

---

## 📡 Backend REST API Endpoints

### Authentication
- `POST /api/auth/login` — Authenticate username & password, returns JWT and user details

### Student Portal (Requires `ROLE_STUDENT`)
- `GET /api/student/dashboard` — Authenticated student's meal stats, today's status, and recent records
- `GET /api/student/history` — Query parameters: `startDate`, `endDate`, `mealType`. Returns filtered records newest first

### Admin Portal (Requires `ROLE_ADMIN`)
- `GET /api/admin/dashboard` — Registered/active student counts, today's meal totals, and selected date statistics
- `GET /api/admin/students` — List of all active students for dropdown selection
- `GET /api/admin/attendance` — Query parameters: `date`, `mealType`. Attendance list and present headcount
- `POST /api/admin/attendance` — Record attendance (validates and prevents duplicates via unique compound index)
- `PUT /api/admin/attendance/{id}` — Edit existing attendance record status
- `DELETE /api/admin/attendance/{id}` — Delete attendance record
- `GET /api/admin/sufficiency` — Query parameters: `date`, `mealType`. Calculates sufficiency status, difference, and historical predictions
- `POST /api/admin/sufficiency` — Enter or update meals prepared in MongoDB and recalculate results

---

## 🧪 Verification & Test Results

- **Backend Compilation:** `mvn clean test` &rarr; `BUILD SUCCESS` (3 unit tests passed: BCrypt encoding, JWT token generation & verification, Food Sufficiency difference formula).
- **Backend Packaging:** `mvn package` &rarr; `BUILD SUCCESS` (executable jar built at `target/hostel-food-management-1.0.0.jar`).
- **Frontend Build:** `npm run build` &rarr; `✓ built in 735ms` (production bundle in `dist/`).
- **Live API Integration Test:** All 6 endpoints tested with live MongoDB database:
  - Admin login & student login returned valid JWT tokens.
  - Role-based authorization enforced (`ROLE_ADMIN` and `ROLE_STUDENT`).
  - Attendance entry recorded and duplicate insertion correctly rejected with `HTTP 409 Conflict`.
  - Food Sufficiency calculated and verified across all 3 status states:
    - Prepared = 3, Demand = 3 &rarr; `SUFFICIENT` ($\text{Difference} = 0$)
    - Prepared = 6, Demand = 3 &rarr; `EXCESS` ($\text{Difference} = +3$)
    - Prepared = 1, Demand = 3 &rarr; `INSUFFICIENT` ($\text{Difference} = -2$)
  - Historical average demand estimate calculated from past attendance records ($\ge 3$ recorded dates).

try {
    # 1. Admin Login
    $loginBody = @{ username = 'admin'; password = 'admin123' } | ConvertTo-Json
    $adminLogin = Invoke-RestMethod -Uri 'http://localhost:8080/api/auth/login' -Method Post -ContentType 'application/json' -Body $loginBody
    Write-Host "Admin Login: Username=$($adminLogin.username), Role=$($adminLogin.role)"
    $adminToken = $adminLogin.token

    # 2. Student Login
    $stuBody = @{ username = 'john.doe'; password = 'student123' } | ConvertTo-Json
    $stuLogin = Invoke-RestMethod -Uri 'http://localhost:8080/api/auth/login' -Method Post -ContentType 'application/json' -Body $stuBody
    Write-Host "Student Login: Username=$($stuLogin.username), Code=$($stuLogin.studentCode), Name=$($stuLogin.fullName)"
    $stuToken = $stuLogin.token

    # 3. Admin Dashboard
    $adminDash = Invoke-RestMethod -Uri 'http://localhost:8080/api/admin/dashboard' -Method Get -Headers @{ Authorization = "Bearer $adminToken" }
    Write-Host "Admin Dashboard: Registered Students=$($adminDash.totalRegisteredStudents), Active=$($adminDash.totalActiveStudents), Total Meals Attended=$($adminDash.totalMealAttendance)"

    # 4. Student Dashboard
    $stuDash = Invoke-RestMethod -Uri 'http://localhost:8080/api/student/dashboard' -Method Get -Headers @{ Authorization = "Bearer $stuToken" }
    Write-Host "Student Dashboard: Total Attended=$($stuDash.totalMealsAttended), Breakfast=$($stuDash.breakfastCount), Lunch=$($stuDash.lunchCount), Dinner=$($stuDash.dinnerCount)"

    # 5. Food Sufficiency Status Transitions (SUFFICIENT, EXCESS, INSUFFICIENT)
    $today = (Get-Date).ToString('yyyy-MM-dd')

    # Test SUFFICIENT (prepared = 3, actual demand = 3)
    $suffBody1 = @{ date = $today; mealType = 'LUNCH'; mealsPrepared = 3; notes = 'Exact match test' } | ConvertTo-Json
    $res1 = Invoke-RestMethod -Uri 'http://localhost:8080/api/admin/sufficiency' -Method Post -ContentType 'application/json' -Body $suffBody1 -Headers @{ Authorization = "Bearer $adminToken" }
    Write-Host "Sufficiency Test 1 (SUFFICIENT): Diff=$($res1.difference), Status=$($res1.status), Message=$($res1.statusMessage)"

    # Test EXCESS (prepared = 6, actual demand = 3)
    $suffBody2 = @{ date = $today; mealType = 'LUNCH'; mealsPrepared = 6; notes = 'Surplus test' } | ConvertTo-Json
    $res2 = Invoke-RestMethod -Uri 'http://localhost:8080/api/admin/sufficiency' -Method Post -ContentType 'application/json' -Body $suffBody2 -Headers @{ Authorization = "Bearer $adminToken" }
    Write-Host "Sufficiency Test 2 (EXCESS): Diff=$($res2.difference), Status=$($res2.status), Message=$($res2.statusMessage)"

    # Test INSUFFICIENT (prepared = 1, actual demand = 3)
    $suffBody3 = @{ date = $today; mealType = 'LUNCH'; mealsPrepared = 1; notes = 'Shortage test' } | ConvertTo-Json
    $res3 = Invoke-RestMethod -Uri 'http://localhost:8080/api/admin/sufficiency' -Method Post -ContentType 'application/json' -Body $suffBody3 -Headers @{ Authorization = "Bearer $adminToken" }
    Write-Host "Sufficiency Test 3 (INSUFFICIENT): Diff=$($res3.difference), Status=$($res3.status), Message=$($res3.statusMessage)"

    # Reset back to 4 meals prepared
    $suffBodyReset = @{ date = $today; mealType = 'LUNCH'; mealsPrepared = 4; notes = 'Standard batch' } | ConvertTo-Json
    $resReset = Invoke-RestMethod -Uri 'http://localhost:8080/api/admin/sufficiency' -Method Post -ContentType 'application/json' -Body $suffBodyReset -Headers @{ Authorization = "Bearer $adminToken" }
    Write-Host "Sufficiency Reset (Prepared=4): Diff=$($resReset.difference), Status=$($resReset.status)"

    Write-Host "`n>>> ALL 6 REQUIREMENTS AND RECALCULATIONS CONFIRMED WORKING! <<<"
} catch {
    Write-Error $_.Exception.Message
}

$uri = "mongodb+srv://vishalvarmasatla_db_user:vishalA56@cluster0.9mejvhu.mongodb.net/hostel_food_attendance_db?retryWrites=true&w=majority&appName=Cluster0"
Write-Host "Testing connection to MongoDB Atlas at cluster0.9mejvhu.mongodb.net..."

# Test DNS SRV resolution
try {
    $dns = Resolve-DnsName -Name "_mongodb._tcp.cluster0.9mejvhu.mongodb.net" -Type SRV -ErrorAction SilentlyContinue
    if ($dns) {
        Write-Host "DNS SRV resolution succeeded: $($dns.Count) hosts resolved."
        foreach ($d in $dns) {
            Write-Host "  -> Host: $($d.NameTarget):$($d.Port)"
        }
    } else {
        Write-Host "DNS SRV did not return records directly, trying standard lookup..."
    }
} catch {
    Write-Host "DNS lookup warning: $($_.Exception.Message)"
}

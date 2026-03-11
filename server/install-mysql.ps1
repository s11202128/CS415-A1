# MySQL Installation Script for Windows
# This script will help you set up MySQL for the Bank of Fiji application

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "MySQL Installation Helper for Windows" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check if MySQL is already installed
Write-Host "Checking for MySQL installation..." -ForegroundColor Yellow
$mysqlPath = "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"

if (Test-Path $mysqlPath) {
    Write-Host "✓ MySQL is already installed at: $mysqlPath" -ForegroundColor Green
    & $mysqlPath --version
} else {
    Write-Host "✗ MySQL is not found in default location" -ForegroundColor Red
    Write-Host ""
    Write-Host "📥 Please install MySQL in one of these ways:" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Option 1: MySQL Community Edition (Recommended)" -ForegroundColor Yellow
    Write-Host "  1. Download from: https://dev.mysql.com/downloads/mysql/" -ForegroundColor White
    Write-Host "  2. Choose 'Windows (x86, 64-bit)' version" -ForegroundColor White
    Write-Host "  3. Run installer and follow setup wizard" -ForegroundColor White
    Write-Host "  4. Choose 'Developer Default' or 'Server only'" -ForegroundColor White
    Write-Host "  5. Configure MySQL as 'Windows Service'" -ForegroundColor White
    Write-Host "  6. Set port to 3306 (default)" -ForegroundColor White
    Write-Host "  7. Root password: leave empty or set one (must match config)" -ForegroundColor White
    Write-Host ""
    Write-Host "Option 2: Docker (Advanced)" -ForegroundColor Yellow
    Write-Host "  docker run --name bof-mysql -e MYSQL_ROOT_PASSWORD=root -p 3306:3306 mysql:8" -ForegroundColor White
    Write-Host ""
    Write-Host "Option 3: Via Chocolatey" -ForegroundColor Yellow
    Write-Host "  choco install mysql" -ForegroundColor White
    Write-Host ""
    exit 1
}

Write-Host ""
Write-Host "Checking MySQL service status..." -ForegroundColor Yellow

# If MySQL is installed, check service
$service = Get-Service | Where-Object { $_.Name -like "*MySQL*" -or $_.Name -eq "MySQL80" }

if ($service) {
    Write-Host "Found service: $($service.Name)" -ForegroundColor Green
    Write-Host "Service status: $($service.Status)" -ForegroundColor Green
    
    if ($service.Status -ne "Running") {
        Write-Host ""
        Write-Host "Starting MySQL service..." -ForegroundColor Yellow
        try {
            Start-Service -Name $service.Name
            Start-Sleep -Seconds 3
            Write-Host "✓ MySQL service started successfully" -ForegroundColor Green
        } catch {
            Write-Host "✗ Failed to start MySQL service: $_" -ForegroundColor Red
            exit 1
        }
    } else {
        Write-Host "✓ MySQL service is already running" -ForegroundColor Green
    }
} else {
    Write-Host "⚠ MySQL service not found. Installation may not be complete." -ForegroundColor Yellow
    exit 1
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Setup Complete!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Next steps:" -ForegroundColor Yellow
Write-Host "  1. cd C:\Users\LENOVO\Desktop\CS415-A1\server" -ForegroundColor White
Write-Host "  2. npm run dev" -ForegroundColor White
Write-Host ""
Write-Host "The database will be created automatically!" -ForegroundColor Green

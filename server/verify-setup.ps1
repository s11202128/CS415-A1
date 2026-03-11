#!/usr/bin/env powershell
# Verify MySQL setup is correct

Write-Host "================================================" -ForegroundColor Cyan
Write-Host "MySQL Setup Verification for Bank of Fiji" -ForegroundColor Cyan
Write-Host "================================================" -ForegroundColor Cyan
Write-Host ""

$allGood = $true

# Check 1: MySQL installed
Write-Host "[1/4] Checking MySQL installation..." -ForegroundColor Yellow
$mysqlExtPath = (Get-Command mysql -ErrorAction SilentlyContinue).Source
if ($mysqlExtPath) {
    Write-Host "✓ MySQL found at: $mysqlExtPath" -ForegroundColor Green
} else {
    $commonPath = "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
    if (Test-Path $commonPath) {
        Write-Host "✓ MySQL found at: $commonPath" -ForegroundColor Green
    } else {
        Write-Host "✗ MySQL not found in PATH" -ForegroundColor Red
        Write-Host "  Please install MySQL from: https://dev.mysql.com/downloads/mysql/" -ForegroundColor Yellow
        $allGood = $false
    }
}

# Check 2: MySQL Service
Write-Host "[2/4] Checking MySQL service..." -ForegroundColor Yellow
$service = Get-Service | Where-Object { $_.Name -eq "MySQL80" -or $_.Name -like "*MySQL*" } | Select-Object -First 1
if ($service) {
    if ($service.Status -eq "Running") {
        Write-Host "✓ MySQL service found and RUNNING" -ForegroundColor Green
    } else {
        Write-Host "⚠ MySQL service found but STOPPED" -ForegroundColor Yellow
        Write-Host "  Starting service..." -ForegroundColor White
        try {
            Start-Service -Name $service.Name
            Start-Sleep -Seconds 2
            Write-Host "✓ Service started" -ForegroundColor Green
        } catch {
            Write-Host "✗ Failed to start service: $_" -ForegroundColor Red
            $allGood = $false
        }
    }
} else {
    Write-Host "✗ MySQL service not found" -ForegroundColor Red
    Write-Host "  Make sure MySQL is installed as Windows Service" -ForegroundColor Yellow
    $allGood = $false
}

# Check 3: Node.js dependencies
Write-Host "[3/4] Checking Node.js dependencies..." -ForegroundColor Yellow
$packageJsonPath = ".\package.json"
if (Test-Path $packageJsonPath) {
    $content = Get-Content $packageJsonPath | ConvertFrom-Json
    if ($content.dependencies.sequelize -and $content.dependencies.mysql2) {
        Write-Host "✓ sequelize and mysql2 packages found" -ForegroundColor Green
    } else {
        Write-Host "⚠ Missing packages. Run: npm install" -ForegroundColor Yellow
        $allGood = $false
    }
} else {
    Write-Host "✗ package.json not found" -ForegroundColor Red
    $allGood = $false
}

# Check 4: Database files
Write-Host "[4/4] Checking database files..." -ForegroundColor Yellow
$dbFiles = @(
    "src/config/database.js",
    "src/database.js",
    "src/store-mysql.js",
    "src/models/Customer.js",
    "src/models/Account.js",
    "src/models/Transaction.js"
)
$missingFiles = @()
foreach ($file in $dbFiles) {
    if (-not (Test-Path $file)) {
        $missingFiles += $file
    }
}
if ($missingFiles.Count -eq 0) {
    Write-Host "✓ All database files present" -ForegroundColor Green
} else {
    Write-Host "✗ Missing files:" -ForegroundColor Red
    $missingFiles | ForEach-Object { Write-Host "  - $_" -ForegroundColor Red }
    $allGood = $false
}

Write-Host ""
Write-Host "================================================" -ForegroundColor Cyan
if ($allGood) {
    Write-Host "✓ All checks passed! Ready to run:" -ForegroundColor Green
    Write-Host "  npm run dev" -ForegroundColor White
} else {
    Write-Host "⚠ Some checks failed. See above for details." -ForegroundColor Yellow
    Write-Host "  https://dev.mysql.com/downloads/mysql/" -ForegroundColor Cyan
}
Write-Host "================================================" -ForegroundColor Cyan

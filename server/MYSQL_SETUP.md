# MySQL Database Setup Guide

## 📋 Automated Setup

The backend now uses **MySQL with Sequelize ORM**. Most of the setup is automated, but you need to complete a few manual steps.

---

## ✅ What Was Automated For You

1. ✓ **Installed dependencies**: `sequelize` and `mysql2` packages
2. ✓ **Created database configuration**: `src/config/database.js` with MySQL connection
3. ✓ **Created database models**: 
   - `Customer` - User accounts
   - `Account` - Bank accounts
   - `Transaction` - All transactions
   - `Bill` - Bill payments
   - `Investment` - Investment products
   - `Loan` - Loan applications
   - `OtpVerification` - OTP for high-value transfers

4. ✓ **Refactored backend code**: 
   - `src/store-mysql.js` - All business logic updated to use MySQL
   - `src/database.js` - Database initialization and seeding
   - `src/index.js` - Updated to initialize database on startup

---

## 🔧 Manual Steps You Need to Complete

### Step 1: Verify MySQL is Running
```powershell
mysql --version
```
If MySQL is not installed, **download and install from**: https://dev.mysql.com/downloads/mysql/

**On Windows**:
- Download MySQL installer
- Run installer and choose "Developer Default" installation
- Configure as Windows Service (automatically starts on reboot)

**Check if running**:
```powershell
Get-Service | Where-Object {$_.Name -like "*mysql*"}
```

### Step 2: Start MySQL Service (if not running)
```powershell
# Windows
net start MySQL80

# Or using PowerShell
Start-Service MySQL80
```

### Step 3: Login to MySQL and Create Database User (Optional)
If you want a custom username/password instead of root:

```bash
mysql -u root -p
# Enter password (default is empty, just press Enter)

# Create a new user
CREATE USER 'bof_user'@'localhost' IDENTIFIED BY 'secure_password';
GRANT ALL PRIVILEGES ON bof_banking_db.* TO 'bof_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

Then update `src/config/database.js`:
```javascript
const sequelize = new Sequelize('bof_banking_db', 'bof_user', 'secure_password', {
  host: 'localhost',
  dialect: 'mysql',
  port: 3306,
  logging: false,
});
```

---

## 🚀 Starting the Backend

```powershell
cd C:\Users\LENOVO\Desktop\CS415-A1\server
npm run dev
```

**Expected output**:
```
✓ Database tables synchronized
✓ Database seeded with initial data
BoF Banking API running on http://localhost:4000
```

---

## 📊 Database Structure

### Tables Created Automatically

**customers** table:
- id (UUID, primary key)
- fullName
- email
- mobile
- password (hashed with bcryptjs)
- status
- createdAt, updatedAt

**accounts** table:
- id (UUID)
- customerId (FK)
- accountNumber
- accountType (Savings, Checking, etc.)
- balance
- currency (FJD)
- status
- createdAt, updatedAt

**transactions** table:
- id (UUID)
- accountId (FK)
- type (debit, credit)
- amount
- description
- status
- balanceAfter
- createdAt, updatedAt

**bills** table:
- id (UUID)
- customerId (FK)
- billType
- amount
- dueDate
- status
- description
- createdAt, updatedAt

**investments**, **loans**, **otp_verifications** tables follow similar patterns.

---

## 🔍 Verify Everything Works

Once backend is running:

```powershell
# Test API
curl http://localhost:4000/api/health

# Expected response:
# {"status":"ok"}
```

---

## 📝 Important Notes

1. **Database is created automatically** on first startup
2. **Tables are created automatically** using Sequelize `sync({ alter: true })`
3. **Sample data is seeded** on first run:
   - 3 sample customers (Litia, Aman, Mere)
   - 3 sample accounts with starting balances
4. **Passwords are hashed** with bcryptjs (salt rounds: 10)
5. **OTP expires in 5 minutes** for high-value transfers (>= 1000 FJD)

---

## 🛠️ Troubleshooting

### Error: "connect ECONNREFUSED 127.0.0.1:3306"
**Solution**: MySQL is not running. Start it:
```powershell
net start MySQL80
```

### Error: "ER_ACCESS_DENIED_FOR_USER 'root'@'localhost'"
**Solution**: Wrong password. Update `src/config/database.js` with correct credentials.

### Error: "ER_NO_DB_ERROR"
**Solution**: Database wasn't created. Delete node_modules/.sequelize and restart:
```powershell
rm -r server/node_modules/.sequelize
npm run dev
```

### Want to see SQL queries?
Change logging in `src/config/database.js`:
```javascript
logging: console.log  // Instead of: logging: false
```

---

## 🎯 Next Steps

1. Start your backend: `npm run dev` (from server folder)
2. Start your frontend: `npm run dev` (from client folder)
3. Open browser: `http://localhost:5173`
4. Test registration and login with your new database!

---

## 📦 Switching Back to In-Memory (Optional)

If you need to revert to in-memory database, restore `store.js` from git:
```powershell
git checkout src/store.js
```

And update `authRoutes.js` and `apiRoutes.js` to use old imports.

---

**Questions?** Check MySQL documentation: https://dev.mysql.com/doc/

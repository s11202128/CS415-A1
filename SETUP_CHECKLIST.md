# ✅ MySQL Setup Checklist

## 📋 What Was Automated For You

Below are all the tasks that have been completed automatically:

- [x] Installed `sequelize` npm package
- [x] Installed `mysql2` npm package  
- [x] Created database configuration file (`src/config/database.js`)
- [x] Created 7 database models:
  - [x] Customer
  - [x] Account
  - [x] Transaction
  - [x] Bill
  - [x] Investment
  - [x] Loan
  - [x] OtpVerification
- [x] Created model index file with relationships
- [x] Created database initialization script (`src/database.js`)
- [x] Refactored business logic to use MySQL (`src/store-mysql.js`)
- [x] Updated server startup (`src/index.js`)
- [x] Generated documentation files:
  - [x] MYSQL_SETUP.md (detailed guide)
  - [x] install-mysql.ps1 (helper script)
  - [x] README_DATABASE_MIGRATION.md (quick start)
  - [x] MIGRATION_DETAILS.md (technical details)

---

## 🔧 What YOU Need to Do (3 Steps)

### ✋ STEP 1: Install MySQL
**Status**: ⏳ PENDING

Choose ONE method:

**Option A: Download installer (Recommended)**
- Go to: https://dev.mysql.com/downloads/mysql/
- Download "Windows (x86, 64-bit)" version
- Run installer
- Choose "Developer Default" installation
- Configure as Windows Service
- Keep port as 3306
- Root password: leave empty OR remember it

**Option B: Run helper script**
```powershell
cd C:\Users\LENOVO\Desktop\CS415-A1\server
.\install-mysql.ps1
```

**Option C: Docker**
```bash
docker run --name bof-mysql -e MYSQL_ROOT_PASSWORD=root -p 3306:3306 mysql:8
```

**Option D: Chocolatey**
```powershell
choco install mysql
```

### ✋ STEP 2: Start MySQL Service
**Status**: ⏳ PENDING

```powershell
# Start MySQL
net start MySQL80

# Verify it's running
Get-Service MySQL80
```

### ✋ STEP 3: Start Your Application
**Status**: ⏳ PENDING

Once MySQL is running:

```powershell
# Terminal 1: Backend
cd C:\Users\LENOVO\Desktop\CS415-A1\server
npm run dev

# Terminal 2: Frontend
cd C:\Users\LENOVO\Desktop\CS415-A1\client
npm run dev
```

Expected output:
```
✓ Database tables synchronized
✓ Database seeded with initial data
BoF Banking API running on http://localhost:4000
```

---

## 📊 File Structure

```
C:\Users\LENOVO\Desktop\CS415-A1\
├── server/
│   ├── src/
│   │   ├── config/
│   │   │   └── database.js          ✨ NEW
│   │   ├── models/                   ✨ NEW
│   │   │   ├── Customer.js
│   │   │   ├── Account.js
│   │   │   ├── Transaction.js
│   │   │   ├── Bill.js
│   │   │   ├── Investment.js
│   │   │   ├── Loan.js
│   │   │   ├── OtpVerification.js
│   │   │   └── index.js
│   │   ├── database.js               ✨ NEW
│   │   ├── store-mysql.js            ✨ NEW
│   │   ├── index.js                  ✏️ MODIFIED
│   │   ├── routes/
│   │   │   ├── authRoutes.js
│   │   │   └── apiRoutes.js
│   │   └── store.js                  (old - not used)
│   ├── MYSQL_SETUP.md                ✨ NEW
│   ├── install-mysql.ps1             ✨ NEW
│   └── package.json
├── client/
│   └── ... (no changes)
├── README_DATABASE_MIGRATION.md       ✨ NEW
└── MIGRATION_DETAILS.md              ✨ NEW
```

---

## 🎯 Quick Reference

| Step | Action | Command |
|------|--------|---------|
| 1 | Install MySQL | Download from mysql.com OR run `install-mysql.ps1` |
| 2 | Start MySQL | `net start MySQL80` |
| 3 | Start Backend | `npm run dev` (server folder) |
| 4 | Start Frontend | `npm run dev` (client folder) |
| 5 | Access App | Open http://localhost:5173 |

---

## ⚡ What Happens Automatically

When you run `npm run dev` in the server folder:

1. ✅ Connects to MySQL on localhost:3306
2. ✅ Creates database `bof_banking_db` (if not exists)
3. ✅ Creates all 7 tables (if not exists)
4. ✅ Checks if data already seeded
5. ✅ If first run: seeds sample customers/accounts
6. ✅ Starts Express server on port 4000

**No manual SQL needed!** 🎉

---

## 💾 What Gets Stored

After first startup, your MySQL database will have:

```
bof_banking_db/
├── customers (3 sample records)
├── accounts (3 sample records)  
├── transactions (created when you test)
├── bills
├── investments
├── loans
└── otp_verifications
```

All data persists between server restarts! ✅

---

## 🚨 If Something Goes Wrong

**"connect ECONNREFUSED"**
→ MySQL not running: `net start MySQL80`

**"ER_ACCESS_DENIED"** 
→ Wrong password in config. Check src/config/database.js

**"ER_NO_DB_ERROR"**
→ Database not created. Let server restart, it will auto-create.

**"mysql command not found"**
→ MySQL not in PATH. Reinstall MySQL or add to PATH manually.

---

## ✅ Success Checklist

Once everything is running, verify:

- [ ] Backend output shows "Database tables synchronized"
- [ ] Backend output shows "Database seeded with initial data"  
- [ ] Frontend shows login page on http://localhost:5173
- [ ] You can register a new account
- [ ] You can login with your account
- [ ] Transfers and bills work
- [ ] Data persists after refresh

---

## 📚 Documentation

For more details, read:
- **Quick Start**: README_DATABASE_MIGRATION.md
- **Setup Guide**: server/MYSQL_SETUP.md
- **Technical Details**: MIGRATION_DETAILS.md
- **Installation Help**: server/install-mysql.ps1

---

## 🎊 You're All Set!

**Next action**: Install MySQL using any of the 4 methods above, then run the 3 startup commands.

Questions? Check the documentation files for detailed troubleshooting.

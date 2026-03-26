# Mobile Migration Plan (Android Native)

## Goal
Migrate the existing web app UI to Android native while reusing:
- Existing Node/Express backend
- Existing MySQL database
- Existing business rules (OTP, transfers, bills, loan workflows)

No mobile local database is introduced. Mobile accesses data only through secure REST APIs.

## Target Architecture

### 3-Tier Mapping
1. Presentation Layer:
- Android Compose screens and state rendering

2. Business Logic Layer:
- Backend services/controllers in server/src/services and server/src/controllers
- Android ViewModel orchestration for UI use-cases

3. Data Access Layer:
- Backend repositories/models (Sequelize/MySQL)
- Android Retrofit API client (remote API only)

### MVC/MVVM Mapping
- Model:
  - Backend models (server/src/models)
  - Android domain/data models
- View:
  - Android Compose UI
- Controller (MVVM adaptation):
  - Android ViewModel

## Suggested Server Folder Structure

server/src/
  controllers/
    authController.js
  services/
    authService.js
  repositories/
    customerRepository.js
    accountRepository.js
    transactionRepository.js
  routes/
    authRoutes.js
    apiRoutes.js
  models/
  middleware/
  utils/

## Suggested Android Folder Structure

mobile-android/
  app/src/main/java/com/bof/mobile/
    data/
      remote/
        ApiService.kt
        NetworkModule.kt
      repository/
        AuthRepository.kt
        DashboardRepository.kt
    model/
      AuthModels.kt
      DashboardModels.kt
      ApiResult.kt
    viewmodel/
      AuthViewModel.kt
      DashboardViewModel.kt
    ui/
      auth/
        LoginScreen.kt
        RegisterScreen.kt
      dashboard/
        DashboardScreen.kt

## Step-by-Step Plan
1. Backend refactor foundation
- Keep behavior unchanged
- Move auth route logic to controller/service
- Continue with transfer/account/billing controllers/services

2. API contract hardening
- Standardize errors and status codes
- Add pagination/filter support for transaction APIs
- Add aggregate endpoint for account details

3. Android app bootstrap
- Set up Kotlin + Compose + Retrofit + Coroutines
- Implement login/register flows first

4. Feature migration
- Dashboard
- Accounts + transactions
- Transfer + OTP
- Bill payments
- Statements and loans

5. Validation and rollout
- API contract tests
- Mobile integration tests
- staged release

## Commit Strategy
1. refactor(server): extract auth controller and service
2. feat(api): secure and paginate transaction endpoint
3. feat(api): add account details aggregate endpoint
4. feat(api): add recipient search and biller directory endpoints
5. feat(android): scaffold native app architecture (mvvm)
6. feat(android): implement auth ui + api integration
7. feat(android): implement dashboard core flow
8. docs(api): add method-level documentation

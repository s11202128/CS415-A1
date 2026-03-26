# Mobile API Additions

## GET /api/transactions
Returns transactions for an account with optional filters and pagination.

### Query Parameters
- accountId (number, optional): Account id
- accountNumber (string, optional): 12-digit account number
- type (string, optional): debit or credit
- fromDate (ISO string, optional)
- toDate (ISO string, optional)
- minAmount (number, optional)
- maxAmount (number, optional)
- paginated (boolean, optional)
- page (number, optional)
- pageSize (number, optional)

### Response
- Non-paginated: array of transaction rows
- Paginated: object with items, page, pageSize, total, totalPages

## GET /api/accounts/:id/details
Returns account details and recent transactions in one request.

### Query Parameters
- limit (number, optional): max rows, default 20

### Response
- account: account object
- customer: owner summary
- transactions: recent transaction array

## GET /api/recipients/search
Search destination accounts for transfer.

### Query Parameters
- q (string, required): minimum length 3

### Response
- Array of recipient objects: accountId, accountNumber, accountHolder, customerId

## GET /api/billers
Returns supported billers for bill payment UI.

### Response
- Array of biller objects: code, name

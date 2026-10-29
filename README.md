# Apex Banking Enterprise Management System

A comprehensive, production-grade Banking Management System web application built using **ONLY core Java (JDK 17+)**, with zero external dependencies, no frameworks (no Spring, no Servlets container), no Maven/Gradle, and no external database.

---

## Complete Feature Matrix

### 1. Staff & Administration Portal (`/admin`)
- **Staff Roles**: `ADMIN`, `MANAGER`, `TELLER`, and `CUSTOMER`.
- **Create Admin & Staff**: Create new Administrators, Managers, and Tellers with custom permissions directly from the portal.
- **Account Lockout & Unlock**: Automatically locks accounts after 3 failed login attempts; Administrators can lock or unlock users at any time.
- **Interactive Overview**: Live system capital, total loans, active customers, and audit event metrics.
- **SVG Analytics Chart**: Interactive Deposits vs. Withdrawals bar chart rendered natively with SVG (no JavaScript charting libraries required).
- **KYC Verification Workflow**: Review customer identity document submissions (Passport, Driver License, National ID) with 1-click Approve / Reject.
- **Account Approval Queue**: Review and approve newly submitted account applications.
- **Loans Management**: Review, disburse, or decline customer loan applications.
- **Dispute & Chargeback Approvals**: Review customer transaction disputes with automated fund reversal and refund crediting.
- **Branch Management**: Directory of branch networks with IFSC-style codes (e.g. `APEX0001` - Downtown Central, `APEX0002` - Financial District).
- **Employee Directory**: Track staff members, departments, roles, and salaries.
- **Automated Interest Run**: Trigger monthly batch interest calculation and crediting across all savings accounts.
- **Fraud Monitoring**: Automated system flagging for high-value transactions exceeding ₹5,000.00.
- **Security Audit Log**: Immutable record of every administrative and authentication event.

### 2. Accounts & Deposit Types (OOP Hierarchy)
- **Base Abstract Account**: Encapsulates account number, holder, joint holder, balance, status, nominee details, branch code, and opening date.
- **Savings Account**: 4.0% annual interest with automatic monthly interest crediting; minimum balance of ₹500.00.
- **Current Account**: ₹1,000.00 Overdraft facility allowing business debit beyond zero balance.
- **Fixed Deposit (FD)**: Term-based lump sum deposit (12, 24, 36 months) with guaranteed interest up to 8.0% p.a. and calculated maturity amounts.
- **Recurring Deposit (RD)**: Monthly disciplined installment deposit with tracked tenure and maturity payoff.
- **Joint Accounts & Nominees**: Configurable secondary account holder and nominee registration (name, relationship).

### 3. Loans Module (OOP)
- **Loan Types**: Home Loan (8.5% p.a.), Car Loan (9.5% p.a.), and Personal Loan (12.5% p.a.).
- **Amortization & EMI**: Standard mathematical EMI formula calculation with tenure selection.
- **Disbursement & Repayment**: Once approved by staff, funds disburse automatically into the customer's account; customers can repay monthly EMIs directly with PIN verification.

### 4. Transfers & Payments Hub
- **Direct Bank Transfers**: Instant transfers with 2-step PIN authorization.
- **High-Value OTP Verification**: Transfers exceeding ₹1,000.00 trigger a simulated 6-digit one-time password on screen.
- **Beneficiary Management**: Save frequent payees with nicknames for fast 1-click payments.
- **Utility & Bill Payments**: Simulated biller payments for Electricity, Mobile Recharge, Satellite DTH TV, and Water.
- **Standing Instructions**: Automate daily, weekly, or monthly recurring transfers between accounts.
- **Currency Converter**: Real-time fixed rate conversion table for USD, EUR, GBP, INR, JPY, and CAD.
- **UPI Instant Pay**: Personalized Virtual Payment Address (`username@apex`) and QR code visualization with customizable daily UPI limits.

### 5. Cards & Cheques Management
- **Virtual Debit Cards**: Visual EMV chip card display with cardholder name, expiry, CVV, and network branding.
- **Card Controls**: Adjust daily card spending limits on the fly; freeze or unlock cards instantly.
- **Chequebook Services**: Request 25, 50, or 100-leaf chequebooks with real-time fulfillment status tracking.

### 6. Statements, Disputes & Analytics
- **Mini-Statements & Full Ledger**: View recent or historical transactions.
- **Advanced Filtering**: Filter by transaction type, expense category, or date range.
- **Statement Downloads**: Export account statement as a CSV file or print via formatted printable layout.
- **Transaction Disputes**: Customers can flag any transaction for investigation; administrators can approve full chargeback refunds.

### 7. Customer Hub & Experience
- **Profile Management**: Update email, phone number, and residential address.
- **Password Reset**: Self-service recovery using security questions and answers.
- **Savings Goals Tracker**: Define savings targets (e.g. "Vacation Fund", "New Car") with live progress percentage bars.
- **Monthly Spending Breakdown**: Categorized expense analysis (Bills, Shopping, Food, Transfers).
- **Customer Helpdesk**: Raise support tickets and receive staff replies.
- **Feedback & Rating**: Submit 1 to 5-star ratings and feedback.
- **Notifications & Inbox**: Instant alerts for credits, debits, loan approvals, and security updates.
- **Neobank Express Professional UI**: High-contrast modern neo-bank aesthetic (Revolut/N26 style) featuring an interactive Total Liquid Capital Hero Card, floating quick-actions bar, emerald & cyan telemetry meters, and instant Light/Dark mode toggling.

### 8. Live Demo Experience & Quick User Switcher
- **Sticky Top Bar Quick Switcher**: 1-click pills to switch instantly between all personas (`👑 Admin`, `👔 Manager`, `💼 Teller`, `👤 John`, `👤 Alice`) from any page without logging out or re-entering credentials. Active persona is highlighted with dynamic status.
- **1-Click Live Demo Sign-In**: Quick access tiles on `/login` to sign in instantly with one tap without typing passwords.
- **Admin Impersonation**: `⚡ Switch` action button for each user in the Registered User Directory (`/admin?tab=users`) to test newly registered customer or staff profiles on the fly.
- **Instant Demo State Reset**: Dedicated `🔄 Reset` button in the top bar to restore all default accounts, balances, loans, and audit records in one click during evaluation.

---

## OOP Architecture

- **Custom Exceptions**:
  - `InsufficientFundsException`
  - `AccountFrozenException`
  - `InvalidAmountException`
  - `DailyLimitExceededException`
- **Inheritance & Polymorphism**:
  - `Account` abstract class extended by `SavingsAccount`, `CurrentAccount`, `FixedDepositAccount`, and `RecurringDepositAccount`.
- **Encapsulation**:
  - All models have `private` fields with getters and setters.
- **Abstraction**:
  - `BankService` interface implemented by `BankServiceImpl`.
  - `Storage` interface implemented by `FileStorage`.
- **Code Style**:
  - Zero inline comments.
  - Plain `if-else` branching throughout (no `switch`, no ternary `? :`).
  - Small, modular methods.

---

## Default Test Credentials (Auto-seeded)

| Role | Username | Password | Default PIN | Notes |
| :--- | :--- | :--- | :--- | :--- |
| **Administrator** | `admin` | `admin123` | `1234` | Full access to Staff Portal |
| **Manager** | `manager` | `manager123` | `1234` | Staff Portal (Approvals, Loans) |
| **Teller** | `teller` | `teller123` | `1234` | Staff Portal (Operations) |
| **Customer 1** | `john` | `password123` | `1234` | Savings Account `ACC1001` (₹1,500.00) |
| **Customer 2** | `alice` | `password123` | `1234` | Current Account `ACC1002` (₹2,500.00) |

---

## How to Compile & Run

### Linux / macOS / Git Bash
```bash
cd "banking-system"
javac -d out $(find src -name "*.java")
java -cp out com.bank.Main
```

### Windows (PowerShell)
```powershell
Set-Location "C:\Users\Piyush Khaitan\.gemini\antigravity\scratch\banking-system"
javac -d out (Get-ChildItem -Path src -Filter *.java -Recurse | ForEach-Object { $_.FullName })
java -cp out com.bank.Main
```

Open your browser and navigate to: **`http://localhost:8080`**

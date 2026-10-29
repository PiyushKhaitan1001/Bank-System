package com.bank.service;

import com.bank.model.Account;
import com.bank.model.AccountFrozenException;
import com.bank.model.AuditLog;
import com.bank.model.Beneficiary;
import com.bank.model.Branch;
import com.bank.model.Card;
import com.bank.model.ChequeBookRequest;
import com.bank.model.CurrentAccount;
import com.bank.model.DailyLimitExceededException;
import com.bank.model.Employee;
import com.bank.model.Feedback;
import com.bank.model.FixedDepositAccount;
import com.bank.model.InsufficientFundsException;
import com.bank.model.InvalidAmountException;
import com.bank.model.Loan;
import com.bank.model.Notification;
import com.bank.model.RecurringDepositAccount;
import com.bank.model.SavingsAccount;
import com.bank.model.SavingsGoal;
import com.bank.model.StandingInstruction;
import com.bank.model.SupportTicket;
import com.bank.model.Transaction;
import com.bank.model.UpiProfile;
import com.bank.model.User;
import com.bank.storage.Storage;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class BankServiceImpl implements BankService {
    private final Storage storage;
    private final List<User> users;
    private final List<Account> accounts;
    private final List<Transaction> transactions;
    private final List<Card> cards;
    private final List<UpiProfile> upiProfiles;
    private final List<Loan> loans;
    private final List<Beneficiary> beneficiaries;
    private final List<StandingInstruction> standingInstructions;
    private final List<ChequeBookRequest> chequeRequests;
    private final List<SupportTicket> supportTickets;
    private final List<Feedback> feedbacks;
    private final List<SavingsGoal> savingsGoals;
    private final List<Branch> branches;
    private final List<Employee> employees;
    private final List<AuditLog> auditLogs;
    private final List<Notification> notifications;

    private final Map<String, String> otpStore = new ConcurrentHashMap<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final Random random = new Random();

    public BankServiceImpl(Storage storage) {
        this.storage = storage;
        this.users = new ArrayList<>(storage.loadUsers());
        this.accounts = new ArrayList<>(storage.loadAccounts());
        this.transactions = new ArrayList<>(storage.loadTransactions());
        this.cards = new ArrayList<>(storage.loadCards());
        this.upiProfiles = new ArrayList<>(storage.loadUpiProfiles());
        this.loans = new ArrayList<>(storage.loadLoans());
        this.beneficiaries = new ArrayList<>(storage.loadBeneficiaries());
        this.standingInstructions = new ArrayList<>(storage.loadStandingInstructions());
        this.chequeRequests = new ArrayList<>(storage.loadChequeBookRequests());
        this.supportTickets = new ArrayList<>(storage.loadSupportTickets());
        this.feedbacks = new ArrayList<>(storage.loadFeedback());
        this.savingsGoals = new ArrayList<>(storage.loadSavingsGoals());
        this.branches = new ArrayList<>(storage.loadBranches());
        this.employees = new ArrayList<>(storage.loadEmployees());
        this.auditLogs = new ArrayList<>(storage.loadAuditLogs());
        this.notifications = new ArrayList<>(storage.loadNotifications());

        if (this.users.isEmpty()) {
            seedInitialData();
        }
    }

    private void seedInitialData() {
        String defaultPinHash = hashPassword("1234");

        User admin = new User("admin", hashPassword("admin123"), "ADMIN", "admin@apexbank.com", "+1-555-0100", "100 Banking Way", defaultPinHash, 0, false, "What is your primary bank?", "apex", "APPROVED", "Passport");
        User manager = new User("manager", hashPassword("manager123"), "MANAGER", "manager@apexbank.com", "+1-555-0109", "102 Executive Suite", defaultPinHash, 0, false, "What is your favorite color?", "blue", "APPROVED", "State ID");
        User teller = new User("teller", hashPassword("teller123"), "TELLER", "teller@apexbank.com", "+1-555-0108", "104 Counter Row", defaultPinHash, 0, false, "What is your favorite animal?", "lion", "APPROVED", "Driver License");
        User john = new User("john", hashPassword("password123"), "CUSTOMER", "john@example.com", "+1-555-0101", "123 Maple St", defaultPinHash, 0, false, "What is your pet name?", "buddy", "APPROVED", "National Passport");
        User alice = new User("alice", hashPassword("password123"), "CUSTOMER", "alice@example.com", "+1-555-0102", "456 Oak Ave", defaultPinHash, 0, false, "What city were you born in?", "chicago", "APPROVED", "Driver License");

        users.add(admin);
        users.add(manager);
        users.add(teller);
        users.add(john);
        users.add(alice);

        Branch b1 = new Branch("APEX0001", "Downtown Central", "New York", "Robert Lang");
        Branch b2 = new Branch("APEX0002", "Financial District", "London", "Sarah Jenkins");
        Branch b3 = new Branch("APEX0003", "Tech Bay Branch", "San Francisco", "David Kim");
        branches.add(b1);
        branches.add(b2);
        branches.add(b3);

        Employee e1 = new Employee("EMP1001", "manager", "Robert Lang", "Branch Manager", "Executive Operations", 95000.0, "2024-01-15");
        Employee e2 = new Employee("EMP1002", "teller", "Emma Watson", "Senior Teller", "Customer Services", 52000.0, "2024-06-01");
        employees.add(e1);
        employees.add(e2);

        Account johnAcc = new SavingsAccount("ACC1001", "john", "None", 1500.0, "ACTIVE", "Mary Doe", "Spouse", "APEX0001", "2026-01-10");
        Account aliceAcc = new CurrentAccount("ACC1002", "alice", "None", 2500.0, "ACTIVE", "Peter Smith", "Brother", "APEX0002", "2026-02-15", 1000.0);
        accounts.add(johnAcc);
        accounts.add(aliceAcc);

        Card johnCard = new Card("4532 8920 1204 1001", "ACC1001", "john", "12/29", "832", "ACTIVE", 2000.0, 0.0);
        Card aliceCard = new Card("4532 4118 7632 1002", "ACC1002", "alice", "08/28", "491", "ACTIVE", 3000.0, 0.0);
        cards.add(johnCard);
        cards.add(aliceCard);

        UpiProfile johnUpi = new UpiProfile("john@apex", "ACC1001", "john", "ACTIVE", 1000.0, 0.0);
        UpiProfile aliceUpi = new UpiProfile("alice@apex", "ACC1002", "alice", "ACTIVE", 1500.0, 0.0);
        upiProfiles.add(johnUpi);
        upiProfiles.add(aliceUpi);

        Beneficiary ben = new Beneficiary("BEN1001", "john", "Alice Smith", "ACC1002", "Alice Biz");
        beneficiaries.add(ben);

        SavingsGoal goal = new SavingsGoal("GOL1001", "john", "New Electric Vehicle", 25000.0, 6500.0, "2027-12-31");
        savingsGoals.add(goal);

        Loan sampleLoan = new Loan("LOAN1001", "john", "CAR", 15000.0, 0.075, 36, 466.50, 11200.0, "APPROVED", "2026-03-01", "ACC1001");
        loans.add(sampleLoan);

        String now = getCurrentTimestamp();
        Transaction t1 = new Transaction("TXN1001", "DEPOSIT", 1500.0, now, "CASH", "ACC1001", "SALARY", "NONE");
        Transaction t2 = new Transaction("TXN1002", "DEPOSIT", 2500.0, now, "CASH", "ACC1002", "SALARY", "NONE");
        transactions.add(t1);
        transactions.add(t2);

        AuditLog log = new AuditLog("AUD1001", "SYSTEM", "SYSTEM_INITIALIZE", "Seeded enterprise banking modules and security controls", now);
        auditLogs.add(log);

        Notification notif = new Notification("NTF1001", "john", "Welcome to Apex Banking", "Your high-yield savings account ACC1001 is active with 4.0% annual interest.", now, false);
        notifications.add(notif);

        persistAll();
    }

    private void persistAll() {
        storage.saveUsers(users);
        storage.saveAccounts(accounts);
        storage.saveTransactions(transactions);
        storage.saveCards(cards);
        storage.saveUpiProfiles(upiProfiles);
        storage.saveLoans(loans);
        storage.saveBeneficiaries(beneficiaries);
        storage.saveStandingInstructions(standingInstructions);
        storage.saveChequeBookRequests(chequeRequests);
        storage.saveSupportTickets(supportTickets);
        storage.saveFeedback(feedbacks);
        storage.saveSavingsGoals(savingsGoals);
        storage.saveBranches(branches);
        storage.saveEmployees(employees);
        storage.saveAuditLogs(auditLogs);
        storage.saveNotifications(notifications);
    }

    private String getCurrentTimestamp() {
        return formatter.format(LocalDateTime.now());
    }

    private String getCurrentDate() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
    }

    @Override
    public String hashPassword(String password) {
        if (password == null) {
            return "";
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 error", e);
        }
    }

    @Override
    public synchronized User authenticate(String username, String password) {
        if (username == null || password == null) {
            return null;
        }
        User user = getUserByUsername(username.trim());
        if (user == null) {
            return null;
        }
        if (user.isLocked()) {
            logAudit("AUTH", "LOGIN_BLOCKED", "User " + username + " is locked due to security policy");
            return null;
        }
        String hashed = hashPassword(password.trim());
        if (user.getHashedPassword().equals(hashed)) {
            user.setFailedLoginAttempts(0);
            storage.saveUsers(users);
            logAudit(user.getUsername(), "LOGIN_SUCCESS", "User authenticated successfully");
            return user;
        } else {
            int attempts = user.getFailedLoginAttempts() + 1;
            user.setFailedLoginAttempts(attempts);
            if (attempts >= 3) {
                user.setLocked(true);
                logAudit(user.getUsername(), "ACCOUNT_LOCKED", "Locked after 3 failed login attempts");
                addNotification(user.getUsername(), "Security Alert: Account Locked", "Your account was locked after 3 unsuccessful login attempts. Contact an administrator.");
            } else {
                logAudit(user.getUsername(), "LOGIN_FAILED", "Failed attempt " + attempts + " of 3");
            }
            storage.saveUsers(users);
            return null;
        }
    }

    @Override
    public synchronized boolean register(String username, String password) {
        return register(username, password, username + "@apexbank.com", "N/A", "N/A", "1234");
    }

    @Override
    public synchronized boolean register(String username, String password, String email, String phone, String address, String pin) {
        return createUserByStaff(username, password, "CUSTOMER", email, phone, address, pin, "SELF_REGISTRATION");
    }

    @Override
    public synchronized boolean createUserByStaff(String username, String password, String role, String email, String phone, String address, String pin, String actor) {
        if (username == null || password == null) {
            return false;
        }
        username = username.trim();
        password = password.trim();
        if (username.isEmpty() || password.isEmpty()) {
            return false;
        }
        if (getUserByUsername(username) != null) {
            return false;
        }
        String assignedRole = "CUSTOMER";
        if (role != null) {
            if (role.equalsIgnoreCase("ADMIN") || role.equalsIgnoreCase("MANAGER") || role.equalsIgnoreCase("TELLER")) {
                assignedRole = role.toUpperCase();
            }
        }
        String hashedPin = hashPassword(pin);
        User user = new User(username, hashPassword(password), assignedRole, email, phone, address, hashedPin,
                0, false, "What is your primary bank?", "apex", "APPROVED", "Self Verified");
        users.add(user);
        storage.saveUsers(users);
        logAudit(actor, "USER_CREATED", "Created user " + username + " with role " + assignedRole);
        addNotification(username, "Account Profile Created", "Welcome to Apex Banking! Your digital identity has been set up.");
        return true;
    }

    @Override
    public synchronized boolean updateProfile(String username, String email, String phone, String address) {
        User user = getUserByUsername(username);
        if (user == null) {
            return false;
        }
        if (email != null && !email.trim().isEmpty()) {
            user.setEmail(email.trim());
        }
        if (phone != null && !phone.trim().isEmpty()) {
            user.setPhone(phone.trim());
        }
        if (address != null && !address.trim().isEmpty()) {
            user.setAddress(address.trim());
        }
        storage.saveUsers(users);
        logAudit(username, "PROFILE_UPDATED", "Customer updated personal profile details");
        addNotification(username, "Profile Updated", "Your profile contact information was updated successfully.");
        return true;
    }

    @Override
    public synchronized boolean changePassword(String username, String oldPassword, String newPassword) {
        User user = getUserByUsername(username);
        if (user == null) {
            return false;
        }
        if (!user.getHashedPassword().equals(hashPassword(oldPassword))) {
            return false;
        }
        user.setHashedPassword(hashPassword(newPassword));
        storage.saveUsers(users);
        logAudit(username, "PASSWORD_CHANGED", "Password updated successfully");
        addNotification(username, "Security Alert", "Your account password was changed.");
        return true;
    }

    @Override
    public synchronized boolean resetPasswordWithSecurityQuestion(String username, String answer, String newPassword) {
        User user = getUserByUsername(username);
        if (user == null) {
            return false;
        }
        if (user.getSecurityAnswer() == null || !user.getSecurityAnswer().equalsIgnoreCase(answer.trim())) {
            return false;
        }
        user.setHashedPassword(hashPassword(newPassword));
        user.setFailedLoginAttempts(0);
        user.setLocked(false);
        storage.saveUsers(users);
        logAudit(username, "PASSWORD_RESET_RECOVERY", "Password reset using security questions and unlocked");
        addNotification(username, "Password Reset", "Your password was reset and your account is unlocked.");
        return true;
    }

    @Override
    public synchronized boolean unlockUser(String username, String actor) {
        User user = getUserByUsername(username);
        if (user == null) {
            return false;
        }
        user.setLocked(false);
        user.setFailedLoginAttempts(0);
        storage.saveUsers(users);
        logAudit(actor, "USER_UNLOCKED", "Unlocked account for " + username);
        addNotification(username, "Account Unlocked", "An administrator has restored your login access.");
        return true;
    }

    @Override
    public synchronized boolean lockUser(String username, String actor) {
        User user = getUserByUsername(username);
        if (user == null) {
            return false;
        }
        user.setLocked(true);
        storage.saveUsers(users);
        logAudit(actor, "USER_LOCKED", "Manually locked account for " + username);
        return true;
    }

    @Override
    public synchronized boolean submitKyc(String username, String documentName) {
        User user = getUserByUsername(username);
        if (user == null) {
            return false;
        }
        user.setKycDocument(documentName);
        user.setKycStatus("PENDING");
        storage.saveUsers(users);
        logAudit(username, "KYC_SUBMITTED", "Submitted document: " + documentName);
        addNotification(username, "KYC Under Review", "Your KYC document (" + documentName + ") was submitted for staff approval.");
        return true;
    }

    @Override
    public synchronized boolean reviewKyc(String username, boolean approve, String actor) {
        User user = getUserByUsername(username);
        if (user == null) {
            return false;
        }
        if (approve) {
            user.setKycStatus("APPROVED");
            addNotification(username, "KYC Approved", "Congratulations! Your identity has been fully verified.");
        } else {
            user.setKycStatus("REJECTED");
            addNotification(username, "KYC Rejected", "Your KYC document was declined. Please re-submit valid credentials.");
        }
        storage.saveUsers(users);
        logAudit(actor, "KYC_REVIEW", "KYC status for " + username + " set to " + user.getKycStatus());
        return true;
    }

    @Override
    public synchronized User getUserByUsername(String username) {
        if (username == null) {
            return null;
        }
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public synchronized List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    @Override
    public synchronized boolean verifyUserPin(String username, String pin) {
        if (username == null || pin == null) {
            return false;
        }
        User user = getUserByUsername(username);
        if (user == null) {
            return false;
        }
        String candidateHash = hashPassword(pin.trim());
        if (user.getHashedPin() != null && !user.getHashedPin().isEmpty()) {
            if (user.getHashedPin().equals(candidateHash)) {
                return true;
            } else {
                return false;
            }
        } else {
            if (pin.trim().equals("1234") || pin.trim().isEmpty()) {
                return true;
            } else {
                return false;
            }
        }
    }

    private synchronized String generateNextAccountNumber() {
        int maxNumber = 1000;
        for (Account a : accounts) {
            String num = a.getAccountNumber();
            if (num != null && num.startsWith("ACC")) {
                try {
                    int val = Integer.parseInt(num.substring(3));
                    if (val > maxNumber) {
                        maxNumber = val;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        int next = maxNumber + 1;
        return "ACC" + next;
    }

    private synchronized String generateNextTransactionId() {
        int maxNumber = 1000;
        for (Transaction t : transactions) {
            String id = t.getId();
            if (id != null && id.startsWith("TXN")) {
                try {
                    int val = Integer.parseInt(id.substring(3));
                    if (val > maxNumber) {
                        maxNumber = val;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        int next = maxNumber + 1;
        return "TXN" + next;
    }

    @Override
    public synchronized Account openAccount(String username, String accountType, double initialDeposit) {
        return openAccountWithDetails(username, accountType, initialDeposit, "None", "None", "None", "APEX0001", true);
    }

    @Override
    public synchronized Account openAccountWithDetails(String username, String accountType, double initialDeposit,
                                                       String jointHolder, String nomineeName, String nomineeRelation,
                                                       String branchCode, boolean autoApprove) {
        if (username == null || accountType == null) {
            return null;
        }
        User user = getUserByUsername(username);
        if (user == null) {
            return null;
        }
        String status = "ACTIVE";
        if (!autoApprove) {
            status = "PENDING_APPROVAL";
        }
        String accNum = generateNextAccountNumber();
        String date = getCurrentDate();
        Account account = null;

        if (accountType.equalsIgnoreCase("SAVINGS")) {
            if (initialDeposit < 500.0) {
                return null;
            }
            account = new SavingsAccount(accNum, username, jointHolder, initialDeposit, status, nomineeName, nomineeRelation, branchCode, date);
        } else {
            if (accountType.equalsIgnoreCase("CURRENT")) {
                if (initialDeposit < 0.0) {
                    return null;
                }
                account = new CurrentAccount(accNum, username, jointHolder, initialDeposit, status, nomineeName, nomineeRelation, branchCode, date, 1000.0);
            } else {
                return null;
            }
        }

        accounts.add(account);
        storage.saveAccounts(accounts);

        String cardNumber = "4532 " + (1000 + cards.size() * 37) + " " + (2000 + cards.size() * 19) + " " + accNum.replace("ACC", "");
        Card card = new Card(cardNumber, accNum, username, "12/30", "384", "ACTIVE", 2000.0, 0.0);
        cards.add(card);
        storage.saveCards(cards);

        if (getUpiProfileByUsername(username) == null) {
            UpiProfile upi = new UpiProfile(username.toLowerCase() + "@apex", accNum, username, "ACTIVE", 1000.0, 0.0);
            upiProfiles.add(upi);
            storage.saveUpiProfiles(upiProfiles);
        }

        if (initialDeposit > 0) {
            Transaction t = new Transaction(generateNextTransactionId(), "DEPOSIT", initialDeposit, getCurrentTimestamp(), "CASH", accNum, "SALARY", "NONE");
            transactions.add(t);
            storage.saveTransactions(transactions);
        }

        logAudit(username, "ACCOUNT_OPENED", "Opened " + accountType + " account " + accNum + " with balance ₹" + initialDeposit);
        addNotification(username, "New Account Created", "Your " + accountType + " account " + accNum + " has been registered.");
        return account;
    }

    @Override
    public synchronized FixedDepositAccount openFixedDeposit(String username, double principal, int termMonths,
                                                            String nomineeName, String nomineeRelation, String branchCode) {
        if (principal < 1000.0 || termMonths < 6) {
            return null;
        }
        Account primary = null;
        for (Account a : getAccountsByUsername(username)) {
            if (!a.isFrozen() && a.getBalance() >= principal) {
                primary = a;
                break;
            }
        }
        if (primary == null) {
            return null;
        }
        primary.setBalance(primary.getBalance() - principal);

        double rate = 0.07;
        if (termMonths >= 24) {
            rate = 0.075;
        }
        if (termMonths >= 36) {
            rate = 0.08;
        }

        double factor = Math.pow(1.0 + (rate / 12.0), termMonths);
        double matAmt = principal * factor;
        String accNum = generateNextAccountNumber();
        String date = getCurrentDate();
        LocalDate mDate = LocalDate.now().plusMonths(termMonths);

        FixedDepositAccount fd = new FixedDepositAccount(accNum, username, "None", principal, "ACTIVE",
                nomineeName, nomineeRelation, branchCode, date, termMonths, rate, mDate.toString(), matAmt);
        accounts.add(fd);
        storage.saveAccounts(accounts);

        Transaction t = new Transaction(generateNextTransactionId(), "FD_DEPOSIT", principal, getCurrentTimestamp(), primary.getAccountNumber(), accNum, "INVESTMENT", "NONE");
        transactions.add(t);
        storage.saveTransactions(transactions);

        logAudit(username, "FD_CREATED", "Opened FD " + accNum + " for ₹" + principal + " maturing at ₹" + String.format("%.2f", matAmt));
        addNotification(username, "Fixed Deposit Booked", "FD " + accNum + " booked for " + termMonths + " months at " + (rate * 100) + "%. Maturity: ₹" + String.format("%.2f", matAmt));
        return fd;
    }

    @Override
    public synchronized RecurringDepositAccount openRecurringDeposit(String username, double monthlyInstallment,
                                                                    int termMonths, String nomineeName,
                                                                    String nomineeRelation, String branchCode) {
        if (monthlyInstallment < 100.0 || termMonths < 6) {
            return null;
        }
        Account primary = null;
        for (Account a : getAccountsByUsername(username)) {
            if (!a.isFrozen() && a.getBalance() >= monthlyInstallment) {
                primary = a;
                break;
            }
        }
        if (primary == null) {
            return null;
        }
        primary.setBalance(primary.getBalance() - monthlyInstallment);

        double rate = 0.065;
        double totalPrincipal = monthlyInstallment * termMonths;
        double estimatedInterest = totalPrincipal * rate * (termMonths / 12.0) * 0.5;
        double matAmt = totalPrincipal + estimatedInterest;
        String accNum = generateNextAccountNumber();
        String date = getCurrentDate();
        LocalDate mDate = LocalDate.now().plusMonths(termMonths);

        RecurringDepositAccount rd = new RecurringDepositAccount(accNum, username, "None", monthlyInstallment, "ACTIVE",
                nomineeName, nomineeRelation, branchCode, date, monthlyInstallment, termMonths, rate, mDate.toString(), matAmt, 1);
        accounts.add(rd);
        storage.saveAccounts(accounts);

        Transaction t = new Transaction(generateNextTransactionId(), "RD_INSTALLMENT", monthlyInstallment, getCurrentTimestamp(), primary.getAccountNumber(), accNum, "INVESTMENT", "NONE");
        transactions.add(t);
        storage.saveTransactions(transactions);

        logAudit(username, "RD_CREATED", "Opened RD " + accNum + " for ₹" + monthlyInstallment + "/mo");
        addNotification(username, "Recurring Deposit Started", "RD " + accNum + " created for ₹" + monthlyInstallment + "/month.");
        return rd;
    }

    @Override
    public synchronized boolean approveAccount(String accountNumber, String actor) {
        Account a = getAccountByNumber(accountNumber);
        if (a == null) {
            return false;
        }
        a.setStatus("ACTIVE");
        storage.saveAccounts(accounts);
        logAudit(actor, "ACCOUNT_APPROVED", "Approved account application: " + accountNumber);
        addNotification(a.getHolderName(), "Account Approved", "Your account " + accountNumber + " has been approved by staff and is now active.");
        return true;
    }

    @Override
    public synchronized boolean rejectAccount(String accountNumber, String actor) {
        Account a = getAccountByNumber(accountNumber);
        if (a == null) {
            return false;
        }
        accounts.remove(a);
        storage.saveAccounts(accounts);
        logAudit(actor, "ACCOUNT_REJECTED", "Rejected account application: " + accountNumber);
        addNotification(a.getHolderName(), "Account Declined", "Your account application for " + accountNumber + " was not approved.");
        return true;
    }

    @Override
    public synchronized List<Account> getAccountsByUsername(String username) {
        List<Account> list = new ArrayList<>();
        if (username == null) {
            return list;
        }
        for (Account a : accounts) {
            if (a.getHolderName().equalsIgnoreCase(username) || a.getJointHolderName().equalsIgnoreCase(username)) {
                list.add(a);
            }
        }
        return list;
    }

    @Override
    public synchronized List<Account> getAllAccounts() {
        return new ArrayList<>(accounts);
    }

    @Override
    public synchronized Account getAccountByNumber(String accountNumber) {
        if (accountNumber == null) {
            return null;
        }
        for (Account a : accounts) {
            if (a.getAccountNumber().equalsIgnoreCase(accountNumber)) {
                return a;
            }
        }
        return null;
    }

    @Override
    public synchronized boolean freezeAccount(String accountNumber, String actor) {
        Account a = getAccountByNumber(accountNumber);
        if (a == null) {
            return false;
        }
        a.setStatus("FROZEN");
        storage.saveAccounts(accounts);
        logAudit(actor, "ACCOUNT_FROZEN", "Froze account " + accountNumber);
        addNotification(a.getHolderName(), "Account Notice", "Your account " + accountNumber + " has been frozen by administration.");
        return true;
    }

    @Override
    public synchronized boolean unfreezeAccount(String accountNumber, String actor) {
        Account a = getAccountByNumber(accountNumber);
        if (a == null) {
            return false;
        }
        a.setStatus("ACTIVE");
        storage.saveAccounts(accounts);
        logAudit(actor, "ACCOUNT_UNFROZEN", "Restored account " + accountNumber);
        addNotification(a.getHolderName(), "Account Unfrozen", "Your account " + accountNumber + " has been restored to active status.");
        return true;
    }

    @Override
    public synchronized boolean deleteAccount(String accountNumber, String actor) {
        Account a = getAccountByNumber(accountNumber);
        if (a == null) {
            return false;
        }
        accounts.remove(a);
        storage.saveAccounts(accounts);
        logAudit(actor, "ACCOUNT_DELETED", "Deleted account " + accountNumber);
        return true;
    }

    @Override
    public synchronized int creditMonthlyInterestToAllSavings() {
        int credited = 0;
        String now = getCurrentTimestamp();
        for (Account a : accounts) {
            if (a instanceof SavingsAccount && !a.isFrozen() && a.getBalance() > 0) {
                double interest = a.calculateMonthlyInterest();
                if (interest > 0.01) {
                    a.setBalance(a.getBalance() + interest);
                    Transaction t = new Transaction(generateNextTransactionId(), "INTEREST_CREDIT", interest, now, "APEX_CENTRAL", a.getAccountNumber(), "SALARY", "NONE");
                    transactions.add(t);
                    addNotification(a.getHolderName(), "Monthly Interest Credited", "Interest of ₹" + String.format("%.2f", interest) + " was credited to " + a.getAccountNumber());
                    credited++;
                }
            }
        }
        if (credited > 0) {
            storage.saveAccounts(accounts);
            storage.saveTransactions(transactions);
            storage.saveNotifications(notifications);
            logAudit("SYSTEM", "INTEREST_RUN", "Monthly interest batch credited to " + credited + " savings accounts");
        }
        return credited;
    }

    @Override
    public synchronized boolean deposit(String accountNumber, double amount, String pin)
            throws InsufficientFundsException, AccountFrozenException, InvalidAmountException {
        if (amount <= 0.0) {
            throw new InvalidAmountException("Deposit amount must be strictly positive");
        }
        Account a = getAccountByNumber(accountNumber);
        if (a == null) {
            return false;
        }
        if (a.isFrozen()) {
            throw new AccountFrozenException("Account " + accountNumber + " is currently frozen");
        }
        if (!verifyUserPin(a.getHolderName(), pin)) {
            return false;
        }
        a.setBalance(a.getBalance() + amount);
        storage.saveAccounts(accounts);
        Transaction t = new Transaction(generateNextTransactionId(), "DEPOSIT", amount, getCurrentTimestamp(), "CASH", accountNumber, "SALARY", "NONE");
        transactions.add(t);
        storage.saveTransactions(transactions);
        addNotification(a.getHolderName(), "Funds Credited", "₹" + String.format("%.2f", amount) + " was deposited into " + accountNumber);
        return true;
    }

    @Override
    public synchronized boolean withdraw(String accountNumber, double amount, String pin)
            throws InsufficientFundsException, AccountFrozenException, InvalidAmountException, DailyLimitExceededException {
        if (amount <= 0.0) {
            throw new InvalidAmountException("Withdrawal amount must be strictly positive");
        }
        if (amount > 5000.0) {
            throw new DailyLimitExceededException("Withdrawal exceeds maximum daily cap of ₹5,000.00");
        }
        Account a = getAccountByNumber(accountNumber);
        if (a == null) {
            return false;
        }
        if (a.isFrozen()) {
            throw new AccountFrozenException("Account " + accountNumber + " is frozen");
        }
        if (!verifyUserPin(a.getHolderName(), pin)) {
            return false;
        }
        double effectiveAvailable = a.getBalance() + a.getOverdraftLimit() - a.getMinimumBalance();
        if (effectiveAvailable < amount) {
            throw new InsufficientFundsException("Insufficient funds. Available: ₹" + String.format("%.2f", effectiveAvailable));
        }
        a.setBalance(a.getBalance() - amount);
        storage.saveAccounts(accounts);
        Transaction t = new Transaction(generateNextTransactionId(), "WITHDRAWAL", amount, getCurrentTimestamp(), accountNumber, "CASH", "SHOPPING", "NONE");
        transactions.add(t);
        storage.saveTransactions(transactions);
        addNotification(a.getHolderName(), "Funds Withdrawn", "₹" + String.format("%.2f", amount) + " was withdrawn from " + accountNumber);
        return true;
    }

    @Override
    public synchronized boolean transfer(String sourceAccountNumber, String targetAccountNumber, double amount, String pin)
            throws InsufficientFundsException, AccountFrozenException, InvalidAmountException, DailyLimitExceededException {
        if (amount <= 0.0) {
            throw new InvalidAmountException("Transfer amount must be positive");
        }
        if (amount > 10000.0) {
            throw new DailyLimitExceededException("Transfer exceeds single transaction limit of ₹10,000.00");
        }
        if (sourceAccountNumber == null || targetAccountNumber == null || sourceAccountNumber.equalsIgnoreCase(targetAccountNumber)) {
            return false;
        }
        Account source = getAccountByNumber(sourceAccountNumber);
        Account target = getAccountByNumber(targetAccountNumber);
        if (source == null || target == null) {
            return false;
        }
        if (source.isFrozen() || target.isFrozen()) {
            throw new AccountFrozenException("One or more accounts involved in transfer are frozen");
        }
        if (!verifyUserPin(source.getHolderName(), pin)) {
            return false;
        }
        double available = source.getBalance() + source.getOverdraftLimit() - source.getMinimumBalance();
        if (available < amount) {
            throw new InsufficientFundsException("Insufficient funds for transfer");
        }
        source.setBalance(source.getBalance() - amount);
        target.setBalance(target.getBalance() + amount);
        storage.saveAccounts(accounts);
        Transaction t = new Transaction(generateNextTransactionId(), "TRANSFER", amount, getCurrentTimestamp(), sourceAccountNumber, targetAccountNumber, "TRANSFER", "NONE");
        transactions.add(t);
        storage.saveTransactions(transactions);
        addNotification(source.getHolderName(), "Transfer Sent", "Transferred ₹" + String.format("%.2f", amount) + " to " + targetAccountNumber);
        addNotification(target.getHolderName(), "Transfer Received", "Received ₹" + String.format("%.2f", amount) + " from " + sourceAccountNumber);
        return true;
    }

    @Override
    public synchronized boolean payBill(String username, String accountNumber, String billerType, String billerRef, double amount, String pin)
            throws InsufficientFundsException, AccountFrozenException, InvalidAmountException {
        if (amount <= 0.0) {
            throw new InvalidAmountException("Bill amount must be positive");
        }
        Account a = getAccountByNumber(accountNumber);
        if (a == null) {
            return false;
        }
        if (a.isFrozen()) {
            throw new AccountFrozenException("Account is frozen");
        }
        if (!verifyUserPin(username, pin)) {
            return false;
        }
        double available = a.getBalance() + a.getOverdraftLimit() - a.getMinimumBalance();
        if (available < amount) {
            throw new InsufficientFundsException("Insufficient funds for utility payment");
        }
        a.setBalance(a.getBalance() - amount);
        storage.saveAccounts(accounts);
        Transaction t = new Transaction(generateNextTransactionId(), "BILL_PAY", amount, getCurrentTimestamp(), accountNumber, billerType.toUpperCase() + "_" + billerRef, "BILLS", "NONE");
        transactions.add(t);
        storage.saveTransactions(transactions);
        addNotification(username, "Bill Payment Success", "Paid ₹" + String.format("%.2f", amount) + " for " + billerType + " (Ref: " + billerRef + ")");
        return true;
    }

    @Override
    public synchronized List<Transaction> getTransactionsByAccount(String accountNumber) {
        List<Transaction> list = new ArrayList<>();
        if (accountNumber == null) {
            return list;
        }
        for (Transaction t : transactions) {
            if (t.getSource().equalsIgnoreCase(accountNumber) || t.getTarget().equalsIgnoreCase(accountNumber)) {
                list.add(t);
            }
        }
        return list;
    }

    @Override
    public synchronized List<Transaction> getTransactionsByUser(String username) {
        return getTransactionsByUserAndFilter(username, null, null, null, null);
    }

    @Override
    public synchronized List<Transaction> getTransactionsByUserAndFilter(String username, String typeFilter,
                                                                         String categoryFilter, String startDate, String endDate) {
        List<Account> userAccounts = getAccountsByUsername(username);
        List<String> accNums = new ArrayList<>();
        for (Account a : userAccounts) {
            accNums.add(a.getAccountNumber().toUpperCase());
        }
        UpiProfile upi = getUpiProfileByUsername(username);
        String upiId = "";
        if (upi != null) {
            upiId = upi.getUpiId().toUpperCase();
        }

        List<Transaction> list = new ArrayList<>();
        for (Transaction t : transactions) {
            boolean matchesUser = false;
            if (accNums.contains(t.getSource().toUpperCase()) || accNums.contains(t.getTarget().toUpperCase())) {
                matchesUser = true;
            } else {
                if (!upiId.isEmpty() && (t.getSource().toUpperCase().equals(upiId) || t.getTarget().toUpperCase().equals(upiId))) {
                    matchesUser = true;
                }
            }

            if (matchesUser) {
                boolean pass = true;
                if (typeFilter != null && !typeFilter.trim().isEmpty() && !typeFilter.equalsIgnoreCase("ALL")) {
                    if (!t.getType().equalsIgnoreCase(typeFilter.trim())) {
                        pass = false;
                    }
                }
                if (pass && categoryFilter != null && !categoryFilter.trim().isEmpty() && !categoryFilter.equalsIgnoreCase("ALL")) {
                    if (!t.getCategory().equalsIgnoreCase(categoryFilter.trim())) {
                        pass = false;
                    }
                }
                if (pass && startDate != null && !startDate.trim().isEmpty()) {
                    if (t.getTimestamp().compareTo(startDate) < 0) {
                        pass = false;
                    }
                }
                if (pass && endDate != null && !endDate.trim().isEmpty()) {
                    if (t.getTimestamp().compareTo(endDate + " 23:59:59") > 0) {
                        pass = false;
                    }
                }
                if (pass) {
                    list.add(t);
                }
            }
        }
        return list;
    }

    @Override
    public synchronized List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }

    @Override
    public synchronized boolean disputeTransaction(String transactionId, String username) {
        for (Transaction t : transactions) {
            if (t.getId().equalsIgnoreCase(transactionId)) {
                t.setDisputeStatus("PENDING");
                storage.saveTransactions(transactions);
                logAudit(username, "TRANSACTION_DISPUTED", "Opened dispute on transaction: " + transactionId);
                addNotification(username, "Dispute Received", "Your dispute request for " + transactionId + " is under administrative review.");
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized boolean reviewDispute(String transactionId, boolean approve, String actor) {
        for (Transaction t : transactions) {
            if (t.getId().equalsIgnoreCase(transactionId)) {
                if (approve) {
                    t.setDisputeStatus("APPROVED");
                    Account src = getAccountByNumber(t.getSource());
                    if (src != null) {
                        src.setBalance(src.getBalance() + t.getAmount());
                        storage.saveAccounts(accounts);
                        Transaction refund = new Transaction(generateNextTransactionId(), "DISPUTE_REFUND", t.getAmount(), getCurrentTimestamp(), "APEX_DISPUTES", src.getAccountNumber(), "REFUND", "NONE");
                        transactions.add(refund);
                        addNotification(src.getHolderName(), "Dispute Refund Approved", "Refund of ₹" + String.format("%.2f", t.getAmount()) + " credited for txn " + transactionId);
                    }
                } else {
                    t.setDisputeStatus("REJECTED");
                }
                storage.saveTransactions(transactions);
                logAudit(actor, "DISPUTE_REVIEWED", "Dispute for " + transactionId + " set to " + t.getDisputeStatus());
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized List<Card> getCardsByUsername(String username) {
        List<Card> list = new ArrayList<>();
        if (username == null) {
            return list;
        }
        for (Card c : cards) {
            if (c.getHolderName().equalsIgnoreCase(username)) {
                list.add(c);
            }
        }
        return list;
    }

    @Override
    public synchronized Card getCardByNumber(String cardNumber) {
        if (cardNumber == null) {
            return null;
        }
        for (Card c : cards) {
            if (c.getCardNumber().replace(" ", "").equalsIgnoreCase(cardNumber.replace(" ", ""))) {
                return c;
            }
        }
        return null;
    }

    @Override
    public synchronized boolean updateCardLimit(String cardNumber, double newLimit) {
        if (newLimit < 0.0) {
            return false;
        }
        Card c = getCardByNumber(cardNumber);
        if (c == null) {
            return false;
        }
        c.setDailyLimit(newLimit);
        storage.saveCards(cards);
        return true;
    }

    @Override
    public synchronized boolean toggleCardStatus(String cardNumber) {
        Card c = getCardByNumber(cardNumber);
        if (c == null) {
            return false;
        }
        if (c.isFrozen()) {
            c.setStatus("ACTIVE");
        } else {
            c.setStatus("FROZEN");
        }
        storage.saveCards(cards);
        return true;
    }

    @Override
    public synchronized UpiProfile getUpiProfileByUsername(String username) {
        if (username == null) {
            return null;
        }
        for (UpiProfile u : upiProfiles) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                return u;
            }
        }
        return null;
    }

    @Override
    public synchronized UpiProfile getUpiProfileById(String upiId) {
        if (upiId == null) {
            return null;
        }
        for (UpiProfile u : upiProfiles) {
            if (u.getUpiId().equalsIgnoreCase(upiId.trim())) {
                return u;
            }
        }
        return null;
    }

    @Override
    public synchronized boolean updateUpiLimit(String upiId, double newLimit) {
        if (newLimit < 0.0) {
            return false;
        }
        UpiProfile u = getUpiProfileById(upiId);
        if (u == null) {
            return false;
        }
        u.setDailyLimit(newLimit);
        storage.saveUpiProfiles(upiProfiles);
        return true;
    }

    @Override
    public synchronized boolean toggleUpiStatus(String upiId) {
        UpiProfile u = getUpiProfileById(upiId);
        if (u == null) {
            return false;
        }
        if (u.isFrozen()) {
            u.setStatus("ACTIVE");
        } else {
            u.setStatus("FROZEN");
        }
        storage.saveUpiProfiles(upiProfiles);
        return true;
    }

    @Override
    public synchronized boolean processUpiPayment(String fromUsername, String toUpiId, double amount, String pin)
            throws InsufficientFundsException, AccountFrozenException, InvalidAmountException, DailyLimitExceededException {
        if (amount <= 0.0) {
            throw new InvalidAmountException("UPI amount must be positive");
        }
        if (!verifyUserPin(fromUsername, pin)) {
            return false;
        }
        UpiProfile senderUpi = getUpiProfileByUsername(fromUsername);
        UpiProfile receiverUpi = getUpiProfileById(toUpiId);
        if (senderUpi == null || receiverUpi == null) {
            return false;
        }
        if (senderUpi.isFrozen() || receiverUpi.isFrozen()) {
            throw new AccountFrozenException("UPI is disabled on account");
        }
        if (!senderUpi.canSpend(amount)) {
            throw new DailyLimitExceededException("Amount exceeds UPI daily limit");
        }
        Account src = getAccountByNumber(senderUpi.getAccountNumber());
        Account tgt = getAccountByNumber(receiverUpi.getAccountNumber());
        if (src == null || tgt == null) {
            return false;
        }
        if (src.isFrozen() || tgt.isFrozen()) {
            throw new AccountFrozenException("Associated bank account is frozen");
        }
        double available = src.getBalance() + src.getOverdraftLimit() - src.getMinimumBalance();
        if (available < amount) {
            throw new InsufficientFundsException("Insufficient funds for UPI transfer");
        }
        src.setBalance(src.getBalance() - amount);
        tgt.setBalance(tgt.getBalance() + amount);
        senderUpi.setUsedToday(senderUpi.getUsedToday() + amount);
        storage.saveAccounts(accounts);
        storage.saveUpiProfiles(upiProfiles);
        Transaction t = new Transaction(generateNextTransactionId(), "UPI_TRANSFER", amount, getCurrentTimestamp(), senderUpi.getUpiId(), receiverUpi.getUpiId(), "TRANSFER", "NONE");
        transactions.add(t);
        storage.saveTransactions(transactions);
        addNotification(fromUsername, "UPI Transfer Sent", "Sent ₹" + String.format("%.2f", amount) + " to " + toUpiId);
        addNotification(receiverUpi.getUsername(), "UPI Payment Received", "Received ₹" + String.format("%.2f", amount) + " from " + senderUpi.getUpiId());
        return true;
    }

    @Override
    public synchronized Loan applyLoan(String username, String loanType, double principal, int termMonths, String disbursedAccount) {
        if (principal <= 0.0 || termMonths <= 0) {
            return null;
        }
        double annualRate = 0.085;
        if (loanType.equalsIgnoreCase("CAR")) {
            annualRate = 0.095;
        } else {
            if (loanType.equalsIgnoreCase("PERSONAL")) {
                annualRate = 0.125;
            }
        }
        double emi = Loan.computeEmi(principal, annualRate, termMonths);
        int maxLoan = 1000;
        for (Loan l : loans) {
            if (l.getId().startsWith("LOAN")) {
                try {
                    int v = Integer.parseInt(l.getId().substring(4));
                    if (v > maxLoan) {
                        maxLoan = v;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        String id = "LOAN" + (maxLoan + 1);
        Loan loan = new Loan(id, username, loanType.toUpperCase(), principal, annualRate, termMonths, emi, principal, "PENDING", getCurrentDate(), disbursedAccount);
        loans.add(loan);
        storage.saveLoans(loans);
        logAudit(username, "LOAN_APPLIED", "Applied for " + loanType + " loan of ₹" + principal);
        addNotification(username, "Loan Application Submitted", "Your application for " + id + " (₹" + principal + ") is pending manager approval.");
        return loan;
    }

    @Override
    public synchronized boolean reviewLoan(String loanId, boolean approve, String actor) {
        for (Loan l : loans) {
            if (l.getId().equalsIgnoreCase(loanId)) {
                if (approve) {
                    l.setStatus("APPROVED");
                    Account target = getAccountByNumber(l.getDisbursedAccountNumber());
                    if (target != null) {
                        target.setBalance(target.getBalance() + l.getPrincipal());
                        storage.saveAccounts(accounts);
                        Transaction t = new Transaction(generateNextTransactionId(), "LOAN_DISBURSE", l.getPrincipal(), getCurrentTimestamp(), "APEX_TREASURY", target.getAccountNumber(), "LOAN", "NONE");
                        transactions.add(t);
                        storage.saveTransactions(transactions);
                    }
                    addNotification(l.getUsername(), "Loan Approved!", "Loan " + loanId + " approved. ₹" + l.getPrincipal() + " has been disbursed.");
                } else {
                    l.setStatus("REJECTED");
                    addNotification(l.getUsername(), "Loan Rejected", "Loan application " + loanId + " was declined.");
                }
                storage.saveLoans(loans);
                logAudit(actor, "LOAN_REVIEWED", "Loan " + loanId + " set to " + l.getStatus());
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized boolean repayLoanEmi(String loanId, String fromAccount, String pin)
            throws InsufficientFundsException, AccountFrozenException, InvalidAmountException {
        Loan targetLoan = null;
        for (Loan l : loans) {
            if (l.getId().equalsIgnoreCase(loanId)) {
                targetLoan = l;
                break;
            }
        }
        if (targetLoan == null || !targetLoan.isApproved() || targetLoan.getRemainingBalance() <= 0.0) {
            return false;
        }
        Account a = getAccountByNumber(fromAccount);
        if (a == null) {
            return false;
        }
        if (a.isFrozen()) {
            throw new AccountFrozenException("Account is frozen");
        }
        if (!verifyUserPin(a.getHolderName(), pin)) {
            return false;
        }
        double payment = targetLoan.getMonthlyEmi();
        if (payment > targetLoan.getRemainingBalance()) {
            payment = targetLoan.getRemainingBalance();
        }
        if (a.getBalance() - payment < a.getMinimumBalance()) {
            throw new InsufficientFundsException("Insufficient funds for EMI payment");
        }
        a.setBalance(a.getBalance() - payment);
        targetLoan.setRemainingBalance(targetLoan.getRemainingBalance() - payment);
        if (targetLoan.getRemainingBalance() <= 0.01) {
            targetLoan.setStatus("PAID");
            targetLoan.setRemainingBalance(0.0);
            addNotification(targetLoan.getUsername(), "Loan Fully Settled", "Congratulations! Loan " + targetLoan.getId() + " is paid off in full.");
        }
        storage.saveAccounts(accounts);
        storage.saveLoans(loans);
        Transaction t = new Transaction(generateNextTransactionId(), "LOAN_EMI", payment, getCurrentTimestamp(), fromAccount, "APEX_LOAN_POOL", "LOAN", "NONE");
        transactions.add(t);
        storage.saveTransactions(transactions);
        addNotification(targetLoan.getUsername(), "EMI Repayment Success", "Paid ₹" + String.format("%.2f", payment) + " towards loan " + targetLoan.getId());
        return true;
    }

    @Override
    public synchronized List<Loan> getLoansByUsername(String username) {
        List<Loan> list = new ArrayList<>();
        if (username == null) {
            return list;
        }
        for (Loan l : loans) {
            if (l.getUsername().equalsIgnoreCase(username)) {
                list.add(l);
            }
        }
        return list;
    }

    @Override
    public synchronized List<Loan> getAllLoans() {
        return new ArrayList<>(loans);
    }

    @Override
    public synchronized ChequeBookRequest requestChequeBook(String username, String accountNumber, int leaves) {
        int max = 1000;
        for (ChequeBookRequest c : chequeRequests) {
            if (c.getId().startsWith("CHQ")) {
                try {
                    int v = Integer.parseInt(c.getId().substring(3));
                    if (v > max) {
                        max = v;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        String id = "CHQ" + (max + 1);
        ChequeBookRequest req = new ChequeBookRequest(id, accountNumber, username, leaves, "REQUESTED", getCurrentDate());
        chequeRequests.add(req);
        storage.saveChequeBookRequests(chequeRequests);
        logAudit(username, "CHEQUE_REQUESTED", "Requested " + leaves + "-leaf chequebook for " + accountNumber);
        addNotification(username, "Chequebook Requested", "Request " + id + " for " + leaves + " leaves submitted.");
        return req;
    }

    @Override
    public synchronized boolean updateChequeStatus(String requestId, String status, String actor) {
        for (ChequeBookRequest c : chequeRequests) {
            if (c.getId().equalsIgnoreCase(requestId)) {
                c.setStatus(status.toUpperCase());
                storage.saveChequeBookRequests(chequeRequests);
                logAudit(actor, "CHEQUE_STATUS_UPDATE", "Cheque " + requestId + " updated to " + status);
                addNotification(c.getUsername(), "Chequebook Status Update", "Your chequebook request " + requestId + " is now: " + status);
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized List<ChequeBookRequest> getChequeRequestsByUsername(String username) {
        List<ChequeBookRequest> list = new ArrayList<>();
        if (username == null) {
            return list;
        }
        for (ChequeBookRequest c : chequeRequests) {
            if (c.getUsername().equalsIgnoreCase(username)) {
                list.add(c);
            }
        }
        return list;
    }

    @Override
    public synchronized List<ChequeBookRequest> getAllChequeRequests() {
        return new ArrayList<>(chequeRequests);
    }

    @Override
    public synchronized StandingInstruction createStandingInstruction(String username, String source, String target, double amount, String frequency) {
        int max = 1000;
        for (StandingInstruction s : standingInstructions) {
            if (s.getId().startsWith("SI")) {
                try {
                    int v = Integer.parseInt(s.getId().substring(2));
                    if (v > max) {
                        max = v;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        String id = "SI" + (max + 1);
        StandingInstruction si = new StandingInstruction(id, username, source, target, amount, frequency.toUpperCase(), "ACTIVE");
        standingInstructions.add(si);
        storage.saveStandingInstructions(standingInstructions);
        logAudit(username, "SI_CREATED", "Recurring instruction " + id + " for ₹" + amount + " " + frequency);
        addNotification(username, "Recurring Instruction Scheduled", "Scheduled " + frequency + " transfer of ₹" + amount + " to " + target);
        return si;
    }

    @Override
    public synchronized boolean cancelStandingInstruction(String instructionId, String username) {
        for (StandingInstruction s : standingInstructions) {
            if (s.getId().equalsIgnoreCase(instructionId) && s.getUsername().equalsIgnoreCase(username)) {
                s.setStatus("CANCELLED");
                storage.saveStandingInstructions(standingInstructions);
                logAudit(username, "SI_CANCELLED", "Cancelled recurring transfer " + instructionId);
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized List<StandingInstruction> getStandingInstructionsByUsername(String username) {
        List<StandingInstruction> list = new ArrayList<>();
        if (username == null) {
            return list;
        }
        for (StandingInstruction s : standingInstructions) {
            if (s.getUsername().equalsIgnoreCase(username)) {
                list.add(s);
            }
        }
        return list;
    }

    @Override
    public synchronized boolean executeStandingInstructions() {
        int count = 0;
        for (StandingInstruction s : standingInstructions) {
            if (s.isActive()) {
                Account src = getAccountByNumber(s.getSourceAccount());
                Account tgt = getAccountByNumber(s.getTargetAccount());
                if (src != null && tgt != null && !src.isFrozen() && !tgt.isFrozen()) {
                    if (src.getBalance() - s.getAmount() >= src.getMinimumBalance()) {
                        src.setBalance(src.getBalance() - s.getAmount());
                        tgt.setBalance(tgt.getBalance() + s.getAmount());
                        Transaction t = new Transaction(generateNextTransactionId(), "RECURRING_TRANSFER", s.getAmount(), getCurrentTimestamp(), src.getAccountNumber(), tgt.getAccountNumber(), "TRANSFER", "NONE");
                        transactions.add(t);
                        addNotification(src.getHolderName(), "Standing Instruction Executed", "Automated transfer of ₹" + s.getAmount() + " sent to " + tgt.getAccountNumber());
                        count++;
                    }
                }
            }
        }
        if (count > 0) {
            storage.saveAccounts(accounts);
            storage.saveTransactions(transactions);
            storage.saveNotifications(notifications);
            logAudit("SYSTEM", "SI_BATCH_RUN", "Executed " + count + " recurring standing instructions");
        }
        return count > 0;
    }

    @Override
    public synchronized boolean addBeneficiary(String username, String payeeName, String payeeAccountNumber, String nickname) {
        int max = 1000;
        for (Beneficiary b : beneficiaries) {
            if (b.getId().startsWith("BEN")) {
                try {
                    int v = Integer.parseInt(b.getId().substring(3));
                    if (v > max) {
                        max = v;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        String id = "BEN" + (max + 1);
        Beneficiary ben = new Beneficiary(id, username, payeeName, payeeAccountNumber, nickname);
        beneficiaries.add(ben);
        storage.saveBeneficiaries(beneficiaries);
        logAudit(username, "BENEFICIARY_ADDED", "Added payee " + payeeName + " (" + payeeAccountNumber + ")");
        return true;
    }

    @Override
    public synchronized boolean removeBeneficiary(String id, String username) {
        for (Beneficiary b : beneficiaries) {
            if (b.getId().equalsIgnoreCase(id) && b.getUsername().equalsIgnoreCase(username)) {
                beneficiaries.remove(b);
                storage.saveBeneficiaries(beneficiaries);
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized List<Beneficiary> getBeneficiariesByUsername(String username) {
        List<Beneficiary> list = new ArrayList<>();
        if (username == null) {
            return list;
        }
        for (Beneficiary b : beneficiaries) {
            if (b.getUsername().equalsIgnoreCase(username)) {
                list.add(b);
            }
        }
        return list;
    }

    @Override
    public synchronized SavingsGoal createGoal(String username, String title, double targetAmount, String targetDate) {
        int max = 1000;
        for (SavingsGoal g : savingsGoals) {
            if (g.getId().startsWith("GOL")) {
                try {
                    int v = Integer.parseInt(g.getId().substring(3));
                    if (v > max) {
                        max = v;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        String id = "GOL" + (max + 1);
        SavingsGoal goal = new SavingsGoal(id, username, title, targetAmount, 0.0, targetDate);
        savingsGoals.add(goal);
        storage.saveSavingsGoals(savingsGoals);
        addNotification(username, "Savings Goal Set", "Goal created: " + title + " (Target: ₹" + targetAmount + ")");
        return goal;
    }

    @Override
    public synchronized boolean contributeToGoal(String goalId, String fromAccount, double amount, String pin)
            throws InsufficientFundsException, AccountFrozenException, InvalidAmountException {
        if (amount <= 0.0) {
            throw new InvalidAmountException("Contribution must be positive");
        }
        SavingsGoal goal = null;
        for (SavingsGoal g : savingsGoals) {
            if (g.getId().equalsIgnoreCase(goalId)) {
                goal = g;
                break;
            }
        }
        if (goal == null) {
            return false;
        }
        Account a = getAccountByNumber(fromAccount);
        if (a == null) {
            return false;
        }
        if (a.isFrozen()) {
            throw new AccountFrozenException("Account is frozen");
        }
        if (!verifyUserPin(a.getHolderName(), pin)) {
            return false;
        }
        if (a.getBalance() - amount < a.getMinimumBalance()) {
            throw new InsufficientFundsException("Insufficient funds for goal savings");
        }
        a.setBalance(a.getBalance() - amount);
        goal.setCurrentAmount(goal.getCurrentAmount() + amount);
        storage.saveAccounts(accounts);
        storage.saveSavingsGoals(savingsGoals);
        Transaction t = new Transaction(generateNextTransactionId(), "GOAL_SAVING", amount, getCurrentTimestamp(), fromAccount, "GOAL_" + goal.getTitle(), "SAVINGS", "NONE");
        transactions.add(t);
        storage.saveTransactions(transactions);
        addNotification(goal.getUsername(), "Goal Progress Updated", "Saved ₹" + amount + " towards '" + goal.getTitle() + "'. Progress: " + String.format("%.1f", goal.getProgressPercentage()) + "%");
        return true;
    }

    @Override
    public synchronized List<SavingsGoal> getGoalsByUsername(String username) {
        List<SavingsGoal> list = new ArrayList<>();
        if (username == null) {
            return list;
        }
        for (SavingsGoal g : savingsGoals) {
            if (g.getUsername().equalsIgnoreCase(username)) {
                list.add(g);
            }
        }
        return list;
    }

    @Override
    public synchronized SupportTicket createTicket(String username, String subject, String description) {
        int max = 1000;
        for (SupportTicket t : supportTickets) {
            if (t.getId().startsWith("TCK")) {
                try {
                    int v = Integer.parseInt(t.getId().substring(3));
                    if (v > max) {
                        max = v;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        String id = "TCK" + (max + 1);
        SupportTicket t = new SupportTicket(id, username, subject, description, "OPEN", getCurrentDate(), "Awaiting staff response");
        supportTickets.add(t);
        storage.saveSupportTickets(supportTickets);
        logAudit(username, "TICKET_OPENED", "Opened support ticket " + id + ": " + subject);
        addNotification(username, "Support Ticket Raised", "Ticket " + id + " has been logged. Our helpdesk will respond shortly.");
        return t;
    }

    @Override
    public synchronized boolean replyTicket(String ticketId, String reply, String status, String actor) {
        for (SupportTicket t : supportTickets) {
            if (t.getId().equalsIgnoreCase(ticketId)) {
                t.setAdminReply(reply);
                t.setStatus(status.toUpperCase());
                storage.saveSupportTickets(supportTickets);
                logAudit(actor, "TICKET_REPLIED", "Updated ticket " + ticketId + " to " + status);
                addNotification(t.getUsername(), "Helpdesk Response", "Ticket " + ticketId + " has a new reply: " + reply);
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized List<SupportTicket> getTicketsByUsername(String username) {
        List<SupportTicket> list = new ArrayList<>();
        if (username == null) {
            return list;
        }
        for (SupportTicket t : supportTickets) {
            if (t.getUsername().equalsIgnoreCase(username)) {
                list.add(t);
            }
        }
        return list;
    }

    @Override
    public synchronized List<SupportTicket> getAllTickets() {
        return new ArrayList<>(supportTickets);
    }

    @Override
    public synchronized boolean submitFeedback(String username, int rating, String comment) {
        int max = 1000;
        for (Feedback f : feedbacks) {
            if (f.getId().startsWith("FB")) {
                try {
                    int v = Integer.parseInt(f.getId().substring(2));
                    if (v > max) {
                        max = v;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        String id = "FB" + (max + 1);
        Feedback fb = new Feedback(id, username, rating, comment, getCurrentDate());
        feedbacks.add(fb);
        storage.saveFeedback(feedbacks);
        logAudit(username, "FEEDBACK_SUBMITTED", "User submitted " + rating + "-star review");
        return true;
    }

    @Override
    public synchronized List<Feedback> getAllFeedback() {
        return new ArrayList<>(feedbacks);
    }

    @Override
    public synchronized List<Notification> getNotificationsByUsername(String username) {
        List<Notification> list = new ArrayList<>();
        if (username == null) {
            return list;
        }
        for (Notification n : notifications) {
            if (n.getUsername().equalsIgnoreCase(username)) {
                list.add(n);
            }
        }
        return list;
    }

    @Override
    public synchronized void addNotification(String username, String title, String message) {
        int max = 1000;
        for (Notification n : notifications) {
            if (n.getId().startsWith("NTF")) {
                try {
                    int v = Integer.parseInt(n.getId().substring(3));
                    if (v > max) {
                        max = v;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        String id = "NTF" + (max + 1);
        Notification notif = new Notification(id, username, title, message, getCurrentTimestamp(), false);
        notifications.add(0, notif);
        storage.saveNotifications(notifications);
    }

    @Override
    public synchronized List<Branch> getAllBranches() {
        return new ArrayList<>(branches);
    }

    @Override
    public synchronized boolean addBranch(String code, String name, String city, String manager, String actor) {
        Branch b = new Branch(code.toUpperCase(), name, city, manager);
        branches.add(b);
        storage.saveBranches(branches);
        logAudit(actor, "BRANCH_ADDED", "Added branch " + code + " in " + city);
        return true;
    }

    @Override
    public synchronized List<Employee> getAllEmployees() {
        return new ArrayList<>(employees);
    }

    @Override
    public synchronized boolean addEmployee(String username, String fullName, String role, String department, double salary, String actor) {
        int max = 1000;
        for (Employee e : employees) {
            if (e.getId().startsWith("EMP")) {
                try {
                    int v = Integer.parseInt(e.getId().substring(3));
                    if (v > max) {
                        max = v;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        String id = "EMP" + (max + 1);
        Employee emp = new Employee(id, username, fullName, role, department, salary, getCurrentDate());
        employees.add(emp);
        storage.saveEmployees(employees);
        logAudit(actor, "EMPLOYEE_ONBOARDED", "Onboarded " + fullName + " (" + role + ")");
        return true;
    }

    @Override
    public synchronized List<AuditLog> getAllAuditLogs() {
        return new ArrayList<>(auditLogs);
    }

    @Override
    public synchronized void logAudit(String actor, String action, String details) {
        int max = 1000;
        for (AuditLog a : auditLogs) {
            if (a.getId().startsWith("AUD")) {
                try {
                    int v = Integer.parseInt(a.getId().substring(3));
                    if (v > max) {
                        max = v;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        String id = "AUD" + (max + 1);
        AuditLog log = new AuditLog(id, actor, action, details, getCurrentTimestamp());
        auditLogs.add(0, log);
        storage.saveAuditLogs(auditLogs);
    }

    @Override
    public synchronized List<Transaction> getFraudAlerts() {
        List<Transaction> list = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getAmount() >= 5000.0) {
                list.add(t);
            }
        }
        return list;
    }

    @Override
    public synchronized String generateOtp(String username) {
        int code = 100000 + random.nextInt(900000);
        String otp = String.valueOf(code);
        otpStore.put(username.toLowerCase(), otp);
        return otp;
    }

    @Override
    public synchronized boolean verifyOtp(String username, String candidateOtp) {
        if (username == null || candidateOtp == null) {
            return false;
        }
        String saved = otpStore.get(username.toLowerCase());
        if (saved != null && saved.equals(candidateOtp.trim())) {
            otpStore.remove(username.toLowerCase());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public synchronized void resetDemoData() {
        users.clear();
        accounts.clear();
        transactions.clear();
        cards.clear();
        loans.clear();
        upiProfiles.clear();
        beneficiaries.clear();
        standingInstructions.clear();
        savingsGoals.clear();
        chequeRequests.clear();
        supportTickets.clear();
        feedbacks.clear();
        branches.clear();
        employees.clear();
        auditLogs.clear();
        notifications.clear();
        seedInitialData();
        saveAllData();
    }

    private void saveAllData() {
        storage.saveUsers(users);
        storage.saveAccounts(accounts);
        storage.saveTransactions(transactions);
        storage.saveCards(cards);
        storage.saveLoans(loans);
        storage.saveUpiProfiles(upiProfiles);
        storage.saveBeneficiaries(beneficiaries);
        storage.saveStandingInstructions(standingInstructions);
        storage.saveSavingsGoals(savingsGoals);
        storage.saveChequeBookRequests(chequeRequests);
        storage.saveSupportTickets(supportTickets);
        storage.saveFeedback(feedbacks);
        storage.saveBranches(branches);
        storage.saveEmployees(employees);
        storage.saveAuditLogs(auditLogs);
        storage.saveNotifications(notifications);
    }
}

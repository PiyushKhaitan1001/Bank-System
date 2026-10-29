package com.bank.service;

import com.bank.model.Account;
import com.bank.model.AccountFrozenException;
import com.bank.model.AuditLog;
import com.bank.model.Beneficiary;
import com.bank.model.Branch;
import com.bank.model.Card;
import com.bank.model.ChequeBookRequest;
import com.bank.model.DailyLimitExceededException;
import com.bank.model.Employee;
import com.bank.model.Feedback;
import com.bank.model.FixedDepositAccount;
import com.bank.model.InsufficientFundsException;
import com.bank.model.InvalidAmountException;
import com.bank.model.Loan;
import com.bank.model.Notification;
import com.bank.model.RecurringDepositAccount;
import com.bank.model.SavingsGoal;
import com.bank.model.StandingInstruction;
import com.bank.model.SupportTicket;
import com.bank.model.Transaction;
import com.bank.model.UpiProfile;
import com.bank.model.User;
import java.util.List;

public interface BankService {
    User authenticate(String username, String password);

    boolean register(String username, String password);

    boolean register(String username, String password, String email, String phone, String address, String pin);

    boolean createUserByStaff(String username, String password, String role, String email, String phone, String address, String pin, String actor);

    boolean updateProfile(String username, String email, String phone, String address);

    boolean changePassword(String username, String oldPassword, String newPassword);

    boolean resetPasswordWithSecurityQuestion(String username, String answer, String newPassword);

    boolean unlockUser(String username, String actor);

    boolean lockUser(String username, String actor);

    boolean submitKyc(String username, String documentName);

    boolean reviewKyc(String username, boolean approve, String actor);

    User getUserByUsername(String username);

    List<User> getAllUsers();

    Account openAccount(String username, String accountType, double initialDeposit);

    Account openAccountWithDetails(String username, String accountType, double initialDeposit, String jointHolder, String nomineeName, String nomineeRelation, String branchCode, boolean autoApprove);

    FixedDepositAccount openFixedDeposit(String username, double principal, int termMonths, String nomineeName, String nomineeRelation, String branchCode);

    RecurringDepositAccount openRecurringDeposit(String username, double monthlyInstallment, int termMonths, String nomineeName, String nomineeRelation, String branchCode);

    boolean approveAccount(String accountNumber, String actor);

    boolean rejectAccount(String accountNumber, String actor);

    List<Account> getAccountsByUsername(String username);

    List<Account> getAllAccounts();

    Account getAccountByNumber(String accountNumber);

    boolean freezeAccount(String accountNumber, String actor);

    boolean unfreezeAccount(String accountNumber, String actor);

    boolean deleteAccount(String accountNumber, String actor);

    int creditMonthlyInterestToAllSavings();

    boolean deposit(String accountNumber, double amount, String pin) throws InsufficientFundsException, AccountFrozenException, InvalidAmountException;

    boolean withdraw(String accountNumber, double amount, String pin) throws InsufficientFundsException, AccountFrozenException, InvalidAmountException, DailyLimitExceededException;

    boolean transfer(String sourceAccountNumber, String targetAccountNumber, double amount, String pin) throws InsufficientFundsException, AccountFrozenException, InvalidAmountException, DailyLimitExceededException;

    boolean payBill(String username, String accountNumber, String billerType, String billerRef, double amount, String pin) throws InsufficientFundsException, AccountFrozenException, InvalidAmountException;

    List<Transaction> getTransactionsByAccount(String accountNumber);

    List<Transaction> getTransactionsByUser(String username);

    List<Transaction> getTransactionsByUserAndFilter(String username, String typeFilter, String categoryFilter, String startDate, String endDate);

    List<Transaction> getAllTransactions();

    boolean disputeTransaction(String transactionId, String username);

    boolean reviewDispute(String transactionId, boolean approve, String actor);

    List<Card> getCardsByUsername(String username);

    Card getCardByNumber(String cardNumber);

    boolean updateCardLimit(String cardNumber, double newLimit);

    boolean toggleCardStatus(String cardNumber);

    UpiProfile getUpiProfileByUsername(String username);

    UpiProfile getUpiProfileById(String upiId);

    boolean updateUpiLimit(String upiId, double newLimit);

    boolean toggleUpiStatus(String upiId);

    boolean processUpiPayment(String fromUsername, String toUpiId, double amount, String pin) throws InsufficientFundsException, AccountFrozenException, InvalidAmountException, DailyLimitExceededException;

    Loan applyLoan(String username, String loanType, double principal, int termMonths, String disbursedAccount);

    boolean reviewLoan(String loanId, boolean approve, String actor);

    boolean repayLoanEmi(String loanId, String fromAccount, String pin) throws InsufficientFundsException, AccountFrozenException, InvalidAmountException;

    List<Loan> getLoansByUsername(String username);

    List<Loan> getAllLoans();

    ChequeBookRequest requestChequeBook(String username, String accountNumber, int leaves);

    boolean updateChequeStatus(String requestId, String status, String actor);

    List<ChequeBookRequest> getChequeRequestsByUsername(String username);

    List<ChequeBookRequest> getAllChequeRequests();

    StandingInstruction createStandingInstruction(String username, String source, String target, double amount, String frequency);

    boolean cancelStandingInstruction(String instructionId, String username);

    List<StandingInstruction> getStandingInstructionsByUsername(String username);

    boolean executeStandingInstructions();

    boolean addBeneficiary(String username, String payeeName, String payeeAccountNumber, String nickname);

    boolean removeBeneficiary(String id, String username);

    List<Beneficiary> getBeneficiariesByUsername(String username);

    SavingsGoal createGoal(String username, String title, double targetAmount, String targetDate);

    boolean contributeToGoal(String goalId, String fromAccount, double amount, String pin) throws InsufficientFundsException, AccountFrozenException, InvalidAmountException;

    List<SavingsGoal> getGoalsByUsername(String username);

    SupportTicket createTicket(String username, String subject, String description);

    boolean replyTicket(String ticketId, String reply, String status, String actor);

    List<SupportTicket> getTicketsByUsername(String username);

    List<SupportTicket> getAllTickets();

    boolean submitFeedback(String username, int rating, String comment);

    List<Feedback> getAllFeedback();

    List<Notification> getNotificationsByUsername(String username);

    void addNotification(String username, String title, String message);

    List<Branch> getAllBranches();

    boolean addBranch(String code, String name, String city, String manager, String actor);

    List<Employee> getAllEmployees();

    boolean addEmployee(String username, String fullName, String role, String department, double salary, String actor);

    List<AuditLog> getAllAuditLogs();

    void logAudit(String actor, String action, String details);

    List<Transaction> getFraudAlerts();

    String generateOtp(String username);

    boolean verifyOtp(String username, String candidateOtp);

    String hashPassword(String password);

    boolean verifyUserPin(String username, String pin);

    void resetDemoData();
}

package com.bank.storage;

import com.bank.model.Account;
import com.bank.model.AuditLog;
import com.bank.model.Beneficiary;
import com.bank.model.Branch;
import com.bank.model.Card;
import com.bank.model.ChequeBookRequest;
import com.bank.model.Employee;
import com.bank.model.Feedback;
import com.bank.model.Loan;
import com.bank.model.Notification;
import com.bank.model.SavingsGoal;
import com.bank.model.StandingInstruction;
import com.bank.model.SupportTicket;
import com.bank.model.Transaction;
import com.bank.model.UpiProfile;
import com.bank.model.User;
import java.util.List;

public interface Storage {
    List<User> loadUsers();

    void saveUsers(List<User> users);

    List<Account> loadAccounts();

    void saveAccounts(List<Account> accounts);

    List<Transaction> loadTransactions();

    void saveTransactions(List<Transaction> transactions);

    List<Card> loadCards();

    void saveCards(List<Card> cards);

    List<UpiProfile> loadUpiProfiles();

    void saveUpiProfiles(List<UpiProfile> upiProfiles);

    List<Loan> loadLoans();

    void saveLoans(List<Loan> loans);

    List<Beneficiary> loadBeneficiaries();

    void saveBeneficiaries(List<Beneficiary> beneficiaries);

    List<StandingInstruction> loadStandingInstructions();

    void saveStandingInstructions(List<StandingInstruction> instructions);

    List<ChequeBookRequest> loadChequeBookRequests();

    void saveChequeBookRequests(List<ChequeBookRequest> requests);

    List<SupportTicket> loadSupportTickets();

    void saveSupportTickets(List<SupportTicket> tickets);

    List<Feedback> loadFeedback();

    void saveFeedback(List<Feedback> feedbackList);

    List<SavingsGoal> loadSavingsGoals();

    void saveSavingsGoals(List<SavingsGoal> goals);

    List<Branch> loadBranches();

    void saveBranches(List<Branch> branches);

    List<Employee> loadEmployees();

    void saveEmployees(List<Employee> employees);

    List<AuditLog> loadAuditLogs();

    void saveAuditLogs(List<AuditLog> logs);

    List<Notification> loadNotifications();

    void saveNotifications(List<Notification> notifications);
}

package com.bank.storage;

import com.bank.model.Account;
import com.bank.model.AuditLog;
import com.bank.model.Beneficiary;
import com.bank.model.Branch;
import com.bank.model.Card;
import com.bank.model.ChequeBookRequest;
import com.bank.model.CurrentAccount;
import com.bank.model.Employee;
import com.bank.model.Feedback;
import com.bank.model.FixedDepositAccount;
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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileStorage implements Storage {
    private final String dataDir;
    private final String usersFile;
    private final String accountsFile;
    private final String transactionsFile;
    private final String cardsFile;
    private final String upiFile;
    private final String loansFile;
    private final String beneficiariesFile;
    private final String standingFile;
    private final String chequesFile;
    private final String ticketsFile;
    private final String feedbacksFile;
    private final String goalsFile;
    private final String branchesFile;
    private final String employeesFile;
    private final String auditFile;
    private final String notificationsFile;

    public FileStorage() {
        this("data");
    }

    public FileStorage(String dataDir) {
        this.dataDir = dataDir;
        this.usersFile = dataDir + File.separator + "users.csv";
        this.accountsFile = dataDir + File.separator + "accounts.csv";
        this.transactionsFile = dataDir + File.separator + "transactions.csv";
        this.cardsFile = dataDir + File.separator + "cards.csv";
        this.upiFile = dataDir + File.separator + "upi.csv";
        this.loansFile = dataDir + File.separator + "loans.csv";
        this.beneficiariesFile = dataDir + File.separator + "beneficiaries.csv";
        this.standingFile = dataDir + File.separator + "standing_instructions.csv";
        this.chequesFile = dataDir + File.separator + "cheques.csv";
        this.ticketsFile = dataDir + File.separator + "tickets.csv";
        this.feedbacksFile = dataDir + File.separator + "feedbacks.csv";
        this.goalsFile = dataDir + File.separator + "goals.csv";
        this.branchesFile = dataDir + File.separator + "branches.csv";
        this.employeesFile = dataDir + File.separator + "employees.csv";
        this.auditFile = dataDir + File.separator + "audit.csv";
        this.notificationsFile = dataDir + File.separator + "notifications.csv";
        ensureDirectoryExists();
    }

    private void ensureDirectoryExists() {
        File directory = new File(this.dataDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    @Override
    public List<User> loadUsers() {
        List<User> list = new ArrayList<>();
        File file = new File(usersFile);
        if (!file.exists()) {
            return list;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 13) {
                        int attempts = Integer.parseInt(parts[7].trim());
                        boolean locked = Boolean.parseBoolean(parts[8].trim());
                        list.add(new User(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(),
                                parts[4].trim(), parts[5].trim(), parts[6].trim(), attempts, locked,
                                parts[9].trim(), parts[10].trim(), parts[11].trim(), parts[12].trim()));
                    } else {
                        if (parts.length >= 7) {
                            list.add(new User(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(),
                                    parts[4].trim(), parts[5].trim(), parts[6].trim()));
                        } else {
                            if (parts.length >= 3) {
                                String u = parts[0].trim();
                                list.add(new User(u, parts[1].trim(), parts[2].trim(), u + "@apexbank.com", "+1-555-0100", "Main Street", ""));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading users: " + e.getMessage());
        }
        return list;
    }

    @Override
    public void saveUsers(List<User> users) {
        ensureDirectoryExists();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(usersFile))) {
            for (User u : users) {
                writer.write(u.getUsername() + ","
                        + u.getHashedPassword() + ","
                        + u.getRole() + ","
                        + u.getEmail() + ","
                        + u.getPhone() + ","
                        + u.getAddress() + ","
                        + u.getHashedPin() + ","
                        + u.getFailedLoginAttempts() + ","
                        + u.isLocked() + ","
                        + u.getSecurityQuestion() + ","
                        + u.getSecurityAnswer() + ","
                        + u.getKycStatus() + ","
                        + u.getKycDocument());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    @Override
    public List<Account> loadAccounts() {
        List<Account> list = new ArrayList<>();
        File file = new File(accountsFile);
        if (!file.exists()) {
            return list;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 10) {
                        String accNum = parts[0].trim();
                        String holder = parts[1].trim();
                        String joint = parts[2].trim();
                        double bal = Double.parseDouble(parts[3].trim());
                        String status = parts[4].trim();
                        String nomName = parts[5].trim();
                        String nomRel = parts[6].trim();
                        String branch = parts[7].trim();
                        String date = parts[8].trim();
                        String type = parts[9].trim();

                        if (type.equalsIgnoreCase("SAVINGS")) {
                            list.add(new SavingsAccount(accNum, holder, joint, bal, status, nomName, nomRel, branch, date));
                        } else {
                            if (type.equalsIgnoreCase("CURRENT")) {
                                double od = 1000.0;
                                if (parts.length >= 11) {
                                    od = Double.parseDouble(parts[10].trim());
                                }
                                list.add(new CurrentAccount(accNum, holder, joint, bal, status, nomName, nomRel, branch, date, od));
                            } else {
                                if (type.equalsIgnoreCase("FIXED_DEPOSIT")) {
                                    int term = Integer.parseInt(parts[10].trim());
                                    double rate = Double.parseDouble(parts[11].trim());
                                    String matDate = parts[12].trim();
                                    double matAmt = Double.parseDouble(parts[13].trim());
                                    list.add(new FixedDepositAccount(accNum, holder, joint, bal, status, nomName, nomRel, branch, date, term, rate, matDate, matAmt));
                                } else {
                                    if (type.equalsIgnoreCase("RECURRING_DEPOSIT")) {
                                        double install = Double.parseDouble(parts[10].trim());
                                        int term = Integer.parseInt(parts[11].trim());
                                        double rate = Double.parseDouble(parts[12].trim());
                                        String matDate = parts[13].trim();
                                        double matAmt = Double.parseDouble(parts[14].trim());
                                        int paid = Integer.parseInt(parts[15].trim());
                                        list.add(new RecurringDepositAccount(accNum, holder, joint, bal, status, nomName, nomRel, branch, date, install, term, rate, matDate, matAmt, paid));
                                    }
                                }
                            }
                        }
                    } else {
                        if (parts.length >= 5) {
                            String accNum = parts[0].trim();
                            String holder = parts[1].trim();
                            double bal = Double.parseDouble(parts[2].trim());
                            String type = parts[3].trim();
                            String status = parts[4].trim();
                            if (type.equalsIgnoreCase("SAVINGS")) {
                                list.add(new SavingsAccount(accNum, holder, bal, status));
                            } else {
                                list.add(new CurrentAccount(accNum, holder, bal, status));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading accounts: " + e.getMessage());
        }
        return list;
    }

    @Override
    public void saveAccounts(List<Account> accounts) {
        ensureDirectoryExists();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(accountsFile))) {
            for (Account a : accounts) {
                StringBuilder sb = new StringBuilder();
                sb.append(a.getAccountNumber()).append(",");
                sb.append(a.getHolderName()).append(",");
                sb.append(a.getJointHolderName()).append(",");
                sb.append(a.getBalance()).append(",");
                sb.append(a.getStatus()).append(",");
                sb.append(a.getNomineeName()).append(",");
                sb.append(a.getNomineeRelation()).append(",");
                sb.append(a.getBranchCode()).append(",");
                sb.append(a.getOpenedDate()).append(",");
                sb.append(a.getAccountType());

                if (a instanceof CurrentAccount) {
                    sb.append(",").append(a.getOverdraftLimit());
                } else {
                    if (a instanceof FixedDepositAccount) {
                        FixedDepositAccount fd = (FixedDepositAccount) a;
                        sb.append(",").append(fd.getTermMonths());
                        sb.append(",").append(fd.getInterestRate());
                        sb.append(",").append(fd.getMaturityDate());
                        sb.append(",").append(fd.getMaturityAmount());
                    } else {
                        if (a instanceof RecurringDepositAccount) {
                            RecurringDepositAccount rd = (RecurringDepositAccount) a;
                            sb.append(",").append(rd.getMonthlyInstallment());
                            sb.append(",").append(rd.getTermMonths());
                            sb.append(",").append(rd.getInterestRate());
                            sb.append(",").append(rd.getMaturityDate());
                            sb.append(",").append(rd.getMaturityAmount());
                            sb.append(",").append(rd.getMonthsPaid());
                        }
                    }
                }
                writer.write(sb.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving accounts: " + e.getMessage());
        }
    }

    @Override
    public List<Transaction> loadTransactions() {
        List<Transaction> list = new ArrayList<>();
        File file = new File(transactionsFile);
        if (!file.exists()) {
            return list;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 8) {
                        list.add(new Transaction(parts[0].trim(), parts[1].trim(), Double.parseDouble(parts[2].trim()),
                                parts[3].trim(), parts[4].trim(), parts[5].trim(), parts[6].trim(), parts[7].trim()));
                    } else {
                        if (parts.length >= 6) {
                            list.add(new Transaction(parts[0].trim(), parts[1].trim(), Double.parseDouble(parts[2].trim()),
                                    parts[3].trim(), parts[4].trim(), parts[5].trim(), "TRANSFER", "NONE"));
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading transactions: " + e.getMessage());
        }
        return list;
    }

    @Override
    public void saveTransactions(List<Transaction> transactions) {
        ensureDirectoryExists();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(transactionsFile))) {
            for (Transaction t : transactions) {
                writer.write(t.getId() + ","
                        + t.getType() + ","
                        + t.getAmount() + ","
                        + t.getTimestamp() + ","
                        + t.getSource() + ","
                        + t.getTarget() + ","
                        + t.getCategory() + ","
                        + t.getDisputeStatus());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving transactions: " + e.getMessage());
        }
    }

    @Override
    public List<Card> loadCards() {
        List<Card> list = new ArrayList<>();
        File file = new File(cardsFile);
        if (!file.exists()) {
            return list;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 8) {
                        list.add(new Card(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(),
                                parts[4].trim(), parts[5].trim(), Double.parseDouble(parts[6].trim()), Double.parseDouble(parts[7].trim())));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading cards: " + e.getMessage());
        }
        return list;
    }

    @Override
    public void saveCards(List<Card> cards) {
        ensureDirectoryExists();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cardsFile))) {
            for (Card c : cards) {
                writer.write(c.getCardNumber() + ","
                        + c.getAccountNumber() + ","
                        + c.getHolderName() + ","
                        + c.getExpiryDate() + ","
                        + c.getCvv() + ","
                        + c.getStatus() + ","
                        + c.getDailyLimit() + ","
                        + c.getUsedToday());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving cards: " + e.getMessage());
        }
    }

    @Override
    public List<UpiProfile> loadUpiProfiles() {
        List<UpiProfile> list = new ArrayList<>();
        File file = new File(upiFile);
        if (!file.exists()) {
            return list;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 6) {
                        list.add(new UpiProfile(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(),
                                Double.parseDouble(parts[4].trim()), Double.parseDouble(parts[5].trim())));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading upi: " + e.getMessage());
        }
        return list;
    }

    @Override
    public void saveUpiProfiles(List<UpiProfile> upiProfiles) {
        ensureDirectoryExists();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(upiFile))) {
            for (UpiProfile u : upiProfiles) {
                writer.write(u.getUpiId() + ","
                        + u.getAccountNumber() + ","
                        + u.getUsername() + ","
                        + u.getStatus() + ","
                        + u.getDailyLimit() + ","
                        + u.getUsedToday());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving upi: " + e.getMessage());
        }
    }

    @Override
    public List<Loan> loadLoans() {
        List<Loan> list = new ArrayList<>();
        File file = new File(loansFile);
        if (!file.exists()) {
            return list;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 11) {
                        list.add(new Loan(parts[0].trim(), parts[1].trim(), parts[2].trim(), Double.parseDouble(parts[3].trim()),
                                Double.parseDouble(parts[4].trim()), Integer.parseInt(parts[5].trim()), Double.parseDouble(parts[6].trim()),
                                Double.parseDouble(parts[7].trim()), parts[8].trim(), parts[9].trim(), parts[10].trim()));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading loans: " + e.getMessage());
        }
        return list;
    }

    @Override
    public void saveLoans(List<Loan> loans) {
        ensureDirectoryExists();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(loansFile))) {
            for (Loan l : loans) {
                writer.write(l.getId() + ","
                        + l.getUsername() + ","
                        + l.getLoanType() + ","
                        + l.getPrincipal() + ","
                        + l.getInterestRate() + ","
                        + l.getTermMonths() + ","
                        + l.getMonthlyEmi() + ","
                        + l.getRemainingBalance() + ","
                        + l.getStatus() + ","
                        + l.getAppliedDate() + ","
                        + l.getDisbursedAccountNumber());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving loans: " + e.getMessage());
        }
    }

    @Override
    public List<Beneficiary> loadBeneficiaries() {
        List<Beneficiary> list = new ArrayList<>();
        File file = new File(beneficiariesFile);
        if (!file.exists()) {
            return list;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 5) {
                        list.add(new Beneficiary(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(), parts[4].trim()));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading beneficiaries: " + e.getMessage());
        }
        return list;
    }

    @Override
    public void saveBeneficiaries(List<Beneficiary> beneficiaries) {
        ensureDirectoryExists();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(beneficiariesFile))) {
            for (Beneficiary b : beneficiaries) {
                writer.write(b.getId() + ","
                        + b.getUsername() + ","
                        + b.getPayeeName() + ","
                        + b.getPayeeAccountNumber() + ","
                        + b.getNickname());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving beneficiaries: " + e.getMessage());
        }
    }

    @Override
    public List<StandingInstruction> loadStandingInstructions() {
        List<StandingInstruction> list = new ArrayList<>();
        File file = new File(standingFile);
        if (!file.exists()) {
            return list;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 7) {
                        list.add(new StandingInstruction(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(),
                                Double.parseDouble(parts[4].trim()), parts[5].trim(), parts[6].trim()));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading standing instructions: " + e.getMessage());
        }
        return list;
    }

    @Override
    public void saveStandingInstructions(List<StandingInstruction> instructions) {
        ensureDirectoryExists();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(standingFile))) {
            for (StandingInstruction s : instructions) {
                writer.write(s.getId() + ","
                        + s.getUsername() + ","
                        + s.getSourceAccount() + ","
                        + s.getTargetAccount() + ","
                        + s.getAmount() + ","
                        + s.getFrequency() + ","
                        + s.getStatus());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving standing instructions: " + e.getMessage());
        }
    }

    @Override
    public List<ChequeBookRequest> loadChequeBookRequests() {
        List<ChequeBookRequest> list = new ArrayList<>();
        File file = new File(chequesFile);
        if (!file.exists()) {
            return list;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 6) {
                        list.add(new ChequeBookRequest(parts[0].trim(), parts[1].trim(), parts[2].trim(),
                                Integer.parseInt(parts[3].trim()), parts[4].trim(), parts[5].trim()));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading cheques: " + e.getMessage());
        }
        return list;
    }

    @Override
    public void saveChequeBookRequests(List<ChequeBookRequest> requests) {
        ensureDirectoryExists();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(chequesFile))) {
            for (ChequeBookRequest c : requests) {
                writer.write(c.getId() + ","
                        + c.getAccountNumber() + ","
                        + c.getUsername() + ","
                        + c.getLeavesCount() + ","
                        + c.getStatus() + ","
                        + c.getRequestDate());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving cheques: " + e.getMessage());
        }
    }

    @Override
    public List<SupportTicket> loadSupportTickets() {
        List<SupportTicket> list = new ArrayList<>();
        File file = new File(ticketsFile);
        if (!file.exists()) {
            return list;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 7) {
                        list.add(new SupportTicket(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(),
                                parts[4].trim(), parts[5].trim(), parts[6].trim()));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading tickets: " + e.getMessage());
        }
        return list;
    }

    @Override
    public void saveSupportTickets(List<SupportTicket> tickets) {
        ensureDirectoryExists();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ticketsFile))) {
            for (SupportTicket t : tickets) {
                writer.write(t.getId() + ","
                        + t.getUsername() + ","
                        + t.getSubject() + ","
                        + t.getDescription() + ","
                        + t.getStatus() + ","
                        + t.getCreatedDate() + ","
                        + t.getAdminReply());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving tickets: " + e.getMessage());
        }
    }

    @Override
    public List<Feedback> loadFeedback() {
        List<Feedback> list = new ArrayList<>();
        File file = new File(feedbacksFile);
        if (!file.exists()) {
            return list;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 5) {
                        list.add(new Feedback(parts[0].trim(), parts[1].trim(), Integer.parseInt(parts[2].trim()), parts[3].trim(), parts[4].trim()));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading feedback: " + e.getMessage());
        }
        return list;
    }

    @Override
    public void saveFeedback(List<Feedback> feedbackList) {
        ensureDirectoryExists();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(feedbacksFile))) {
            for (Feedback f : feedbackList) {
                writer.write(f.getId() + ","
                        + f.getUsername() + ","
                        + f.getRating() + ","
                        + f.getComment() + ","
                        + f.getSubmittedDate());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving feedback: " + e.getMessage());
        }
    }

    @Override
    public List<SavingsGoal> loadSavingsGoals() {
        List<SavingsGoal> list = new ArrayList<>();
        File file = new File(goalsFile);
        if (!file.exists()) {
            return list;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 6) {
                        list.add(new SavingsGoal(parts[0].trim(), parts[1].trim(), parts[2].trim(),
                                Double.parseDouble(parts[3].trim()), Double.parseDouble(parts[4].trim()), parts[5].trim()));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading goals: " + e.getMessage());
        }
        return list;
    }

    @Override
    public void saveSavingsGoals(List<SavingsGoal> goals) {
        ensureDirectoryExists();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(goalsFile))) {
            for (SavingsGoal g : goals) {
                writer.write(g.getId() + ","
                        + g.getUsername() + ","
                        + g.getTitle() + ","
                        + g.getTargetAmount() + ","
                        + g.getCurrentAmount() + ","
                        + g.getTargetDate());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving goals: " + e.getMessage());
        }
    }

    @Override
    public List<Branch> loadBranches() {
        List<Branch> list = new ArrayList<>();
        File file = new File(branchesFile);
        if (!file.exists()) {
            return list;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 4) {
                        list.add(new Branch(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim()));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading branches: " + e.getMessage());
        }
        return list;
    }

    @Override
    public void saveBranches(List<Branch> branches) {
        ensureDirectoryExists();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(branchesFile))) {
            for (Branch b : branches) {
                writer.write(b.getCode() + ","
                        + b.getName() + ","
                        + b.getCity() + ","
                        + b.getManager());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving branches: " + e.getMessage());
        }
    }

    @Override
    public List<Employee> loadEmployees() {
        List<Employee> list = new ArrayList<>();
        File file = new File(employeesFile);
        if (!file.exists()) {
            return list;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 7) {
                        list.add(new Employee(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(),
                                parts[4].trim(), Double.parseDouble(parts[5].trim()), parts[6].trim()));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading employees: " + e.getMessage());
        }
        return list;
    }

    @Override
    public void saveEmployees(List<Employee> employees) {
        ensureDirectoryExists();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(employeesFile))) {
            for (Employee emp : employees) {
                writer.write(emp.getId() + ","
                        + emp.getUsername() + ","
                        + emp.getFullName() + ","
                        + emp.getRole() + ","
                        + emp.getDepartment() + ","
                        + emp.getSalary() + ","
                        + emp.getJoinDate());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving employees: " + e.getMessage());
        }
    }

    @Override
    public List<AuditLog> loadAuditLogs() {
        List<AuditLog> list = new ArrayList<>();
        File file = new File(auditFile);
        if (!file.exists()) {
            return list;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split(",", 5);
                    if (parts.length >= 5) {
                        list.add(new AuditLog(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(), parts[4].trim()));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading audit: " + e.getMessage());
        }
        return list;
    }

    @Override
    public void saveAuditLogs(List<AuditLog> logs) {
        ensureDirectoryExists();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(auditFile))) {
            for (AuditLog log : logs) {
                writer.write(log.getId() + ","
                        + log.getActor() + ","
                        + log.getAction() + ","
                        + log.getDetails() + ","
                        + log.getTimestamp());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving audit: " + e.getMessage());
        }
    }

    @Override
    public List<Notification> loadNotifications() {
        List<Notification> list = new ArrayList<>();
        File file = new File(notificationsFile);
        if (!file.exists()) {
            return list;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split(",", 6);
                    if (parts.length >= 6) {
                        list.add(new Notification(parts[0].trim(), parts[1].trim(), parts[2].trim(),
                                parts[3].trim(), parts[4].trim(), Boolean.parseBoolean(parts[5].trim())));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading notifications: " + e.getMessage());
        }
        return list;
    }

    @Override
    public void saveNotifications(List<Notification> notifications) {
        ensureDirectoryExists();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(notificationsFile))) {
            for (Notification n : notifications) {
                writer.write(n.getId() + ","
                        + n.getUsername() + ","
                        + n.getTitle() + ","
                        + n.getMessage() + ","
                        + n.getTimestamp() + ","
                        + n.isRead());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving notifications: " + e.getMessage());
        }
    }
}

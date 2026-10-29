package com.bank.model;

public abstract class Account {
    private String accountNumber;
    private String holderName;
    private String jointHolderName;
    private double balance;
    private String status;
    private String nomineeName;
    private String nomineeRelation;
    private String branchCode;
    private String openedDate;

    public Account(String accountNumber, String holderName, double balance, String status) {
        this(accountNumber, holderName, "None", balance, status, "None", "None", "APEX0001", "2026-10-24");
    }

    public Account(String accountNumber, String holderName, String jointHolderName, double balance, String status,
                   String nomineeName, String nomineeRelation, String branchCode, String openedDate) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.jointHolderName = jointHolderName;
        this.balance = balance;
        this.status = status;
        this.nomineeName = nomineeName;
        this.nomineeRelation = nomineeRelation;
        this.branchCode = branchCode;
        this.openedDate = openedDate;
    }

    public abstract double calculateInterest();

    public abstract double calculateMonthlyInterest();

    public abstract String getAccountType();

    public abstract double getMinimumBalance();

    public abstract double getOverdraftLimit();

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public String getJointHolderName() {
        return jointHolderName;
    }

    public void setJointHolderName(String jointHolderName) {
        this.jointHolderName = jointHolderName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNomineeName() {
        return nomineeName;
    }

    public void setNomineeName(String nomineeName) {
        this.nomineeName = nomineeName;
    }

    public String getNomineeRelation() {
        return nomineeRelation;
    }

    public void setNomineeRelation(String nomineeRelation) {
        this.nomineeRelation = nomineeRelation;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getOpenedDate() {
        return openedDate;
    }

    public void setOpenedDate(String openedDate) {
        this.openedDate = openedDate;
    }

    public boolean isFrozen() {
        if (this.status != null && this.status.equalsIgnoreCase("FROZEN")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isPending() {
        if (this.status != null && this.status.equalsIgnoreCase("PENDING_APPROVAL")) {
            return true;
        } else {
            return false;
        }
    }
}

package com.bank.model;

public class CurrentAccount extends Account {
    private static final double MINIMUM_BALANCE = 0.0;
    private static final double INTEREST_RATE = 0.01;
    private double overdraftLimit;

    public CurrentAccount(String accountNumber, String holderName, double balance, String status) {
        this(accountNumber, holderName, "None", balance, status, "None", "None", "APEX0001", "2026-10-24", 1000.0);
    }

    public CurrentAccount(String accountNumber, String holderName, String jointHolderName, double balance, String status,
                          String nomineeName, String nomineeRelation, String branchCode, String openedDate, double overdraftLimit) {
        super(accountNumber, holderName, jointHolderName, balance, status, nomineeName, nomineeRelation, branchCode, openedDate);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public double calculateInterest() {
        return getBalance() * INTEREST_RATE;
    }

    @Override
    public double calculateMonthlyInterest() {
        return (getBalance() * INTEREST_RATE) / 12.0;
    }

    @Override
    public String getAccountType() {
        return "CURRENT";
    }

    @Override
    public double getMinimumBalance() {
        return MINIMUM_BALANCE;
    }

    @Override
    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }
}

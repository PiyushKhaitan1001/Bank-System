package com.bank.model;

public class SavingsAccount extends Account {
    private static final double MINIMUM_BALANCE = 500.0;
    private static final double INTEREST_RATE = 0.04;

    public SavingsAccount(String accountNumber, String holderName, double balance, String status) {
        super(accountNumber, holderName, balance, status);
    }

    public SavingsAccount(String accountNumber, String holderName, String jointHolderName, double balance, String status,
                          String nomineeName, String nomineeRelation, String branchCode, String openedDate) {
        super(accountNumber, holderName, jointHolderName, balance, status, nomineeName, nomineeRelation, branchCode, openedDate);
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
        return "SAVINGS";
    }

    @Override
    public double getMinimumBalance() {
        return MINIMUM_BALANCE;
    }

    @Override
    public double getOverdraftLimit() {
        return 0.0;
    }
}

package com.bank.model;

public class RecurringDepositAccount extends Account {
    private double monthlyInstallment;
    private int termMonths;
    private double interestRate;
    private String maturityDate;
    private double maturityAmount;
    private int monthsPaid;

    public RecurringDepositAccount(String accountNumber, String holderName, String jointHolderName, double balance,
                                   String status, String nomineeName, String nomineeRelation, String branchCode,
                                   String openedDate, double monthlyInstallment, int termMonths, double interestRate,
                                   String maturityDate, double maturityAmount, int monthsPaid) {
        super(accountNumber, holderName, jointHolderName, balance, status, nomineeName, nomineeRelation, branchCode, openedDate);
        this.monthlyInstallment = monthlyInstallment;
        this.termMonths = termMonths;
        this.interestRate = interestRate;
        this.maturityDate = maturityDate;
        this.maturityAmount = maturityAmount;
        this.monthsPaid = monthsPaid;
    }

    @Override
    public double calculateInterest() {
        return maturityAmount - (monthlyInstallment * termMonths);
    }

    @Override
    public double calculateMonthlyInterest() {
        return calculateInterest() / termMonths;
    }

    @Override
    public String getAccountType() {
        return "RECURRING_DEPOSIT";
    }

    @Override
    public double getMinimumBalance() {
        return getBalance();
    }

    @Override
    public double getOverdraftLimit() {
        return 0.0;
    }

    public double getMonthlyInstallment() {
        return monthlyInstallment;
    }

    public void setMonthlyInstallment(double monthlyInstallment) {
        this.monthlyInstallment = monthlyInstallment;
    }

    public int getTermMonths() {
        return termMonths;
    }

    public void setTermMonths(int termMonths) {
        this.termMonths = termMonths;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public String getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(String maturityDate) {
        this.maturityDate = maturityDate;
    }

    public double getMaturityAmount() {
        return maturityAmount;
    }

    public void setMaturityAmount(double maturityAmount) {
        this.maturityAmount = maturityAmount;
    }

    public int getMonthsPaid() {
        return monthsPaid;
    }

    public void setMonthsPaid(int monthsPaid) {
        this.monthsPaid = monthsPaid;
    }
}

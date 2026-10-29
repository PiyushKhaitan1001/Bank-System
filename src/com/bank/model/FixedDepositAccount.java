package com.bank.model;

public class FixedDepositAccount extends Account {
    private double principalAmount;
    private int termMonths;
    private double interestRate;
    private String maturityDate;
    private double maturityAmount;

    public FixedDepositAccount(String accountNumber, String holderName, String jointHolderName, double principalAmount,
                               String status, String nomineeName, String nomineeRelation, String branchCode,
                               String openedDate, int termMonths, double interestRate, String maturityDate, double maturityAmount) {
        super(accountNumber, holderName, jointHolderName, principalAmount, status, nomineeName, nomineeRelation, branchCode, openedDate);
        this.principalAmount = principalAmount;
        this.termMonths = termMonths;
        this.interestRate = interestRate;
        this.maturityDate = maturityDate;
        this.maturityAmount = maturityAmount;
    }

    @Override
    public double calculateInterest() {
        return maturityAmount - principalAmount;
    }

    @Override
    public double calculateMonthlyInterest() {
        return (maturityAmount - principalAmount) / termMonths;
    }

    @Override
    public String getAccountType() {
        return "FIXED_DEPOSIT";
    }

    @Override
    public double getMinimumBalance() {
        return principalAmount;
    }

    @Override
    public double getOverdraftLimit() {
        return 0.0;
    }

    public double getPrincipalAmount() {
        return principalAmount;
    }

    public void setPrincipalAmount(double principalAmount) {
        this.principalAmount = principalAmount;
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
}

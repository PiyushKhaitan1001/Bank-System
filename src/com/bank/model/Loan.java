package com.bank.model;

public class Loan {
    private String id;
    private String username;
    private String loanType;
    private double principal;
    private double interestRate;
    private int termMonths;
    private double monthlyEmi;
    private double remainingBalance;
    private String status;
    private String appliedDate;
    private String disbursedAccountNumber;

    public Loan(String id, String username, String loanType, double principal, double interestRate, int termMonths,
                double monthlyEmi, double remainingBalance, String status, String appliedDate, String disbursedAccountNumber) {
        this.id = id;
        this.username = username;
        this.loanType = loanType;
        this.principal = principal;
        this.interestRate = interestRate;
        this.termMonths = termMonths;
        this.monthlyEmi = monthlyEmi;
        this.remainingBalance = remainingBalance;
        this.status = status;
        this.appliedDate = appliedDate;
        this.disbursedAccountNumber = disbursedAccountNumber;
    }

    public static double computeEmi(double principal, double annualRate, int months) {
        if (principal <= 0.0 || months <= 0) {
            return 0.0;
        }
        double monthlyRate = annualRate / 12.0;
        double factor = Math.pow(1.0 + monthlyRate, months);
        if (factor - 1.0 == 0.0) {
            return principal / months;
        }
        return (principal * monthlyRate * factor) / (factor - 1.0);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public double getPrincipal() {
        return principal;
    }

    public void setPrincipal(double principal) {
        this.principal = principal;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public int getTermMonths() {
        return termMonths;
    }

    public void setTermMonths(int termMonths) {
        this.termMonths = termMonths;
    }

    public double getMonthlyEmi() {
        return monthlyEmi;
    }

    public void setMonthlyEmi(double monthlyEmi) {
        this.monthlyEmi = monthlyEmi;
    }

    public double getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(double remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(String appliedDate) {
        this.appliedDate = appliedDate;
    }

    public String getDisbursedAccountNumber() {
        return disbursedAccountNumber;
    }

    public void setDisbursedAccountNumber(String disbursedAccountNumber) {
        this.disbursedAccountNumber = disbursedAccountNumber;
    }

    public boolean isPending() {
        if (this.status != null && this.status.equalsIgnoreCase("PENDING")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isApproved() {
        if (this.status != null && this.status.equalsIgnoreCase("APPROVED")) {
            return true;
        } else {
            return false;
        }
    }
}

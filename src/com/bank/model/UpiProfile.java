package com.bank.model;

public class UpiProfile {
    private String upiId;
    private String accountNumber;
    private String username;
    private String status;
    private double dailyLimit;
    private double usedToday;

    public UpiProfile(String upiId, String accountNumber, String username, String status, double dailyLimit, double usedToday) {
        this.upiId = upiId;
        this.accountNumber = accountNumber;
        this.username = username;
        this.status = status;
        this.dailyLimit = dailyLimit;
        this.usedToday = usedToday;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getDailyLimit() {
        return dailyLimit;
    }

    public void setDailyLimit(double dailyLimit) {
        this.dailyLimit = dailyLimit;
    }

    public double getUsedToday() {
        return usedToday;
    }

    public void setUsedToday(double usedToday) {
        this.usedToday = usedToday;
    }

    public boolean isFrozen() {
        if (this.status != null && this.status.equalsIgnoreCase("FROZEN")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean canSpend(double amount) {
        if (amount <= 0.0) {
            return false;
        }
        if (isFrozen()) {
            return false;
        }
        if (this.usedToday + amount > this.dailyLimit) {
            return false;
        }
        return true;
    }
}

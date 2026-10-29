package com.bank.model;

public class Card {
    private String cardNumber;
    private String accountNumber;
    private String holderName;
    private String expiryDate;
    private String cvv;
    private String status;
    private double dailyLimit;
    private double usedToday;

    public Card(String cardNumber, String accountNumber, String holderName, String expiryDate, String cvv, String status, double dailyLimit, double usedToday) {
        this.cardNumber = cardNumber;
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.status = status;
        this.dailyLimit = dailyLimit;
        this.usedToday = usedToday;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

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

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
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

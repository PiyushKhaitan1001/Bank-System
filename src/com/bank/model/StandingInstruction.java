package com.bank.model;

public class StandingInstruction {
    private String id;
    private String username;
    private String sourceAccount;
    private String targetAccount;
    private double amount;
    private String frequency;
    private String status;

    public StandingInstruction(String id, String username, String sourceAccount, String targetAccount, double amount, String frequency, String status) {
        this.id = id;
        this.username = username;
        this.sourceAccount = sourceAccount;
        this.targetAccount = targetAccount;
        this.amount = amount;
        this.frequency = frequency;
        this.status = status;
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

    public String getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(String sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public String getTargetAccount() {
        return targetAccount;
    }

    public void setTargetAccount(String targetAccount) {
        this.targetAccount = targetAccount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isActive() {
        if (this.status != null && this.status.equalsIgnoreCase("ACTIVE")) {
            return true;
        } else {
            return false;
        }
    }
}

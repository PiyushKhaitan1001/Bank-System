package com.bank.model;

public class SavingsGoal {
    private String id;
    private String username;
    private String title;
    private double targetAmount;
    private double currentAmount;
    private String targetDate;

    public SavingsGoal(String id, String username, String title, double targetAmount, double currentAmount, String targetDate) {
        this.id = id;
        this.username = username;
        this.title = title;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.targetDate = targetDate;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public double getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(double currentAmount) {
        this.currentAmount = currentAmount;
    }

    public String getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(String targetDate) {
        this.targetDate = targetDate;
    }

    public double getProgressPercentage() {
        if (targetAmount <= 0.0) {
            return 100.0;
        }
        double pct = (currentAmount / targetAmount) * 100.0;
        if (pct > 100.0) {
            return 100.0;
        } else {
            return pct;
        }
    }
}

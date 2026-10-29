package com.bank.model;

public class Transaction {
    private String id;
    private String type;
    private double amount;
    private String timestamp;
    private String source;
    private String target;
    private String category;
    private String disputeStatus;

    public Transaction(String id, String type, double amount, String timestamp, String source, String target) {
        this(id, type, amount, timestamp, source, target, "TRANSFER", "NONE");
    }

    public Transaction(String id, String type, double amount, String timestamp, String source, String target, String category, String disputeStatus) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
        this.source = source;
        this.target = target;
        this.category = category;
        this.disputeStatus = disputeStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDisputeStatus() {
        return disputeStatus;
    }

    public void setDisputeStatus(String disputeStatus) {
        this.disputeStatus = disputeStatus;
    }

    public boolean isDisputed() {
        if (this.disputeStatus != null && this.disputeStatus.equalsIgnoreCase("PENDING")) {
            return true;
        } else {
            return false;
        }
    }
}

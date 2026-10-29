package com.bank.model;

public class ChequeBookRequest {
    private String id;
    private String accountNumber;
    private String username;
    private int leavesCount;
    private String status;
    private String requestDate;

    public ChequeBookRequest(String id, String accountNumber, String username, int leavesCount, String status, String requestDate) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.username = username;
        this.leavesCount = leavesCount;
        this.status = status;
        this.requestDate = requestDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getLeavesCount() {
        return leavesCount;
    }

    public void setLeavesCount(int leavesCount) {
        this.leavesCount = leavesCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }
}

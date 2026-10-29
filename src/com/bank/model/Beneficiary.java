package com.bank.model;

public class Beneficiary {
    private String id;
    private String username;
    private String payeeName;
    private String payeeAccountNumber;
    private String nickname;

    public Beneficiary(String id, String username, String payeeName, String payeeAccountNumber, String nickname) {
        this.id = id;
        this.username = username;
        this.payeeName = payeeName;
        this.payeeAccountNumber = payeeAccountNumber;
        this.nickname = nickname;
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

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getPayeeAccountNumber() {
        return payeeAccountNumber;
    }

    public void setPayeeAccountNumber(String payeeAccountNumber) {
        this.payeeAccountNumber = payeeAccountNumber;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}

package com.bank.model;

public class User {
    private String username;
    private String hashedPassword;
    private String role;
    private String email;
    private String phone;
    private String address;
    private String hashedPin;
    private int failedLoginAttempts;
    private boolean locked;
    private String securityQuestion;
    private String securityAnswer;
    private String kycStatus;
    private String kycDocument;

    public User(String username, String hashedPassword, String role) {
        this(username, hashedPassword, role, username + "@apexbank.com", "N/A", "N/A", "", 0, false, "What is your primary bank?", "apex", "APPROVED", "Default KYC");
    }

    public User(String username, String hashedPassword, String role, String email, String phone, String address, String hashedPin) {
        this(username, hashedPassword, role, email, phone, address, hashedPin, 0, false, "What is your primary bank?", "apex", "APPROVED", "Default KYC");
    }

    public User(String username, String hashedPassword, String role, String email, String phone, String address, String hashedPin,
                int failedLoginAttempts, boolean locked, String securityQuestion, String securityAnswer, String kycStatus, String kycDocument) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.role = role;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.hashedPin = hashedPin;
        this.failedLoginAttempts = failedLoginAttempts;
        this.locked = locked;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
        this.kycStatus = kycStatus;
        this.kycDocument = kycDocument;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHashedPin() {
        return hashedPin;
    }

    public void setHashedPin(String hashedPin) {
        this.hashedPin = hashedPin;
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public void setFailedLoginAttempts(int failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public String getKycStatus() {
        return kycStatus;
    }

    public void setKycStatus(String kycStatus) {
        this.kycStatus = kycStatus;
    }

    public String getKycDocument() {
        return kycDocument;
    }

    public void setKycDocument(String kycDocument) {
        this.kycDocument = kycDocument;
    }

    public boolean isAdmin() {
        if (this.role != null && this.role.equalsIgnoreCase("ADMIN")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isManager() {
        if (isAdmin()) {
            return true;
        } else {
            if (this.role != null && this.role.equalsIgnoreCase("MANAGER")) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean isStaff() {
        if (isManager()) {
            return true;
        } else {
            if (this.role != null && this.role.equalsIgnoreCase("TELLER")) {
                return true;
            } else {
                return false;
            }
        }
    }
}

package com.bank.model;

public class SupportTicket {
    private String id;
    private String username;
    private String subject;
    private String description;
    private String status;
    private String createdDate;
    private String adminReply;

    public SupportTicket(String id, String username, String subject, String description, String status, String createdDate, String adminReply) {
        this.id = id;
        this.username = username;
        this.subject = subject;
        this.description = description;
        this.status = status;
        this.createdDate = createdDate;
        this.adminReply = adminReply;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getAdminReply() {
        return adminReply;
    }

    public void setAdminReply(String adminReply) {
        this.adminReply = adminReply;
    }
}

package com.bank.model;

public class AuditLog {
    private String id;
    private String actor;
    private String action;
    private String details;
    private String timestamp;

    public AuditLog(String id, String actor, String action, String details, String timestamp) {
        this.id = id;
        this.actor = actor;
        this.action = action;
        this.details = details;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

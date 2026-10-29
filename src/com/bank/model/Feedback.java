package com.bank.model;

public class Feedback {
    private String id;
    private String username;
    private int rating;
    private String comment;
    private String submittedDate;

    public Feedback(String id, String username, int rating, String comment, String submittedDate) {
        this.id = id;
        this.username = username;
        this.rating = rating;
        this.comment = comment;
        this.submittedDate = submittedDate;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(String submittedDate) {
        this.submittedDate = submittedDate;
    }
}

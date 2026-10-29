package com.bank.model;

public class Employee {
    private String id;
    private String username;
    private String fullName;
    private String role;
    private String department;
    private double salary;
    private String joinDate;

    public Employee(String id, String username, String fullName, String role, String department, double salary, String joinDate) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.role = role;
        this.department = department;
        this.salary = salary;
        this.joinDate = joinDate;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }
}

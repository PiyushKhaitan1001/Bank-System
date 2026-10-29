package com.bank.model;

public class Branch {
    private String code;
    private String name;
    private String city;
    private String manager;

    public Branch(String code, String name, String city, String manager) {
        this.code = code;
        this.name = name;
        this.city = city;
        this.manager = manager;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }
}

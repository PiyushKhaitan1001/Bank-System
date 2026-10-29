package com.bank.model;

public class AccountFrozenException extends Exception {
    public AccountFrozenException(String message) {
        super(message);
    }
}

package com.papail.exceptions;

public class AccountNotFoundException extends Exception {
    public AccountNotFoundException(String accountId) {
        super(String.format("Account %s not found.", accountId));
    }
}

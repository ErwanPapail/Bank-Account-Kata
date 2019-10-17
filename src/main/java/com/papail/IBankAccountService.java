package com.papail;

import com.papail.exceptions.AccountNotFoundException;
import com.papail.exceptions.InsufficientFundsException;
import com.papail.exceptions.InvalidAmountException;
import com.papail.printer.IStatementPrinter;

import java.math.BigDecimal;
import java.time.Instant;

public interface IBankAccountService {
    void makeDeposit(final String accountId, final BigDecimal moneyAmount, final Instant date) throws InvalidAmountException, AccountNotFoundException;
    void makeWithdraw(final String accountId, final BigDecimal moneyAmount, final Instant date) throws InvalidAmountException, AccountNotFoundException, InsufficientFundsException;
    void printStatement(final IStatementPrinter printer, final String accountId);
}

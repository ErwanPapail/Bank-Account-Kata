package com.papail;

import com.papail.exceptions.AccountNotFoundException;
import com.papail.exceptions.InsufficientFundsException;
import com.papail.exceptions.InvalidAmountException;
import com.papail.models.Operation;
import com.papail.printer.IStatementPrinter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;

public class BankAccountService implements IBankAccountService{
    private final IOperationRepository operationRepository;

    BankAccountService(final IOperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    public void makeDeposit(final String accountId, final BigDecimal moneyAmount, final Instant date) throws InvalidAmountException, AccountNotFoundException {
        throwExceptionIfAccountDoesNotExists(accountId);
        throwExceptionIfAmountIsInvalid(moneyAmount);

        final var balance = operationRepository.getBalance(accountId);
        final var balanceAfterDeposit = balance.add(moneyAmount);
        final var deposit = Operation.createDeposit(date, moneyAmount, balanceAfterDeposit);

        operationRepository.add(accountId, deposit);
    }

    public void makeWithdraw(final String accountId, final BigDecimal moneyAmount, final Instant date) throws InvalidAmountException, AccountNotFoundException, InsufficientFundsException {
        throwExceptionIfAccountDoesNotExists(accountId);
        throwExceptionIfAmountIsInvalid(moneyAmount);

        final var balance = operationRepository.getBalance(accountId);
        final var balanceAfterWithdraw = balance.subtract(moneyAmount);

        throwExceptionIfFundsAreInsufficient(balanceAfterWithdraw);

        final var withdraw = Operation.createWithdraw(date, moneyAmount, balanceAfterWithdraw);

        operationRepository.add(accountId, withdraw);
    }

    private void throwExceptionIfAmountIsInvalid(final BigDecimal moneyAmount) throws InvalidAmountException {
        if (moneyAmount.compareTo(BigDecimal.ZERO) <= 0) { throw new InvalidAmountException(); }
    }

    private void throwExceptionIfFundsAreInsufficient(final BigDecimal balanceAfterOperation) throws InsufficientFundsException {
        if (balanceAfterOperation.compareTo(BigDecimal.ZERO) < 0) { throw new InsufficientFundsException(); }
    }

    private void throwExceptionIfAccountDoesNotExists(final String accountId) throws AccountNotFoundException {
        if(!this.operationRepository.doesAccountExist(accountId)) { throw new AccountNotFoundException(accountId); }
    }

    public void printStatement(final IStatementPrinter printer, final String accountId) {
        final var operations = Collections.unmodifiableList(operationRepository.find(accountId));

        printer.printStatement(operations);
    }
}

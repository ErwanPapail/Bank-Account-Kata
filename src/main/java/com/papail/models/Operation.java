package com.papail.models;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public class Operation {

    public enum OperationType { DEPOSIT, WITHDRAW };
    private final OperationType operationType;
    private final Instant date;
    private final BigDecimal amount;
    private final BigDecimal balance;

    private Operation(final OperationType operationType, final Instant date, final BigDecimal amount, final BigDecimal balance) {
        this.operationType = operationType;
        this.date = date;
        this.amount = amount;
        this.balance = balance;
    }

    public static Operation createDeposit(final Instant date, final BigDecimal amount, final BigDecimal balanceAfterOperation) {
        return new Operation(OperationType.DEPOSIT, date, amount, balanceAfterOperation);
    }

    public static Operation createWithdraw(final Instant date, final BigDecimal amount, final BigDecimal balanceAfterOperation) {
        return new Operation(OperationType.WITHDRAW, date, amount, balanceAfterOperation);
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public Instant getDate() {
        return date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Operation)) return false;
        final Operation operation = (Operation) o;
        return operationType == operation.operationType &&
                Objects.equals(date, operation.date) &&
                Objects.equals(amount, operation.amount) &&
                Objects.equals(balance, operation.balance);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(operationType, date, amount, balance);
    }
}

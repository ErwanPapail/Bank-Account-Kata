package com.papail.models;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OperationTest {

    private final Clock date = Clock.fixed(Instant.now(), ZoneOffset.UTC);

    @Test
    void shouldCreateOperationOfTypeDeposit() {
        final var amount = BigDecimal.ONE;
        final var balance = BigDecimal.ONE;
        final var actual = Operation.createDeposit(date.instant(), amount, balance);

        assertEquals(Operation.OperationType.DEPOSIT, actual.getOperationType());
        assertEquals(date.instant(), actual.getDate());
        assertEquals(amount, actual.getAmount());
        assertEquals(balance, actual.getAmount());
    }

    @Test
    void shouldCreateOperationOfTypeWithdraw() {
        final var amount = BigDecimal.ONE;
        final var balance = BigDecimal.ONE;
        final var actual = Operation.createWithdraw(date.instant(), amount, balance);

        assertEquals(Operation.OperationType.WITHDRAW, actual.getOperationType());
        assertEquals(date.instant(), actual.getDate());
        assertEquals(amount, actual.getAmount());
        assertEquals(balance, actual.getAmount());
    }
    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Operation.class)
                .verify();
    }

}
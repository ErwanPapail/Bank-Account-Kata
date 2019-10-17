package com.papail.formatter;

import com.papail.models.Operation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TabularOperationFormatterTest {

    private IOperationFormatter tabularOperationFormatter;
    private final Clock date = Clock.fixed(Instant.now(), ZoneOffset.UTC);

    @BeforeEach
    void setUp() {
        tabularOperationFormatter = new TabularOperationFormatter();
    }

    @Test
    void shouldFormatDepositOperation() {
        final var operation = Operation.createDeposit(date.instant(), BigDecimal.ONE, BigDecimal.ONE);
        final var expectedDate = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDateTime.ofInstant(date.instant(), ZoneOffset.UTC));
        final var expected = "|| " + expectedDate + "   || 1.0          ||              || 1.0          ||";

        assertEquals(expected, tabularOperationFormatter.format(operation));
    }

    @Test
    void shouldFormatWithdrawOperation() {
        final var operation = Operation.createWithdraw(date.instant(), BigDecimal.ONE, BigDecimal.ONE);
        final var expectedDate = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDateTime.ofInstant(date.instant(), ZoneOffset.UTC));
        final var expected = "|| " + expectedDate + "   ||              || 1.0          || 1.0          ||";

        assertEquals(expected, tabularOperationFormatter.format(operation));
    }
}
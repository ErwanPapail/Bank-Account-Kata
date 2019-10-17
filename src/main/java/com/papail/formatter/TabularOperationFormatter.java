package com.papail.formatter;

import com.papail.models.Operation;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class TabularOperationFormatter implements IOperationFormatter {
    public static final String STATEMENT_PRINTER_HEADER = "|| date         || credit       || debit        || balance      ||";

    @Override
    public String format(final Operation operation) {
        switch(operation.getOperationType()) {
            case WITHDRAW:
                return String.format("|| %-12s || %-12s || %-12.1f || %-12.1f ||", formatDate(operation.getDate()), "", operation.getAmount(), operation.getBalance());
            case DEPOSIT:
                return String.format("|| %-12s || %-12.1f || %-12s || %-12.1f ||", formatDate(operation.getDate()), operation.getAmount(), "", operation.getBalance());
            default:
                return "|| Error        || Error        || Error        || Error        ||";
        }
    }

    private String formatDate(final Instant instant) {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDateTime.ofInstant(instant, ZoneOffset.UTC));
    }
}

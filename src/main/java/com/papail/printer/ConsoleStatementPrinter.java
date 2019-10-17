package com.papail.printer;

import com.papail.formatter.IOperationFormatter;
import com.papail.formatter.TabularOperationFormatter;
import com.papail.models.Operation;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ConsoleStatementPrinter implements IStatementPrinter {
    private final IOperationFormatter operationFormatter;

    public ConsoleStatementPrinter(final IOperationFormatter operationFormatter) {
        this.operationFormatter = operationFormatter;
    }

    @Override
    public void printStatement(final List<Operation> operations) {
        System.out.println(TabularOperationFormatter.STATEMENT_PRINTER_HEADER);

        final var formattedOperations = operations
                .stream()
                .sorted(Comparator.comparing(Operation::getDate).reversed())
                .map(operationFormatter::format)
                .collect(Collectors.toList());

        formattedOperations.forEach(System.out::println);
    }
}

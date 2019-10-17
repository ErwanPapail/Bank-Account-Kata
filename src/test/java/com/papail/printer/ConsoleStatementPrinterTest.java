package com.papail.printer;

import com.papail.IOperationRepository;
import com.papail.formatter.IOperationFormatter;
import com.papail.models.Operation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsoleStatementPrinterTest {

    @Mock
    private IOperationRepository operationRepository;

    @Mock
    private IOperationFormatter tabularOperationFormatter;

    private final Clock date = Clock.fixed(Instant.now(), ZoneOffset.UTC);

    @InjectMocks
    private ConsoleStatementPrinter statementPrinter;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreOutputStream() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Should print operations in PrintStream")
    void shouldPrintStatement() {
        final var head = "|| date         || credit       || debit        || balance      ||";
        final var operation1StringValue = "operation1";
        final var operation2StringValue = "operation2";
        final var expected = head + System.lineSeparator() + operation1StringValue + System.lineSeparator() + operation2StringValue + System.lineSeparator();

        final List<Operation> operations = new LinkedList<>();
        final var operation1 = Operation.createDeposit(date.instant(), BigDecimal.ONE, BigDecimal.ONE);
        final var operation2 = Operation.createDeposit(date.instant(), BigDecimal.ONE, BigDecimal.ONE);
        operations.add(operation1);
        operations.add(operation2);

        when(tabularOperationFormatter.format(eq(operation1))).thenReturn(operation1StringValue);
        when(tabularOperationFormatter.format(eq(operation2))).thenReturn(operation2StringValue);

        statementPrinter.printStatement(operations);

        final var actual = new String(outContent.toByteArray());
        assertEquals(expected, actual);

        verifyNoMoreInteractions(operationRepository);
    }
}

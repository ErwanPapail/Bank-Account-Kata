package com.papail;

import com.papail.exceptions.AccountNotFoundException;
import com.papail.exceptions.InsufficientFundsException;
import com.papail.exceptions.InvalidAmountException;
import com.papail.formatter.IOperationFormatter;
import com.papail.formatter.TabularOperationFormatter;
import com.papail.printer.ConsoleStatementPrinter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class BankAccountServiceAcceptanceTest {

    private ByteArrayOutputStream consoleOutputStream;
    private IBankAccountService bankAccountService;
    private ConsoleStatementPrinter printer;

    @BeforeEach
    void setUp() {
        final IOperationRepository IOperationRepository = new OperationRepositoryFakeImpl();
        bankAccountService = new BankAccountService(IOperationRepository);
        final IOperationFormatter tabularOperationFormatter = new TabularOperationFormatter();
        printer = new ConsoleStatementPrinter(tabularOperationFormatter);
    }

    @BeforeEach
    void redirectOutputStream() {
        consoleOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutputStream));
    }

    @AfterEach
    void restoreOutputStream() {
        System.setOut(System.out);
    }

    @Test
    @DisplayName("Should print all operations")
    void kataUserStory() throws InvalidAmountException, AccountNotFoundException, InsufficientFundsException {
        final var expected =
                        "|| date         || credit       || debit        || balance      ||" + System.lineSeparator() +
                        "|| 03/09/2019   ||              || 300000.0     || 225000.0     ||" + System.lineSeparator() +
                        "|| 02/09/2019   || 25000.0      ||              || 525000.0     ||" + System.lineSeparator() +
                        "|| 01/09/2019   || 500000.0     ||              || 500000.0     ||" + System.lineSeparator();

        final var accountId = "0";

        final var firstDepositAmount = BigDecimal.valueOf(500000);
        final var firstDepositDate = LocalDateTime.of(2019, 9, 1, 0, 0,0);

        final var secondDepositAmount = BigDecimal.valueOf(25000);
        final var secondDepositDate = LocalDateTime.of(2019, 9, 2, 0, 0,0);

        final var firstWithdrawAmount = BigDecimal.valueOf(300000);
        final var firstWithdrawDate = LocalDateTime.of(2019, 9, 3, 0, 0,0);

        bankAccountService.makeDeposit(accountId, firstDepositAmount, firstDepositDate.toInstant(ZoneOffset.UTC));
        bankAccountService.makeDeposit(accountId, secondDepositAmount, secondDepositDate.toInstant(ZoneOffset.UTC));
        bankAccountService.makeWithdraw(accountId, firstWithdrawAmount, firstWithdrawDate.toInstant(ZoneOffset.UTC));

        bankAccountService.printStatement(printer, accountId);

        final var actual = new String(consoleOutputStream.toByteArray());

        assertEquals(expected, actual);
    }
}

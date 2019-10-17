package com.papail;

import com.papail.exceptions.AccountNotFoundException;
import com.papail.exceptions.InsufficientFundsException;
import com.papail.exceptions.InvalidAmountException;
import com.papail.models.Operation;
import com.papail.printer.IStatementPrinter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankAccountServiceTest {

    @Mock
    private IOperationRepository operationRepository;

    @Mock
    private IStatementPrinter iStatementPrinter;

    @InjectMocks
    private BankAccountService bankAccountService;

    private final Clock date = Clock.fixed(Instant.now(), ZoneOffset.UTC);

    @ParameterizedTest
    @DisplayName("Given correct value, should make deposit")
    @CsvSource({"0, 1", "1000, 15", "5000000, 1500"})
    void shouldDepositPositiveIntegerMoneyAmount(final int initialBalanceInt, final int moneyAmountInt) throws InvalidAmountException, AccountNotFoundException {
        final var initialBalance = BigDecimal.valueOf(initialBalanceInt);
        final var moneyAmountToDeposit = BigDecimal.valueOf(moneyAmountInt);
        final var balanceAfterOperation = initialBalance.add(moneyAmountToDeposit);
        final var id = "0";

        when(operationRepository.getBalance(eq(id))).thenReturn(initialBalance);
        when(operationRepository.doesAccountExist(eq(id))).thenReturn(true);

        bankAccountService.makeDeposit(id, moneyAmountToDeposit, date.instant());

        final ArgumentCaptor<Operation> argument = ArgumentCaptor.forClass(Operation.class);

        verify(operationRepository).getBalance(eq(id));
        verify(operationRepository).add(eq(id), argument.capture());

        assertEquals(date.instant(), argument.getValue().getDate());
        assertEquals(moneyAmountToDeposit, argument.getValue().getAmount());
        assertEquals(balanceAfterOperation, argument.getValue().getBalance());

        verifyNoMoreInteractions(operationRepository);
    }

    @Test
    @DisplayName("Given correct amount to withdraw, should make withdraw")
    void givenCorrectAmountShouldMakeWithdraw() throws InvalidAmountException, AccountNotFoundException, InsufficientFundsException {
        final var initialBalance = BigDecimal.valueOf(1000);
        final var moneyAmountToWithdraw = BigDecimal.valueOf(500);
        final var balanceAfterOperation = initialBalance.subtract(moneyAmountToWithdraw);
        final var id = "0";

        when(operationRepository.getBalance(eq(id))).thenReturn(initialBalance);
        when(operationRepository.doesAccountExist(eq(id))).thenReturn(true);

        bankAccountService.makeWithdraw(id, moneyAmountToWithdraw, date.instant());
        final ArgumentCaptor<Operation> argument = ArgumentCaptor.forClass(Operation.class);

        verify(operationRepository).getBalance(eq(id));
        verify(operationRepository).add(eq(id), argument.capture());

        assertEquals(date.instant(), argument.getValue().getDate());
        assertEquals(moneyAmountToWithdraw, argument.getValue().getAmount());
        assertEquals(balanceAfterOperation, argument.getValue().getBalance());        verifyNoMoreInteractions(operationRepository);
    }

    @ParameterizedTest
    @DisplayName("Given negative input to deposit, should raise InvalidAmountException")
    @ValueSource(ints = {-1, -10, -2000})
    void givenNegativeAmountToDepositShouldThrowException(final int moneyAmountInt) {
        final var moneyAmount = BigDecimal.valueOf(moneyAmountInt);
        final var id = "0";

        when(operationRepository.doesAccountExist(eq(id))).thenReturn(true);

        assertThrows(InvalidAmountException.class, () -> bankAccountService.makeDeposit(id, moneyAmount, date.instant()));
        verifyNoMoreInteractions(operationRepository);
    }

    @Test
    @DisplayName("Given negative input to withdraw, should raise InvalidAmountException")
    void givenNegativeAmountToWithdrawShouldThrowException() {
        final var moneyAmount = BigDecimal.valueOf(-100);
        final var id = "0";
        when(operationRepository.doesAccountExist(eq(id))).thenReturn(true);

        assertThrows(InvalidAmountException.class, () -> bankAccountService.makeWithdraw(id, moneyAmount, date.instant()));
        verifyNoMoreInteractions(operationRepository);
    }

    @Test
    @DisplayName("Given unknown accountId, should throw AccountNotFoundException when making deposit")
    void givenUnknownAccountIdShouldThrowExceptionWhileMakingDeposit() {
        final var moneyAmount = BigDecimal.valueOf(-100);
        final var id = "0";

        when(operationRepository.doesAccountExist(eq(id))).thenReturn(false);

        assertThrows(AccountNotFoundException.class, () -> bankAccountService.makeDeposit(id, moneyAmount, date.instant()));
        verifyNoMoreInteractions(operationRepository);
    }

    @Test
    @DisplayName("Given unknown accountId, should throw AccountNotFoundException when making withdraw")
    void givenUnknownAccountIdShouldThrowExceptionWhileMakingWithdraw() {
        final var moneyAmount = BigDecimal.valueOf(-100);
        final var id = "0";

        when(operationRepository.doesAccountExist(eq(id))).thenReturn(false);

        assertThrows(AccountNotFoundException.class, () -> bankAccountService.makeWithdraw(id, moneyAmount, date.instant()));
        verifyNoMoreInteractions(operationRepository);
    }

    @Test
    @DisplayName("Given amount to withdraw superior to balance, should throw InsufficientFundsException")
    void givenAmountToWithdrawSuperiorToBalanceShouldFundsException() {
        final var initialBalance = BigDecimal.valueOf(500);
        final var moneyAmountToWithdraw = BigDecimal.valueOf(1000);
        final var id = "0";

        when(operationRepository.getBalance(eq(id))).thenReturn(initialBalance);
        when(operationRepository.doesAccountExist(eq(id))).thenReturn(true);

        assertThrows(InsufficientFundsException.class, () -> bankAccountService.makeWithdraw(id, moneyAmountToWithdraw, date.instant()));
        verifyNoMoreInteractions(operationRepository);
    }

    @Test
    @DisplayName("Should call printer method to print statement")
    void shouldCallPrinterToPrintStatement() {
        final var id = "0";

        bankAccountService.printStatement(iStatementPrinter, id);
        final List<Operation> operations = new LinkedList<>();

        verify(iStatementPrinter).printStatement(eq(operations));
        verifyNoMoreInteractions(iStatementPrinter);
    }
}
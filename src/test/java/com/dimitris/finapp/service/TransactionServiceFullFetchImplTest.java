package com.dimitris.finapp.service;

import com.dimitris.finapp.dto.transaction.AccountBasicsDto;
import com.dimitris.finapp.dto.transaction.TransactionRequestDto;
import com.dimitris.finapp.dto.transaction.TransactionResponseDto;
import com.dimitris.finapp.enums.CurrencyEnum;
import com.dimitris.finapp.exception.FinException;
import com.dimitris.finapp.persistence.entities.Account;
import com.dimitris.finapp.persistence.entities.CurrencyType;
import com.dimitris.finapp.persistence.entities.Transaction;
import com.dimitris.finapp.persistence.repositories.AccountRepository;
import com.dimitris.finapp.persistence.repositories.TransactionRepository;
import com.dimitris.finapp.service.impl.TransactionServiceBasicFetchImpl;
import com.dimitris.finapp.service.impl.TransactionServiceFullFetchImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceFullFetchImplTest {

    @Mock
    private TransactionRepository transactionRepositoryMock;

    @Mock
    private AccountRepository accountRepositoryMock;

    @InjectMocks
    private TransactionServiceFullFetchImpl transactionService;

    @Test
    public void testExecuteTransaction_happy_path() throws FinException {
        Long sourceAccountId = 1L;
        Long targetAccountId = 3L;
        BigDecimal amount = new BigDecimal("50.00");
        BigDecimal sourceAccountBalance = new BigDecimal("100.00");
        BigDecimal targetAccountBalance = new BigDecimal("500.00");
        CurrencyType currencyType = new CurrencyType(CurrencyEnum.EUR);
        Account sourceAccount = new Account();
        sourceAccount.setBalance(sourceAccountBalance);
        Account targetAccount = new Account();
        targetAccount.setBalance(targetAccountBalance);

        BigDecimal newSourceAccountBalance = new BigDecimal("50.00");
        BigDecimal newTargetAccountBalance = new BigDecimal("550.00");

        TransactionRequestDto transactionRequestDto = new TransactionRequestDto(sourceAccountId, targetAccountId, amount);
        Transaction savedTransaction = new Transaction(sourceAccount, targetAccount, amount, currencyType);
        savedTransaction.setId(10L);
        TransactionResponseDto transactionResponseDto = new TransactionResponseDto(10L);

        when(accountRepositoryMock.findById(sourceAccountId)).thenReturn(Optional.of(sourceAccount));
        when(accountRepositoryMock.findById(targetAccountId)).thenReturn(Optional.of(targetAccount));
        when(transactionRepositoryMock.save(any(Transaction.class))).thenReturn(savedTransaction);

        TransactionResponseDto actualResponseDto = transactionService.executeTransaction(transactionRequestDto);

        verify(accountRepositoryMock).findById(sourceAccountId);
        verify(accountRepositoryMock).findById(targetAccountId);

        Account newSourceAccount = sourceAccount;
        newSourceAccount.setBalance(newSourceAccountBalance);
        Account newTargetAccount = targetAccount;
        newTargetAccount.setBalance(newTargetAccountBalance);
        verify(accountRepositoryMock).save(eq(newSourceAccount));
        verify(accountRepositoryMock).save(eq(newTargetAccount));

        verify(transactionRepositoryMock).save(any(Transaction.class));
        verifyNoMoreInteractions(accountRepositoryMock);
        verifyNoMoreInteractions(transactionRepositoryMock);

        assertEquals(transactionResponseDto, actualResponseDto);
    }

    @Test
    public void testExecuteTransaction_same_source_and_target_should_throw_exception() throws FinException {
        TransactionRequestDto transactionRequestDto = new TransactionRequestDto(1L, 1L, new BigDecimal("50.00"));
        Throwable throwable = assertThrows(FinException.class, () -> transactionService.executeTransaction(transactionRequestDto));
        assertEquals(throwable, new FinException(HttpStatus.CONFLICT, "Transaction cancelled: source and target accounts cannot be the same"));
    }

    @Test
    public void testExecuteTransaction_source_account_does_not_exist_should_throw_exception() throws FinException {
        when(accountRepositoryMock.findById(1L)).thenReturn(Optional.empty());
        TransactionRequestDto transactionRequestDto = new TransactionRequestDto(1L, 2L, new BigDecimal("50.00"));

        Throwable throwable = assertThrows(FinException.class, () -> transactionService.executeTransaction(transactionRequestDto));
        assertEquals(throwable, new FinException(HttpStatus.NOT_FOUND, "Transaction cancelled: account with id " + 1L + " does not exist"));

        verify(accountRepositoryMock).findById(1L);
        verifyNoMoreInteractions(accountRepositoryMock);
        verifyNoInteractions(transactionRepositoryMock);
    }

    @Test
    public void testExecuteTransaction_target_account_does_not_exist_should_throw_exception() throws FinException {
        when(accountRepositoryMock.findById(1L)).thenReturn(Optional.of(new Account()));
        when(accountRepositoryMock.findById(2L)).thenReturn(Optional.empty());
        TransactionRequestDto transactionRequestDto = new TransactionRequestDto(1L, 2L, new BigDecimal("50.00"));

        Throwable throwable = assertThrows(FinException.class, () -> transactionService.executeTransaction(transactionRequestDto));
        assertEquals(throwable, new FinException(HttpStatus.NOT_FOUND, "Transaction cancelled: account with id " + 2L + " does not exist"));

        verify(accountRepositoryMock).findById(1L);
        verify(accountRepositoryMock).findById(2L);
        verifyNoMoreInteractions(accountRepositoryMock);
        verifyNoInteractions(transactionRepositoryMock);
    }

    @Test
    public void testExecuteTransaction_source_account_low_balance_should_throw_exception() throws FinException {
        Account sourceAccount = new Account();
        sourceAccount.setBalance(new BigDecimal("100.00"));
        sourceAccount.setCurrencyType(new CurrencyType(CurrencyEnum.EUR));
        Account targetAccount = new Account();
        targetAccount.setBalance(new BigDecimal("400.00"));
        targetAccount.setCurrencyType(new CurrencyType(CurrencyEnum.EUR));

        when(accountRepositoryMock.findById(1L)).thenReturn(Optional.of(sourceAccount));
        when(accountRepositoryMock.findById(2L)).thenReturn(Optional.of(targetAccount));
        TransactionRequestDto transactionRequestDto = new TransactionRequestDto(1L, 2L, new BigDecimal("150.00"));

        Throwable throwable = assertThrows(FinException.class, () -> transactionService.executeTransaction(transactionRequestDto));
        assertEquals(throwable, new FinException(HttpStatus.BAD_REQUEST, "Transaction cancelled: low account balance"));

        verify(accountRepositoryMock).findById(1L);
        verify(accountRepositoryMock).findById(2L);
        verifyNoMoreInteractions(accountRepositoryMock);
        verifyNoInteractions(transactionRepositoryMock);
    }
}

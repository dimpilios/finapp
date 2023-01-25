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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceBasicFetchImplTest {

    @Mock
    private TransactionRepository transactionRepositoryMock;

    @Mock
    private AccountRepository accountRepositoryMock;

    @InjectMocks
    private TransactionServiceBasicFetchImpl transactionService;

    @Test
    public void testExecuteTransaction_happy_path() throws FinException {
        Long sourceAccountId = 1L;
        Long targetAccountId = 3L;
        BigDecimal amount = new BigDecimal("50.00");
        BigDecimal sourceAccountBalance = new BigDecimal("100.00");
        BigDecimal targetAccountBalance = new BigDecimal("500.00");
        CurrencyType currencyType = new CurrencyType(CurrencyEnum.EUR);
        AccountBasicsDto sourceAccountBasicsDto = new AccountBasicsDto(sourceAccountBalance, currencyType);
        AccountBasicsDto targetAccountBasicsDto = new AccountBasicsDto(targetAccountBalance, currencyType);

        BigDecimal newSourceAccountBalance = new BigDecimal("50.00");
        BigDecimal newTargetAccountBalance = new BigDecimal("550.00");

        TransactionRequestDto transactionRequestDto = new TransactionRequestDto(sourceAccountId, targetAccountId, amount);
        Transaction savedTransaction = new Transaction(null, null, amount, currencyType);
        savedTransaction.setId(10L);
        TransactionResponseDto transactionResponseDto = new TransactionResponseDto(10L);

        when(accountRepositoryMock.getAccountBasics(sourceAccountId)).thenReturn(Optional.of(sourceAccountBasicsDto));
        when(accountRepositoryMock.getAccountBasics(targetAccountId)).thenReturn(Optional.of(targetAccountBasicsDto));
        when(transactionRepositoryMock.save(any(Transaction.class))).thenReturn(savedTransaction);

        TransactionResponseDto actualResponseDto = transactionService.executeTransaction(transactionRequestDto);

        verify(accountRepositoryMock).getAccountBasics(sourceAccountId);
        verify(accountRepositoryMock).getAccountBasics(targetAccountId);
        verify(accountRepositoryMock).updateAccountBalance(sourceAccountId, newSourceAccountBalance);
        verify(accountRepositoryMock).updateAccountBalance(targetAccountId, newTargetAccountBalance);
        verify(accountRepositoryMock).getReferenceById(sourceAccountId);
        verify(accountRepositoryMock).getReferenceById(targetAccountId);
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
        when(accountRepositoryMock.getAccountBasics(1L)).thenReturn(Optional.empty());
        TransactionRequestDto transactionRequestDto = new TransactionRequestDto(1L, 2L, new BigDecimal("50.00"));

        Throwable throwable = assertThrows(FinException.class, () -> transactionService.executeTransaction(transactionRequestDto));
        assertEquals(throwable, new FinException(HttpStatus.NOT_FOUND, "Transaction cancelled: account with id " + 1L + " does not exist"));

        verify(accountRepositoryMock).getAccountBasics(1L);
        verifyNoMoreInteractions(accountRepositoryMock);
        verifyNoInteractions(transactionRepositoryMock);
    }

    @Test
    public void testExecuteTransaction_target_account_does_not_exist_should_throw_exception() throws FinException {
        when(accountRepositoryMock.getAccountBasics(1L)).thenReturn(
                Optional.of(new AccountBasicsDto(new BigDecimal("100.00"), new CurrencyType(CurrencyEnum.EUR))));
        when(accountRepositoryMock.getAccountBasics(2L)).thenReturn(Optional.empty());
        TransactionRequestDto transactionRequestDto = new TransactionRequestDto(1L, 2L, new BigDecimal("50.00"));

        Throwable throwable = assertThrows(FinException.class, () -> transactionService.executeTransaction(transactionRequestDto));
        assertEquals(throwable, new FinException(HttpStatus.NOT_FOUND, "Transaction cancelled: account with id " + 2L + " does not exist"));

        verify(accountRepositoryMock).getAccountBasics(1L);
        verify(accountRepositoryMock).getAccountBasics(2L);
        verifyNoMoreInteractions(accountRepositoryMock);
        verifyNoInteractions(transactionRepositoryMock);
    }

    @Test
    public void testExecuteTransaction_source_account_low_balance_should_throw_exception() throws FinException {
        when(accountRepositoryMock.getAccountBasics(1L)).thenReturn(
                Optional.of(new AccountBasicsDto(new BigDecimal("100.00"), new CurrencyType(CurrencyEnum.EUR))));
        when(accountRepositoryMock.getAccountBasics(2L)).thenReturn(
                Optional.of(new AccountBasicsDto(new BigDecimal("400.00"), new CurrencyType(CurrencyEnum.EUR))));
        TransactionRequestDto transactionRequestDto = new TransactionRequestDto(1L, 2L, new BigDecimal("150.00"));

        Throwable throwable = assertThrows(FinException.class, () -> transactionService.executeTransaction(transactionRequestDto));
        assertEquals(throwable, new FinException(HttpStatus.BAD_REQUEST, "Transaction cancelled: low account balance"));

        verify(accountRepositoryMock).getAccountBasics(1L);
        verify(accountRepositoryMock).getAccountBasics(2L);
        verifyNoMoreInteractions(accountRepositoryMock);
        verifyNoInteractions(transactionRepositoryMock);
    }

}

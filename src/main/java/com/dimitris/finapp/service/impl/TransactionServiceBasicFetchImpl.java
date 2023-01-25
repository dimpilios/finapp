package com.dimitris.finapp.service.impl;

import com.dimitris.finapp.dto.transaction.AccountBasicsDto;
import com.dimitris.finapp.dto.transaction.TransactionRequestDto;
import com.dimitris.finapp.dto.transaction.TransactionResponseDto;
import com.dimitris.finapp.exception.FinException;
import com.dimitris.finapp.persistence.entities.Account;
import com.dimitris.finapp.persistence.entities.CurrencyType;
import com.dimitris.finapp.persistence.entities.Transaction;
import com.dimitris.finapp.persistence.repositories.AccountRepository;
import com.dimitris.finapp.persistence.repositories.TransactionRepository;
import com.dimitris.finapp.service.TransactionService;
import com.dimitris.finapp.service.util.MoneyCalcUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

/**
 * Service class implementing the transaction functionalities
 * It uses ORM in a limited way by fetching only the needed data
 */
@Service
@Qualifier("basicFetch")
@Transactional(rollbackOn = Exception.class)
public class TransactionServiceBasicFetchImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    /**
     * Executes money transfer from one account to another
     * @param transactionRequestDTO The dto holding the two accounts and the amount to be transfered
     * @return
     * @throws FinException If acceptance criteria are not met
     */
    @Override
    public TransactionResponseDto executeTransaction(TransactionRequestDto transactionRequestDTO) throws FinException {

        Long sourceAccountId = transactionRequestDTO.getSourceAccountId();
        Long targetAccountId = transactionRequestDTO.getTargetAccountId();
        BigDecimal amount = transactionRequestDTO.getAmount();

        if (sourceAccountId.equals(targetAccountId)) {
            throw new FinException(HttpStatus.CONFLICT, "Transaction cancelled: source and target accounts cannot be the same");
        }

        AccountBasicsDto sourceAccountBasicsDto = accountRepository.getAccountBasics(sourceAccountId).orElseThrow(
                () -> new FinException(HttpStatus.NOT_FOUND, "Transaction cancelled: account with id " + sourceAccountId + " does not exist"));
        AccountBasicsDto targetAccountBasicsDto = accountRepository.getAccountBasics(targetAccountId).orElseThrow(
                () -> new FinException(HttpStatus.NOT_FOUND, "Transaction cancelled: account with id " + targetAccountId + " does not exist"));

        BigDecimal currentSourceBalance = sourceAccountBasicsDto.getBalance();
        if (MoneyCalcUtil.compare(currentSourceBalance, amount) < 0) {
            throw new FinException(HttpStatus.BAD_REQUEST, "Transaction cancelled: low account balance");
        }

        BigDecimal newSourceBalance = MoneyCalcUtil.subtract(currentSourceBalance, amount);
        accountRepository.updateAccountBalance(sourceAccountId, newSourceBalance);

        BigDecimal currentTargetBalance = targetAccountBasicsDto.getBalance();
        BigDecimal newTargetBalance = MoneyCalcUtil.add(currentTargetBalance, amount);
        accountRepository.updateAccountBalance(targetAccountId, newTargetBalance);

        CurrencyType currencyType = targetAccountBasicsDto.getCurrencyType();

        Account sourceAccount = accountRepository.getReferenceById(sourceAccountId);
        Account targetAccount = accountRepository.getReferenceById(targetAccountId);
        Transaction transaction = new Transaction(sourceAccount, targetAccount, amount, currencyType);
        Transaction savedTransaction = transactionRepository.save(transaction);

        return new TransactionResponseDto(savedTransaction.getId());
    }

}

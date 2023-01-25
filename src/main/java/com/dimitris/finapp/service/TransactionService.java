package com.dimitris.finapp.service;

import com.dimitris.finapp.dto.transaction.TransactionRequestDto;
import com.dimitris.finapp.dto.transaction.TransactionResponseDto;
import com.dimitris.finapp.exception.FinException;

/**
 * Service interface abstracting the transaction functionalities
 */
public interface TransactionService {

    /**
     * Executes a transaction, i.e. a transfer of money amount from one account to another
     * @param transactionRequestDTO The dto holding the two accounts and the amount to be transfered
     * @return The dto that will be returned to the user
     * @throws FinException If acceptance criteria are not met
     */
    TransactionResponseDto executeTransaction(TransactionRequestDto transactionRequestDTO) throws FinException;
}

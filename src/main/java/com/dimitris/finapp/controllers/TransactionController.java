package com.dimitris.finapp.controllers;

import com.dimitris.finapp.dto.transaction.TransactionRequestDto;
import com.dimitris.finapp.dto.transaction.TransactionResponseDto;
import com.dimitris.finapp.exception.FinException;
import com.dimitris.finapp.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class provides the basic REST endpoints through which user interacts with the application
 */
@RestController
@RequestMapping(path = "/api/transaction")
public class TransactionController {

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    @Qualifier("basicFetch")
    private TransactionService transactionService;

    /**
     * Executes transfer of a money amount from one account to another
     * @param transactionRequestDTO The input dto containing account ids and the amount to be transfered
     * @return A {@link TransactionResponseDto} which contains the id of the successful transaction (transfer)
     * @throws FinException If transaction acceptance criteria are not met
     */
    @PostMapping
    public TransactionResponseDto executeTransaction(
            @RequestBody TransactionRequestDto transactionRequestDTO) throws FinException {
        logger.debug("INSIDE executeTransaction(). Params: {}", transactionRequestDTO);
        TransactionResponseDto transactionResponseDto = transactionService.executeTransaction(transactionRequestDTO);
        logger.debug("EXITING executeTransaction()");
        return transactionResponseDto;
    }
}

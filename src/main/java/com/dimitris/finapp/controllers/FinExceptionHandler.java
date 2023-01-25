package com.dimitris.finapp.controllers;

import com.dimitris.finapp.dto.FinErrorResponseDto;
import com.dimitris.finapp.exception.FinException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * This class handles at Controller level any checked exception thrown from lower level services
 */
@ControllerAdvice()
public class FinExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    /**
     * Handles {@link FinException} exception thrown from lower level services. Logs exception stack trace
     * and wraps exception into special exception dto {@link FinErrorResponseDto} which returns to user
     * @param finException
     * @return
     */
    @ExceptionHandler(value = {FinException.class})
    protected ResponseEntity<FinErrorResponseDto> handleException(FinException finException) {
        logger.error("{}", finException);
        FinErrorResponseDto finErrorDto = new FinErrorResponseDto(finException.getMessage());
        return new ResponseEntity<>(finErrorDto, finException.getHttpStatus());
    }
}

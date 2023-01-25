package com.dimitris.finapp.endpoint;

import com.dimitris.finapp.controllers.TransactionController;
import com.dimitris.finapp.dto.transaction.TransactionRequestDto;
import com.dimitris.finapp.dto.transaction.TransactionResponseDto;
import com.dimitris.finapp.exception.FinException;
import com.dimitris.finapp.persistence.InitialDataCMDRunner;
import com.dimitris.finapp.service.TransactionService;
import com.dimitris.finapp.service.impl.TransactionServiceFullFetchImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionController transactionController;

    @Test
    void testExecuteTransaction_happy_path() throws Exception {
        TransactionRequestDto requestDto = new TransactionRequestDto(2L, 5L, new BigDecimal("50.00"));
        String requestAsJsonString = new ObjectMapper().writeValueAsString(requestDto);
        TransactionResponseDto responseDto = new TransactionResponseDto(10L);
        when(transactionController.executeTransaction(requestDto)).thenReturn(responseDto);

        mockMvc
                .perform(
                    post("/api/transaction")
                    .content(requestAsJsonString)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(
                    status().isOk()
                )
                .andExpect(
                    content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                )
                .andExpect(
                    jsonPath("$.transactionId").value(10L)
                );
    }

    @Test
    void testExecuteTransaction_exception_should_be_properly_handled() throws Exception {
        TransactionRequestDto requestDto = new TransactionRequestDto(2L, 5L, new BigDecimal("50.00"));
        String requestAsJsonString = new ObjectMapper().writeValueAsString(requestDto);
        when(transactionController.executeTransaction(requestDto))
                .thenThrow(new FinException(HttpStatus.BAD_REQUEST, "Transaction cancelled: low account balance"));

        mockMvc
                .perform(
                        post("/api/transaction")
                        .content(requestAsJsonString)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(
                        status().isBadRequest()
                )
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                )
                .andExpect(
                        jsonPath("$.errorMessage").value("Transaction cancelled: low account balance")
                );
    }
}

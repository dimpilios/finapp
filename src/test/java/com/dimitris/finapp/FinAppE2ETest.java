package com.dimitris.finapp;

import com.dimitris.finapp.dto.transaction.TransactionRequestDto;
import com.dimitris.finapp.dto.transaction.TransactionResponseDto;
import com.dimitris.finapp.persistence.InitialDataCMDRunner;
import com.dimitris.finapp.persistence.entities.Account;
import com.dimitris.finapp.persistence.entities.Transaction;
import com.dimitris.finapp.persistence.repositories.AccountRepository;
import com.dimitris.finapp.persistence.repositories.TransactionRepository;
import com.dimitris.finapp.service.util.MoneyCalcUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@TestPropertySource({"classpath:application-test-data-jpa.properties"})
@ComponentScan(basePackageClasses = InitialDataCMDRunner.class)
@AutoConfigureMockMvc
public class FinAppE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    void testExecuteTransaction_happy_path() throws Exception {
        TransactionRequestDto requestDto = new TransactionRequestDto(2L, 5L, new BigDecimal("50.00"));
        String requestAsJsonString = new ObjectMapper().writeValueAsString(requestDto);

        Account sourceAccount = accountRepository.findById(2L).get();
        Account targetAccount = accountRepository.findById(5L).get();

        MvcResult mvcResult =
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
                        jsonPath("$.transactionId").exists()
                )
                .andReturn();

        TransactionResponseDto responseDto = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), TransactionResponseDto.class);

        Account sourceAccountUpdated = accountRepository.findById(2L).get();
        Account targetAccountUpdated = accountRepository.findById(5L).get();
        assertEquals(sourceAccountUpdated.getBalance(), MoneyCalcUtil.subtract(sourceAccount.getBalance(), new BigDecimal("50.00")));
        assertEquals(targetAccountUpdated.getBalance(), MoneyCalcUtil.add(targetAccount.getBalance(), new BigDecimal("50.00")));

        Transaction transaction = transactionRepository.findById(responseDto.getTransactionId()).get();
        assertEquals(transaction.getSourceAccount().getId(), sourceAccountUpdated.getId());
        assertEquals(transaction.getTargetAccount().getId(), targetAccountUpdated.getId());
        assertEquals(transaction.getAmount(), new BigDecimal("50.00"));
        assertEquals(transaction.getCurrencyType(), targetAccount.getCurrencyType());
    }

    @Test
    void testExecuteTransaction_exception_should_be_handled_properly() throws Exception {
        TransactionRequestDto requestDto = new TransactionRequestDto(2L, 5L, new BigDecimal("850.00"));
        String requestAsJsonString = new ObjectMapper().writeValueAsString(requestDto);

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
                        jsonPath("$.transactionId").doesNotExist()
                )
                .andExpect(
                        jsonPath("$.errorMessage").value("Transaction cancelled: low account balance")
                );

    }
}

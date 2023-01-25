package com.dimitris.finapp.persistence;

import com.dimitris.finapp.dto.transaction.AccountBasicsDto;
import com.dimitris.finapp.enums.CurrencyEnum;
import com.dimitris.finapp.persistence.entities.CurrencyType;
import com.dimitris.finapp.persistence.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@TestPropertySource({"classpath:application-test-data-jpa.properties"})
@ComponentScan(basePackageClasses = InitialDataCMDRunner.class)
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void testGetAccountBasics_of_existent_account_should_return_correct_data() {
        AccountBasicsDto expectedAccountBasicsDto = new AccountBasicsDto(new BigDecimal("500.00"), new CurrencyType(CurrencyEnum.USD));
        AccountBasicsDto accountBasicsDto = accountRepository.getAccountBasics(1L).get();
        assertEquals(accountBasicsDto, expectedAccountBasicsDto);
    }

    @Test
    public void testGetAccountBasics_of_non_existent_account_should_give_null() {
        assertTrue(!accountRepository.getAccountBasics(15L).isPresent());
    }
}

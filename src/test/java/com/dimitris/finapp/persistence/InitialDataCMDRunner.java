package com.dimitris.finapp.persistence;

import com.dimitris.finapp.enums.CurrencyEnum;
import com.dimitris.finapp.persistence.entities.Account;
import com.dimitris.finapp.persistence.entities.CurrencyType;
import com.dimitris.finapp.persistence.entities.Transaction;
import com.dimitris.finapp.persistence.repositories.AccountRepository;
import com.dimitris.finapp.persistence.repositories.CurrencyTypeRepository;
import com.dimitris.finapp.persistence.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Component
public class InitialDataCMDRunner implements CommandLineRunner {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CurrencyTypeRepository currencyTypeRepository;

    @Override
    public void run(String... args) throws Exception {

        CurrencyType USD = new CurrencyType(CurrencyEnum.USD);
        CurrencyType CAD = new CurrencyType(CurrencyEnum.CAD);
        CurrencyType HKD = new CurrencyType(CurrencyEnum.HKD);
        CurrencyType AUD = new CurrencyType(CurrencyEnum.AUD);
        CurrencyType GBP = new CurrencyType(CurrencyEnum.GBP);
        CurrencyType EUR = new CurrencyType(CurrencyEnum.EUR);
        CurrencyType JPY = new CurrencyType(CurrencyEnum.JPY);

        currencyTypeRepository.saveAll(List.of(USD, CAD, HKD, AUD, GBP, EUR, JPY));

        Account account1 = new Account(1L, new BigDecimal("500.00"), USD, LocalDate.of(2018, Month.MARCH, 1));
        Account account2 = new Account(2L, new BigDecimal("650.00"), EUR, LocalDate.of(2022, Month.JANUARY, 9));
        Account account3 = new Account(3L, new BigDecimal("1100.00"), EUR, LocalDate.of(2020, Month.APRIL, 15));
        Account account4 = new Account(4L, new BigDecimal("800.00"), GBP, LocalDate.of(2020, Month.NOVEMBER, 24));
        Account account5 = new Account(5L, new BigDecimal("1500.00"), EUR, LocalDate.of(2017, Month.OCTOBER, 29));
        Account account6 = new Account(6L, new BigDecimal("1350.00"), CAD, LocalDate.of(2019, Month.JUNE, 5));

        accountRepository.saveAll(List.of(account1, account2, account3, account4, account5, account6));

        Transaction transaction1 = new Transaction(account1, account6, new BigDecimal("50.00"), CAD);
        Transaction transaction2 = new Transaction(account1, account6, new BigDecimal("30.00"), CAD);
        Transaction transaction3 = new Transaction(account1, account6, new BigDecimal("80.00"), CAD);
        Transaction transaction4 = new Transaction(account6, account1, new BigDecimal("70.00"), USD);
        Transaction transaction5 = new Transaction(account1, account4, new BigDecimal("30.00"), GBP);
        Transaction transaction6 = new Transaction(account1, account2, new BigDecimal("50.00"), EUR);
        Transaction transaction7 = new Transaction(account3, account5, new BigDecimal("150.00"), EUR);
        Transaction transaction8 = new Transaction(account2, account3, new BigDecimal("250.00"), EUR);
        Transaction transaction9 = new Transaction(account5, account3, new BigDecimal("90.00"), EUR);

        transactionRepository.saveAll(List.of(
                transaction1, transaction2, transaction3, transaction4, transaction5,
                transaction6, transaction7, transaction8, transaction9
        ));
    }
}
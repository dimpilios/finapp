package com.dimitris.finapp.persistence.repositories;

import com.dimitris.finapp.dto.transaction.AccountBasicsDto;
import com.dimitris.finapp.persistence.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * DAO class used to execute CRUD operations on account entities
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Retrieves basic account data
     * @param id Account id
     * @return
     */
    @Query(
            "select new com.dimitris.finapp.dto.transaction.AccountBasicsDto(a.balance, a.currencyType) " +
            "from Account a " +
            "where a.id = :id"
    )
    Optional<AccountBasicsDto> getAccountBasics(@Param("id") Long id);

    /**
     * Updates account balance
     * @param id Account id
     * @param balance The new balance value to be set
     * @return Number of db rows affected
     */
    @Modifying
    @Query(
            "update Account a " +
            "set a.balance = :balance " +
            "where a.id = :id"
    )
    int updateAccountBalance(@Param("id") Long id, @Param("balance") BigDecimal balance);
}

package com.dimitris.finapp.persistence.repositories;

import com.dimitris.finapp.persistence.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DAO class used to execute CRUD operations on transaction entities
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}

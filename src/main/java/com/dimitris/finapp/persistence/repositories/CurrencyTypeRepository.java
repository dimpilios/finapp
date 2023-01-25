package com.dimitris.finapp.persistence.repositories;

import com.dimitris.finapp.persistence.entities.CurrencyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DAO class used to execute CRUD operations on currency type entities
 */
@Repository
public interface CurrencyTypeRepository extends JpaRepository<CurrencyType, Integer> {
}

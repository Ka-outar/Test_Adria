
package com.adria.banking_platform.repository;

import com.adria.banking_platform.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository
        extends JpaRepository<Transaction, UUID> {

    List<Transaction> findByAccountId(UUID accountId);
}

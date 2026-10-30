
package com.adria.banking_platform.service;

import com.adria.banking_platform.domain.Account;
import com.adria.banking_platform.domain.AccountStatus;
import com.adria.banking_platform.domain.Transaction;
import com.adria.banking_platform.repository.TransactionRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    @Value("${bank.transaction.max-amount:1000}")
    private BigDecimal maxAmount;

    public TransactionService(
            TransactionRepository transactionRepository,
            AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    @Transactional
    public Transaction createTransaction(Transaction transaction) {

        BigDecimal amount = transaction.getAmount();

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be positive");
        }

        if (amount.compareTo(maxAmount) > 0) {
            throw new RuntimeException("Maximum amount exceeded");
        }

        Account account = accountService.getAccountById(
                transaction.getAccountId());

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException("Account is blocked");
        }

        if (!account.getCurrency().equalsIgnoreCase(
                transaction.getCurrency())) {
            throw new RuntimeException("Currency mismatch");
        }

        if (BigDecimal.valueOf(account.getBalance())
                .compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        Transaction saved = transactionRepository.save(transaction);

        accountService.adjustBalance(
                account.getId(),
                -amount.doubleValue());

        return saved;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction getTransactionById(UUID id) {
        return transactionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Transaction not found"));
    }

    public List<Transaction> getTransactionsByAccount(UUID accountId) {
        return transactionRepository.findByAccountId(accountId);
    }
}

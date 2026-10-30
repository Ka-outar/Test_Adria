
package com.adria.banking_platform.web.graphql;

import com.adria.banking_platform.domain.Transaction;
import com.adria.banking_platform.service.TransactionService;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
public class TransactionGraphQLController {

    private final TransactionService transactionService;

    public TransactionGraphQLController(
            TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @QueryMapping
    public List<Transaction> transactions() {
        return transactionService.getAllTransactions();
    }

    @QueryMapping
    public Transaction transactionById(@Argument String id) {
        return transactionService.getTransactionById(
                UUID.fromString(id));
    }

    @QueryMapping
    public List<Transaction> transactionsByAccount(
            @Argument String accountId) {
        return transactionService.getTransactionsByAccount(
                UUID.fromString(accountId));
    }

    @MutationMapping
    public Transaction createTransaction(
            @Argument String accountId,
            @Argument Double amount,
            @Argument String currency,
            @Argument String merchantCategory,
            @Argument String country) {

        Transaction transaction = new Transaction();
        transaction.setAccountId(UUID.fromString(accountId));
        transaction.setAmount(
                java.math.BigDecimal.valueOf(amount));
        transaction.setCurrency(currency);
        transaction.setMerchantCategory(merchantCategory);
        transaction.setCountry(country);

        return transactionService.createTransaction(transaction);
    }
}

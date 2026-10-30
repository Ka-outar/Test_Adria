package com.adria.banking_platform.web.graphql;

import com.adria.banking_platform.domain.Account;
import com.adria.banking_platform.service.AccountService;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
public class AccountGraphQLController {

    private final AccountService accountService;

    public AccountGraphQLController(AccountService accountService) {
        this.accountService = accountService;
    }

    // Get all accounts
    @QueryMapping
    public List<Account> accounts() {
        return accountService.getAllAccounts();
    }

    // Get account by ID
    @QueryMapping
    public Account accountById(@Argument String id) {
        return accountService.getAccountById(UUID.fromString(id));
    }

    // Create account
    @MutationMapping
    public Account createAccount(
            @Argument String customerReference,
            @Argument String currency,
            @Argument Double balance) {

        Account account = new Account();
        account.setCustomerReference(customerReference);
        account.setCurrency(currency);
        account.setBalance(balance);

        return accountService.createAccount(account);
    }

    // Delete account
    @MutationMapping
    public Boolean deleteAccount(@Argument String id) {
        accountService.deleteAccount(UUID.fromString(id));
        return true;
    }
}
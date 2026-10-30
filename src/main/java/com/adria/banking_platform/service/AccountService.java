package com.adria.banking_platform.service;

import com.adria.banking_platform.domain.Account;
import com.adria.banking_platform.domain.AccountStatus;
import com.adria.banking_platform.repository.AccountRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(Account account) {
        account.setStatus(AccountStatus.ACTIVE);
        return accountRepository.save(account);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountById(UUID id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public Account updateAccount(UUID id, Account updatedAccount) {
        Account account = getAccountById(id);

        account.setCustomerReference(updatedAccount.getCustomerReference());
        account.setCurrency(updatedAccount.getCurrency());
        account.setBalance(updatedAccount.getBalance());
        account.setStatus(updatedAccount.getStatus());

        return accountRepository.save(account);
    }

    public void deleteAccount(UUID id) {
        Account account = getAccountById(id);
        accountRepository.delete(account);
    }
}
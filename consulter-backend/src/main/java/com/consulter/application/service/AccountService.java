package com.consulter.application.service;

import com.consulter.application.exception.MemberAlreadyExistsException;
import com.consulter.application.exception.ResourceNotFoundException;
import com.consulter.domain.model.Account;
import com.consulter.domain.port.in.AccountUseCase;
import com.consulter.domain.port.out.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService implements AccountUseCase {

    private final AccountRepository accountRepository;

    @Override
    public Account create(UUID ownerId, String name, String currency) {
        Account account = new Account(null, ownerId, name, BigDecimal.ZERO, currency != null ? currency : "EUR", null, null);
        return accountRepository.save(account);
    }

    @Override
    public Account findById(UUID id) {
        return accountRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + id));
    }

    @Override
    public List<Account> findAccessibleByUserId(UUID userId) {
        return accountRepository.findAccessibleByUserId(userId);
    }

    @Override
    public void addMember(UUID accountId, UUID userId) {
        if (accountRepository.existsMember(accountId, userId)) {
            throw new MemberAlreadyExistsException("User " + userId + " is already a member of account " + accountId);
        }
        accountRepository.addMember(accountId, userId);
    }

    @Override
    public void removeMember(UUID accountId, UUID userId) {
        accountRepository.removeMember(accountId, userId);
    }
}

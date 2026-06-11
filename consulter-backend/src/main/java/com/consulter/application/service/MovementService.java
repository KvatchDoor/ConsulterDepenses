package com.consulter.application.service;

import com.consulter.application.exception.ResourceNotFoundException;
import com.consulter.domain.model.Account;
import com.consulter.domain.model.Movement;
import com.consulter.domain.model.MovementType;
import com.consulter.domain.port.in.MovementUseCase;
import com.consulter.domain.port.out.AccountRepository;
import com.consulter.domain.port.out.MovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MovementService implements MovementUseCase {

    private final MovementRepository movementRepository;
    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public Movement create(UUID accountId, UUID createdBy, UUID categoryId, MovementType type, BigDecimal amount, String description, LocalDate movementDate) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountId));

        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Amount must be greater than 0");
        if (amount.compareTo(new BigDecimal("10000")) > 0)
            throw new IllegalArgumentException("Amount must not exceed 10000");
        if (amount.stripTrailingZeros().scale() > 2)
            throw new IllegalArgumentException("Amount must have at most 2 decimal places");

        Movement movement = new Movement(null, accountId, createdBy, categoryId, type, amount, description, movementDate, null, null);
        Movement saved = movementRepository.save(movement);

        BigDecimal newBalance = type == MovementType.CREDIT
            ? account.balance().add(amount)
            : account.balance().subtract(amount);

        accountRepository.save(new Account(
            account.id(), account.ownerId(), account.name(),
            newBalance, account.currency(), account.createdAt(), LocalDateTime.now()
        ));

        return saved;
    }

    @Override
    public Movement findById(UUID id) {
        return movementRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Movement not found: " + id));
    }

    @Override
    public List<Movement> findByAccountId(UUID accountId) {
        return movementRepository.findByAccountId(accountId);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Movement movement = movementRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Movement not found: " + id));

        Account account = accountRepository.findById(movement.accountId())
            .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + movement.accountId()));

        BigDecimal newBalance = movement.type() == MovementType.CREDIT
            ? account.balance().subtract(movement.amount())
            : account.balance().add(movement.amount());

        accountRepository.save(new Account(
            account.id(), account.ownerId(), account.name(),
            newBalance, account.currency(), account.createdAt(), LocalDateTime.now()
        ));

        movementRepository.deleteById(id);
    }
}

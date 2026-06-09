package com.consulter.infrastructure.persistence.adapter;

import com.consulter.domain.model.Account;
import com.consulter.domain.port.out.AccountRepository;
import com.consulter.infrastructure.persistence.entity.AccountMemberEntity;
import com.consulter.infrastructure.persistence.entity.AccountMemberId;
import com.consulter.infrastructure.persistence.jpa.AccountJpaRepository;
import com.consulter.infrastructure.persistence.jpa.AccountMemberJpaRepository;
import com.consulter.infrastructure.persistence.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AccountRepositoryAdapter implements AccountRepository {

    private final AccountJpaRepository jpaRepository;
    private final AccountMemberJpaRepository memberJpaRepository;
    private final AccountMapper mapper;

    @Override
    public Account save(Account account) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(account)));
    }

    @Override
    public Optional<Account> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Account> findAccessibleByUserId(UUID userId) {
        return jpaRepository.findAccessibleByUserId(userId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public boolean existsMember(UUID accountId, UUID userId) {
        return memberJpaRepository.existsById(new AccountMemberId(accountId, userId));
    }

    @Override
    public void addMember(UUID accountId, UUID userId) {
        memberJpaRepository.save(AccountMemberEntity.builder()
            .id(new AccountMemberId(accountId, userId))
            .build());
    }

    @Override
    public void removeMember(UUID accountId, UUID userId) {
        memberJpaRepository.deleteByAccountIdAndUserId(accountId, userId);
    }
}

package com.consulter.domain.port.out;

import com.consulter.domain.model.Account;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {
    Account save(Account account);
    Optional<Account> findById(UUID id);
    List<Account> findAccessibleByUserId(UUID userId);
    boolean existsMember(UUID accountId, UUID userId);
    void addMember(UUID accountId, UUID userId);
    void removeMember(UUID accountId, UUID userId);
}

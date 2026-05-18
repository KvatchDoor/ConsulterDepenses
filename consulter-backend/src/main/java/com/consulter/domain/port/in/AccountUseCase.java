package com.consulter.domain.port.in;

import com.consulter.domain.model.Account;
import java.util.List;
import java.util.UUID;

public interface AccountUseCase {
    Account create(UUID ownerId, String name, String currency);
    Account findById(UUID id);
    List<Account> findAccessibleByUserId(UUID userId);
    void addMember(UUID accountId, UUID userId);
    void removeMember(UUID accountId, UUID userId);
}

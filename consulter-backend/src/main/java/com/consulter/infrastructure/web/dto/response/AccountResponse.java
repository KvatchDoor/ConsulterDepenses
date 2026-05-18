package com.consulter.infrastructure.web.dto.response;

import com.consulter.domain.model.Account;
import java.math.BigDecimal;
import java.util.UUID;

public record AccountResponse(UUID id, UUID ownerId, String name, BigDecimal balance, String currency) {

    public static AccountResponse from(Account account) {
        return new AccountResponse(account.id(), account.ownerId(), account.name(), account.balance(), account.currency());
    }
}

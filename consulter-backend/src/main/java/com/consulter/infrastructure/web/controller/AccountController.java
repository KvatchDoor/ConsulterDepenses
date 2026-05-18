package com.consulter.infrastructure.web.controller;

import com.consulter.domain.model.Account;
import com.consulter.domain.port.in.AccountUseCase;
import com.consulter.infrastructure.web.api.AccountsApi;
import com.consulter.infrastructure.web.model.AccountResponse;
import com.consulter.infrastructure.web.model.AddMemberRequest;
import com.consulter.infrastructure.web.model.CreateAccountRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AccountController implements AccountsApi {

    private final AccountUseCase accountUseCase;

    @Override
    public ResponseEntity<AccountResponse> createAccount(CreateAccountRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(toResponse(accountUseCase.create(request.getOwnerId(), request.getName(), request.getCurrency())));
    }

    @Override
    public ResponseEntity<List<AccountResponse>> getAccountsByUser(UUID userId) {
        return ResponseEntity.ok(accountUseCase.findAccessibleByUserId(userId).stream().map(this::toResponse).toList());
    }

    @Override
    public ResponseEntity<AccountResponse> getAccountById(UUID id) {
        return ResponseEntity.ok(toResponse(accountUseCase.findById(id)));
    }

    @Override
    public ResponseEntity<Void> addMember(UUID id, AddMemberRequest request) {
        accountUseCase.addMember(id, request.getUserId());
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> removeMember(UUID id, UUID userId) {
        accountUseCase.removeMember(id, userId);
        return ResponseEntity.noContent().build();
    }

    private AccountResponse toResponse(Account a) {
        return new AccountResponse()
            .id(a.id())
            .ownerId(a.ownerId())
            .name(a.name())
            .balance(a.balance())
            .currency(a.currency());
    }
}

package com.consulter.infrastructure.web.controller;

import com.consulter.domain.port.in.AccountUseCase;
import com.consulter.infrastructure.web.dto.request.AddMemberRequest;
import com.consulter.infrastructure.web.dto.request.CreateAccountRequest;
import com.consulter.infrastructure.web.dto.response.AccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountUseCase accountUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponse create(@RequestBody CreateAccountRequest request) {
        return AccountResponse.from(accountUseCase.create(request.ownerId(), request.name(), request.currency()));
    }

    @GetMapping
    public List<AccountResponse> findByUser(@RequestParam UUID userId) {
        return accountUseCase.findAccessibleByUserId(userId).stream().map(AccountResponse::from).toList();
    }

    @GetMapping("/{id}")
    public AccountResponse findById(@PathVariable UUID id) {
        return AccountResponse.from(accountUseCase.findById(id));
    }

    @PostMapping("/{id}/members")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addMember(@PathVariable UUID id, @RequestBody AddMemberRequest request) {
        accountUseCase.addMember(id, request.userId());
    }

    @DeleteMapping("/{id}/members/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeMember(@PathVariable UUID id, @PathVariable UUID userId) {
        accountUseCase.removeMember(id, userId);
    }
}

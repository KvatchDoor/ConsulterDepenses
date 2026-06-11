package com.consulter.infrastructure.web.controller;

import com.consulter.domain.model.Account;
import com.consulter.domain.port.in.AccountUseCase;
import com.consulter.infrastructure.web.model.AccountResponse;
import com.consulter.infrastructure.web.model.AddMemberRequest;
import com.consulter.infrastructure.web.model.CreateAccountRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private AccountUseCase accountUseCase;

    @InjectMocks
    private AccountController accountController;

    private Account sampleAccount(UUID ownerId) {
        return new Account(
            UUID.randomUUID(), ownerId, "Compte principal",
            new BigDecimal("500.00"), "EUR", null, null
        );
    }

    @Test
    void createAccount_returnsCreatedStatus() {
        UUID ownerId = UUID.randomUUID();
        Account account = sampleAccount(ownerId);

        CreateAccountRequest request = new CreateAccountRequest()
            .ownerId(ownerId)
            .name("Compte principal")
            .currency("EUR");

        when(accountUseCase.create(eq(ownerId), eq("Compte principal"), eq("EUR")))
            .thenReturn(account);

        ResponseEntity<AccountResponse> response = accountController.createAccount(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(account.id());
        assertThat(response.getBody().getBalance()).isEqualByComparingTo(account.balance());
    }

    @Test
    void getAccountsByUser_returnsOkWithList() {
        UUID userId = UUID.randomUUID();
        List<Account> accounts = List.of(sampleAccount(userId), sampleAccount(userId));

        when(accountUseCase.findAccessibleByUserId(userId)).thenReturn(accounts);

        ResponseEntity<List<AccountResponse>> response = accountController.getAccountsByUser(userId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    void getAccountById_returnsOk() {
        UUID ownerId = UUID.randomUUID();
        Account account = sampleAccount(ownerId);

        when(accountUseCase.findById(account.id())).thenReturn(account);

        ResponseEntity<AccountResponse> response = accountController.getAccountById(account.id());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(account.id());
    }

    @Test
    void addMember_returnsNoContent() {
        UUID accountId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        AddMemberRequest request = new AddMemberRequest().userId(userId);

        doNothing().when(accountUseCase).addMember(accountId, userId);

        ResponseEntity<Void> response = accountController.addMember(accountId, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(accountUseCase).addMember(accountId, userId);
    }

    @Test
    void removeMember_returnsNoContent() {
        UUID accountId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        doNothing().when(accountUseCase).removeMember(accountId, userId);

        ResponseEntity<Void> response = accountController.removeMember(accountId, userId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(accountUseCase).removeMember(accountId, userId);
    }
}

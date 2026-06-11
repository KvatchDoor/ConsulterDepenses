package com.consulter.application.service;

import com.consulter.application.exception.MemberAlreadyExistsException;
import com.consulter.application.exception.ResourceNotFoundException;
import com.consulter.domain.model.Account;
import com.consulter.domain.port.out.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    void create_savesAccountWithZeroBalanceAndProvidedCurrency() {
        UUID ownerId = UUID.randomUUID();
        Account saved = new Account(UUID.randomUUID(), ownerId, "Courant", BigDecimal.ZERO, "USD", null, null);
        when(accountRepository.save(any())).thenReturn(saved);

        Account result = accountService.create(ownerId, "Courant", "USD");

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(captor.capture());
        assertThat(captor.getValue().balance()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(captor.getValue().currency()).isEqualTo("USD");
        assertThat(result).isEqualTo(saved);
    }

    @Test
    void create_usesEurAsDefaultCurrency_whenCurrencyIsNull() {
        UUID ownerId = UUID.randomUUID();
        when(accountRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Account result = accountService.create(ownerId, "Epargne", null);

        assertThat(result.currency()).isEqualTo("EUR");
    }

    @Test
    void findById_returnsAccount_whenFound() {
        UUID id = UUID.randomUUID();
        Account account = new Account(id, UUID.randomUUID(), "Test", BigDecimal.TEN, "EUR", null, null);
        when(accountRepository.findById(id)).thenReturn(Optional.of(account));

        assertThat(accountService.findById(id)).isEqualTo(account);
    }

    @Test
    void findById_throwsResourceNotFoundException_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(accountRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.findById(id))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining(id.toString());
    }

    @Test
    void findAccessibleByUserId_returnsList() {
        UUID userId = UUID.randomUUID();
        List<Account> accounts = List.of(
            new Account(UUID.randomUUID(), userId, "A", BigDecimal.ZERO, "EUR", null, null)
        );
        when(accountRepository.findAccessibleByUserId(userId)).thenReturn(accounts);

        assertThat(accountService.findAccessibleByUserId(userId)).isEqualTo(accounts);
    }

    @Test
    void addMember_delegatesToRepository() {
        UUID accountId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        when(accountRepository.existsMember(accountId, userId)).thenReturn(false);

        accountService.addMember(accountId, userId);

        verify(accountRepository).addMember(accountId, userId);
    }

    @Test
    void addMember_throwsMemberAlreadyExistsException_whenAlreadyMember() {
        UUID accountId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        when(accountRepository.existsMember(accountId, userId)).thenReturn(true);

        assertThatThrownBy(() -> accountService.addMember(accountId, userId))
            .isInstanceOf(MemberAlreadyExistsException.class)
            .hasMessageContaining(userId.toString())
            .hasMessageContaining(accountId.toString());
    }

    @Test
    void removeMember_delegatesToRepository() {
        UUID accountId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        accountService.removeMember(accountId, userId);

        verify(accountRepository).removeMember(accountId, userId);
    }
}

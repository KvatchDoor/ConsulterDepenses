package com.consulter.application.service;

import com.consulter.application.exception.ResourceNotFoundException;
import com.consulter.domain.model.Account;
import com.consulter.domain.model.Movement;
import com.consulter.domain.model.MovementType;
import com.consulter.domain.port.out.AccountRepository;
import com.consulter.domain.port.out.MovementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovementServiceTest {

    @Mock
    private MovementRepository movementRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private MovementService movementService;

    private Account accountWithBalance(BigDecimal balance) {
        return new Account(UUID.randomUUID(), UUID.randomUUID(), "Test", balance, "EUR", null, null);
    }

    @Test
    void create_creditMovement_increasesAccountBalance() {
        Account account = accountWithBalance(new BigDecimal("100.00"));
        Movement saved = new Movement(UUID.randomUUID(), account.id(), UUID.randomUUID(), null,
            MovementType.CREDIT, new BigDecimal("50.00"), "virement", LocalDate.now(), null, null);

        when(accountRepository.findById(account.id())).thenReturn(Optional.of(account));
        when(movementRepository.save(any())).thenReturn(saved);
        when(accountRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        movementService.create(account.id(), saved.createdBy(), null,
            MovementType.CREDIT, new BigDecimal("50.00"), "virement", LocalDate.now());

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(captor.capture());
        assertThat(captor.getValue().balance()).isEqualByComparingTo(new BigDecimal("150.00"));
    }

    @Test
    void create_debitMovement_decreasesAccountBalance() {
        Account account = accountWithBalance(new BigDecimal("100.00"));
        Movement saved = new Movement(UUID.randomUUID(), account.id(), UUID.randomUUID(), null,
            MovementType.DEBIT, new BigDecimal("30.00"), "achat", LocalDate.now(), null, null);

        when(accountRepository.findById(account.id())).thenReturn(Optional.of(account));
        when(movementRepository.save(any())).thenReturn(saved);
        when(accountRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        movementService.create(account.id(), saved.createdBy(), null,
            MovementType.DEBIT, new BigDecimal("30.00"), "achat", LocalDate.now());

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(captor.capture());
        assertThat(captor.getValue().balance()).isEqualByComparingTo(new BigDecimal("70.00"));
    }

    @Test
    void create_throwsResourceNotFoundException_whenAccountNotFound() {
        UUID accountId = UUID.randomUUID();
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> movementService.create(accountId, UUID.randomUUID(), null,
            MovementType.DEBIT, BigDecimal.TEN, "test", LocalDate.now()))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining(accountId.toString());
    }

    @Test
    void findById_returnsMovement_whenFound() {
        UUID id = UUID.randomUUID();
        Movement movement = new Movement(id, UUID.randomUUID(), UUID.randomUUID(), null,
            MovementType.CREDIT, BigDecimal.TEN, "test", LocalDate.now(), null, null);
        when(movementRepository.findById(id)).thenReturn(Optional.of(movement));

        assertThat(movementService.findById(id)).isEqualTo(movement);
    }

    @Test
    void findById_throwsResourceNotFoundException_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(movementRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> movementService.findById(id))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining(id.toString());
    }

    @Test
    void findByAccountId_returnsList() {
        UUID accountId = UUID.randomUUID();
        List<Movement> movements = List.of(
            new Movement(UUID.randomUUID(), accountId, UUID.randomUUID(), null,
                MovementType.DEBIT, BigDecimal.TEN, "test", LocalDate.now(), null, null)
        );
        when(movementRepository.findByAccountId(accountId)).thenReturn(movements);

        assertThat(movementService.findByAccountId(accountId)).isEqualTo(movements);
    }

    @Test
    void delete_callsRepositoryDeleteById() {
        Account account = accountWithBalance(new BigDecimal("100.00"));
        UUID id = UUID.randomUUID();
        Movement movement = new Movement(id, account.id(), UUID.randomUUID(), null,
            MovementType.DEBIT, BigDecimal.TEN, "test", LocalDate.now(), null, null);

        when(movementRepository.findById(id)).thenReturn(Optional.of(movement));
        when(accountRepository.findById(account.id())).thenReturn(Optional.of(account));
        when(accountRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        movementService.delete(id);

        verify(movementRepository).deleteById(id);
    }

    // --- Bug exposing tests (intentionally failing on current code) ---

    @Test
    void delete_creditMovement_reversesBalanceOnAccount() {
        // Arrange — compte à 150 €, CRÉDIT de 50 € existant
        Account account = accountWithBalance(new BigDecimal("150.00"));
        UUID movementId = UUID.randomUUID();
        Movement creditMovement = new Movement(movementId, account.id(), UUID.randomUUID(), null,
            MovementType.CREDIT, new BigDecimal("50.00"), "virement entrant", LocalDate.now(), null, null);

        when(movementRepository.findById(movementId)).thenReturn(Optional.of(creditMovement));
        when(accountRepository.findById(account.id())).thenReturn(Optional.of(account));
        when(accountRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        movementService.delete(movementId);

        // Assert — le solde doit être réduit de 50 € (reversal du CRÉDIT)
        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(captor.capture());
        assertThat(captor.getValue().balance()).isEqualByComparingTo(new BigDecimal("100.00"));
    }

    @Test
    void delete_debitMovement_reversesBalanceOnAccount() {
        // Arrange — compte à 70 €, DÉBIT de 30 € existant
        Account account = accountWithBalance(new BigDecimal("70.00"));
        UUID movementId = UUID.randomUUID();
        Movement debitMovement = new Movement(movementId, account.id(), UUID.randomUUID(), null,
            MovementType.DEBIT, new BigDecimal("30.00"), "achat", LocalDate.now(), null, null);

        when(movementRepository.findById(movementId)).thenReturn(Optional.of(debitMovement));
        when(accountRepository.findById(account.id())).thenReturn(Optional.of(account));
        when(accountRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        movementService.delete(movementId);

        // Assert — le solde doit être augmenté de 30 € (reversal du DÉBIT)
        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(captor.capture());
        assertThat(captor.getValue().balance()).isEqualByComparingTo(new BigDecimal("100.00"));
    }

    @Test
    void delete_throwsResourceNotFoundException_whenMovementNotFound() {
        UUID unknownId = UUID.randomUUID();
        when(movementRepository.findById(unknownId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> movementService.delete(unknownId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining(unknownId.toString());
    }
}

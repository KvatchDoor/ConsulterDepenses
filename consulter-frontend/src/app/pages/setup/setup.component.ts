import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AccountService } from '../../services/account.service';
import { StateService } from '../../services/state.service';

@Component({
  selector: 'app-setup',
  standalone: true,
  imports: [FormsModule],
  template: `
    <div class="setup">
      <div class="setup__hero">
        <div class="setup__logo">💳</div>
        <h1 class="setup__title">Consulter Dépenses</h1>
        <p class="setup__subtitle">Gérez vos finances personnelles simplement</p>
      </div>

      <div class="setup__form">
        @if (error()) {
          <div class="error-banner">{{ error() }}</div>
        }

        <div class="field">
          <label class="label" for="userId">Votre identifiant utilisateur (UUID)</label>
          <input
            id="userId"
            class="input input--mono"
            type="text"
            placeholder="xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
            [(ngModel)]="userId"
            [disabled]="loading()"
          />
          @if (userId && !isValidUuid(userId)) {
            <span class="field-hint field-hint--error">Format UUID invalide</span>
          }
        </div>

        <div class="field">
          <label class="label" for="accountName">Nom du compte</label>
          <input
            id="accountName"
            class="input"
            type="text"
            placeholder="ex: Mes finances"
            [(ngModel)]="accountName"
            [disabled]="loading()"
          />
        </div>

        <div class="field">
          <label class="label" for="currency">Devise</label>
          <select id="currency" class="input input--select" [(ngModel)]="currency" [disabled]="loading()">
            <option value="EUR">EUR — Euro (€)</option>
            <option value="USD">USD — Dollar américain ($)</option>
            <option value="GBP">GBP — Livre sterling (£)</option>
            <option value="CHF">CHF — Franc suisse (CHF)</option>
            <option value="CAD">CAD — Dollar canadien ($)</option>
          </select>
        </div>

        <button
          class="btn-primary"
          (click)="submit()"
          [disabled]="loading() || !isValidUuid(userId) || !accountName.trim()"
        >
          @if (loading()) {
            <span class="spinner"></span>
            Création en cours...
          } @else {
            Commencer →
          }
        </button>
      </div>
    </div>
  `,
  styleUrl: './setup.component.css',
})
export class SetupComponent implements OnInit {
  private readonly state = inject(StateService);
  private readonly accountService = inject(AccountService);
  private readonly router = inject(Router);

  userId = '';
  accountName = 'Mes finances';
  currency = 'EUR';
  loading = signal(false);
  error = signal<string | null>(null);

  private readonly UUID_RE =
    /^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/i;

  ngOnInit(): void {
    if (this.state.isReady()) {
      this.router.navigate(['/']);
    }
  }

  isValidUuid(value: string): boolean {
    return this.UUID_RE.test(value.trim());
  }

  submit(): void {
    const uid = this.userId.trim();
    if (!this.isValidUuid(uid) || !this.accountName.trim()) return;

    this.loading.set(true);
    this.error.set(null);

    this.accountService
      .create({ ownerId: uid, name: this.accountName.trim(), currency: this.currency })
      .subscribe({
        next: (account) => {
          this.state.configure(account.id, uid, uid.slice(0, 8), account.currency);
          this.router.navigate(['/']);
        },
        error: () => {
          this.loading.set(false);
          this.error.set(
            "Impossible de créer le compte. Vérifiez que le backend est démarré sur localhost:8080.",
          );
        },
      });
  }
}

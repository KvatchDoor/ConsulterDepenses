import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { BottomNavComponent } from '../../components/bottom-nav/bottom-nav.component';
import { Category } from '../../models/category.model';
import { MovementType } from '../../models/movement.model';
import { CategoryService } from '../../services/category.service';
import { MovementService } from '../../services/movement.service';
import { StateService } from '../../services/state.service';

@Component({
  selector: 'app-new-movement',
  standalone: true,
  imports: [FormsModule, BottomNavComponent],
  template: `
    <div class="page">
      <header class="header">
        <button class="back-btn" (click)="goBack()">
          <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
            <path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7" />
          </svg>
        </button>
        <h1 class="header__title">Nouvelle opération</h1>
        <div style="width:38px"></div>
      </header>

      @if (error()) {
        <div class="error-banner">{{ error() }}</div>
      }

      <!-- Type toggle -->
      <div class="type-toggle">
        <button
          class="type-btn"
          [class.type-btn--debit-active]="type() === 'DEBIT'"
          (click)="type.set('DEBIT')"
        >
          <span class="type-btn__dot type-btn__dot--debit">↓</span>
          Dépense
        </button>
        <button
          class="type-btn"
          [class.type-btn--credit-active]="type() === 'CREDIT'"
          (click)="type.set('CREDIT')"
        >
          <span class="type-btn__dot type-btn__dot--credit">↑</span>
          Rentrée
        </button>
      </div>

      <!-- Amount -->
      <div class="amount-wrap" [class.amount-wrap--credit]="type() === 'CREDIT'" [class.amount-wrap--invalid]="amountError()">
        <span class="amount-sign">{{ type() === 'CREDIT' ? '+' : '−' }}</span>
        <input
          class="amount-input"
          type="number"
          min="0.01"
          max="10000"
          step="0.01"
          placeholder="0,00"
          [(ngModel)]="amount"
          inputmode="decimal"
          (ngModelChange)="onAmountChange()"
          aria-label="Montant"
          [attr.aria-invalid]="amountError() ? 'true' : null"
          aria-describedby="amount-error"
        />
        <span class="amount-currency">{{ currency() }}</span>
      </div>
      <div
        id="amount-error"
        class="amount-error"
        role="alert"
        aria-live="polite"
        [class.amount-error--visible]="amountError()"
      >
        @if (amountError()) {
          {{ amountError() }}
        }
      </div>

      <!-- Form fields -->
      <div class="form">
        <div class="field">
          <label class="label">Date</label>
          <input class="input" type="date" [(ngModel)]="date" />
        </div>

        <div class="field">
          <label class="label">Catégorie <span class="opt">(optionnel)</span></label>
          <div class="cat-grid">
            @for (cat of categories(); track cat.id) {
              <button
                class="cat-item"
                [class.cat-item--selected]="selectedCat() === cat.id"
                [style.--cc]="cat.color"
                (click)="toggleCat(cat.id)"
              >
                <span class="cat-item__icon">{{ cat.icon }}</span>
                <span class="cat-item__lbl">{{ cat.label }}</span>
              </button>
            }
          </div>
        </div>

        <div class="field">
          <label class="label">Description <span class="opt">(optionnel)</span></label>
          <input
            class="input"
            type="text"
            placeholder="ex: Courses Carrefour"
            [(ngModel)]="description"
          />
        </div>

        <button
          class="submit-btn"
          [class.submit-btn--credit]="type() === 'CREDIT'"
          (click)="submit()"
          [disabled]="loading() || !amount || !!amountError()"
        >
          @if (loading()) {
            <span class="spinner"></span>
          } @else {
            Enregistrer l'opération
          }
        </button>
      </div>

      <div style="height:80px"></div>
      <app-bottom-nav />
    </div>
  `,
  styleUrl: './new-movement.component.css',
})
export class NewMovementComponent implements OnInit {
  private readonly state = inject(StateService);
  private readonly movementSvc = inject(MovementService);
  private readonly categorySvc = inject(CategoryService);
  private readonly router = inject(Router);

  type = signal<MovementType>('DEBIT');
  amount: number | null = null;
  date = new Date().toISOString().split('T')[0];
  description = '';
  selectedCat = signal<string | null>(null);
  categories = signal<Category[]>([]);
  loading = signal(false);
  error = signal<string | null>(null);
  readonly currency = this.state.currency;

  /** Signal mis à jour lors de chaque changement du champ montant */
  private readonly amountValue = signal<number | null>(null);

  /** Message d'erreur calculé à partir de la valeur saisie */
  readonly amountError = computed<string | null>(() => {
    const v = this.amountValue();
    if (v === null) return null;
    if (v <= 0) return 'Le montant doit être supérieur à 0';
    if (v > 10000) return 'Le montant ne peut pas dépasser 10 000 €';
    // Vérifie que la valeur n'a pas plus de 2 décimales
    const str = v.toString();
    const dot = str.indexOf('.');
    if (dot !== -1 && str.length - dot - 1 > 2) {
      return 'Le montant ne peut pas avoir plus de 2 décimales';
    }
    return null;
  });

  ngOnInit(): void {
    if (!this.state.isReady()) {
      this.router.navigate(['/setup']);
      return;
    }
    this.categorySvc.getAll().subscribe((cats) => this.categories.set(cats));
  }

  onAmountChange(): void {
    this.amountValue.set(this.amount);
  }

  toggleCat(id: string): void {
    this.selectedCat.set(this.selectedCat() === id ? null : id);
  }

  goBack(): void {
    this.router.navigate(['/']);
  }

  submit(): void {
    if (!this.amount || this.amountError()) return;

    this.loading.set(true);
    this.error.set(null);

    this.movementSvc
      .create({
        accountId: this.state.accountId()!,
        createdBy: this.state.userId()!,
        type: this.type(),
        amount: this.amount,
        movementDate: this.date,
        categoryId: this.selectedCat() ?? undefined,
        description: this.description.trim() || undefined,
      })
      .subscribe({
        next: () => this.router.navigate(['/']),
        error: () => {
          this.loading.set(false);
          this.error.set(
            "Impossible d'enregistrer l'opération. Vérifiez que le backend est démarré.",
          );
        },
      });
  }
}

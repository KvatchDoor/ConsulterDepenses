import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { forkJoin } from 'rxjs';
import { BottomNavComponent } from '../../components/bottom-nav/bottom-nav.component';
import { Account } from '../../models/account.model';
import { Category } from '../../models/category.model';
import { Movement, MovementGroup, MovementType } from '../../models/movement.model';
import { AccountService } from '../../services/account.service';
import { CategoryService } from '../../services/category.service';
import { MovementService } from '../../services/movement.service';
import { StateService } from '../../services/state.service';

type Filter = 'ALL' | MovementType;

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [BottomNavComponent],
  template: `
    <div class="page">
      <header class="header">
        <div class="header__left">
          <div class="avatar">{{ userName()?.charAt(0)?.toUpperCase() || '?' }}</div>
          <span class="header__greeting">Bonjour, {{ userName() || 'vous' }} 👋</span>
        </div>
        <button class="icon-btn" (click)="confirmReset()" title="Paramètres">
          <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
            <path stroke-linecap="round" stroke-linejoin="round" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z" />
            <path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
          </svg>
        </button>
      </header>

      @if (loading()) {
        <div class="center-content">
          <div class="spinner"></div>
        </div>
      } @else if (error()) {
        <div class="center-content">
          <div class="error-state">
            <div class="error-icon">⚠️</div>
            <p>{{ error() }}</p>
            <button class="btn-retry" (click)="load()">Réessayer</button>
          </div>
        </div>
      } @else {
        <!-- Balance Card -->
        <div class="balance-card">
          <div class="balance-card__name">{{ account()?.name }}</div>
          <div class="balance-card__amount">
            {{ fmt(account()?.balance ?? 0) }}&nbsp;<span class="balance-card__currency">{{ account()?.currency }}</span>
          </div>
          <div class="balance-card__stats">
            <div class="stat">
              <div class="stat__icon stat__icon--credit">↑</div>
              <div>
                <div class="stat__lbl">Rentrées</div>
                <div class="stat__val stat__val--credit">+{{ fmt(totalCredit()) }}</div>
              </div>
            </div>
            <div class="stat-sep"></div>
            <div class="stat">
              <div class="stat__icon stat__icon--debit">↓</div>
              <div>
                <div class="stat__lbl">Dépenses</div>
                <div class="stat__val stat__val--debit">-{{ fmt(totalDebit()) }}</div>
              </div>
            </div>
          </div>
        </div>

        <!-- Filter tabs -->
        <div class="filters">
          <button class="filter-btn" [class.active]="filter() === 'ALL'" (click)="filter.set('ALL')">Tout</button>
          <button class="filter-btn" [class.active]="filter() === 'CREDIT'" (click)="filter.set('CREDIT')">Rentrées</button>
          <button class="filter-btn" [class.active]="filter() === 'DEBIT'" (click)="filter.set('DEBIT')">Dépenses</button>
        </div>

        <!-- Movements list -->
        <div class="movements">
          @if (groupedMovements().length === 0) {
            <div class="empty">
              <div class="empty__icon">📊</div>
              <p class="empty__title">Aucune opération</p>
              <p class="empty__sub">Appuyez sur "Saisir" pour ajouter une opération</p>
            </div>
          } @else {
            @for (group of groupedMovements(); track group.date) {
              <div class="group">
                <div class="group__label">{{ group.dateLabel }}</div>
                @for (m of group.movements; track m.id) {
                  <div class="card" [class.card--deleting]="deletingId() === m.id">
                    <div class="card__icon" [style.background]="catBg(m.categoryId)">
                      {{ catIcon(m.categoryId) }}
                    </div>
                    <div class="card__body">
                      <div class="card__desc">{{ m.description || catLabel(m.categoryId) }}</div>
                      <div class="card__cat">{{ catLabel(m.categoryId) }}</div>
                    </div>
                    <div class="card__right">
                      <div class="card__amount" [class.credit]="m.type === 'CREDIT'" [class.debit]="m.type === 'DEBIT'">
                        {{ m.type === 'CREDIT' ? '+' : '-' }}{{ fmt(m.amount) }}
                      </div>
                      <button class="del-btn" (click)="deleteMovement(m.id)" title="Supprimer" [disabled]="!!deletingId()">
                        <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                          <path stroke-linecap="round" stroke-linejoin="round" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                        </svg>
                      </button>
                    </div>
                  </div>
                }
              </div>
            }
          }
        </div>
      }

      <div style="height:80px"></div>
      <app-bottom-nav />
    </div>
  `,
  styleUrl: './dashboard.component.css',
})
export class DashboardComponent implements OnInit {
  private readonly state = inject(StateService);
  private readonly accountSvc = inject(AccountService);
  private readonly movementSvc = inject(MovementService);
  private readonly categorySvc = inject(CategoryService);
  private readonly router = inject(Router);

  account = signal<Account | null>(null);
  movements = signal<Movement[]>([]);
  categories = signal<Category[]>([]);
  loading = signal(true);
  error = signal<string | null>(null);
  filter = signal<Filter>('ALL');
  deletingId = signal<string | null>(null);

  readonly userName = this.state.userName;

  readonly totalCredit = computed(() =>
    this.movements()
      .filter((m) => m.type === 'CREDIT')
      .reduce((s, m) => s + m.amount, 0),
  );

  readonly totalDebit = computed(() =>
    this.movements()
      .filter((m) => m.type === 'DEBIT')
      .reduce((s, m) => s + m.amount, 0),
  );

  readonly groupedMovements = computed((): MovementGroup[] => {
    const f = this.filter();
    const filtered = f === 'ALL' ? this.movements() : this.movements().filter((m) => m.type === f);

    const map = new Map<string, Movement[]>();
    for (const m of filtered) {
      if (!map.has(m.movementDate)) map.set(m.movementDate, []);
      map.get(m.movementDate)!.push(m);
    }

    return Array.from(map.entries())
      .sort((a, b) => b[0].localeCompare(a[0]))
      .map(([date, movements]) => ({ date, dateLabel: this.dateLabel(date), movements }));
  });

  ngOnInit(): void {
    if (!this.state.isReady()) {
      this.router.navigate(['/setup']);
      return;
    }
    this.load();
  }

  load(): void {
    const accountId = this.state.accountId()!;
    this.loading.set(true);
    this.error.set(null);

    forkJoin({
      account: this.accountSvc.getById(accountId),
      movements: this.movementSvc.getByAccount(accountId),
      categories: this.categorySvc.getAll(),
    }).subscribe({
      next: ({ account, movements, categories }) => {
        this.account.set(account);
        this.movements.set(movements);
        this.categories.set(categories);
        this.loading.set(false);
      },
      error: () => {
        this.error.set("Impossible de charger les données. Vérifiez que le backend est démarré sur localhost:8080.");
        this.loading.set(false);
      },
    });
  }

  deleteMovement(id: string): void {
    if (this.deletingId()) return;
    this.deletingId.set(id);

    this.movementSvc.delete(id).subscribe({
      next: () => {
        this.movements.update((ms) => ms.filter((m) => m.id !== id));
        this.deletingId.set(null);
        this.accountSvc.getById(this.state.accountId()!).subscribe((a) => this.account.set(a));
      },
      error: () => this.deletingId.set(null),
    });
  }

  confirmReset(): void {
    if (confirm('Réinitialiser la configuration locale ?\nL\'historique reste sur le serveur.')) {
      this.state.reset();
      this.router.navigate(['/setup']);
    }
  }

  catIcon(id?: string | null): string {
    return this.categories().find((c) => c.id === id)?.icon ?? '💫';
  }

  catBg(id?: string | null): string {
    const color = this.categories().find((c) => c.id === id)?.color;
    return color ? `${color}22` : '#f1f5f9';
  }

  catLabel(id?: string | null): string {
    return this.categories().find((c) => c.id === id)?.label ?? 'Général';
  }

  fmt(amount: number): string {
    return amount.toLocaleString('fr-FR', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
  }

  private dateLabel(d: string): string {
    const today = new Date().toISOString().split('T')[0];
    const yest = new Date(Date.now() - 86400000).toISOString().split('T')[0];
    if (d === today) return "Aujourd'hui";
    if (d === yest) return 'Hier';
    const dt = new Date(`${d}T00:00:00`);
    return dt.toLocaleDateString('fr-FR', { weekday: 'long', day: 'numeric', month: 'long' });
  }
}

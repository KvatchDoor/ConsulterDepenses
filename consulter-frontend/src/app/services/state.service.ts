import { Injectable, PLATFORM_ID, inject, signal } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

@Injectable({ providedIn: 'root' })
export class StateService {
  private readonly platformId = inject(PLATFORM_ID);

  private readonly ACCOUNT_ID_KEY = 'cd_account_id';
  private readonly USER_ID_KEY = 'cd_user_id';
  private readonly USER_NAME_KEY = 'cd_user_name';
  private readonly CURRENCY_KEY = 'cd_currency';

  accountId = signal<string | null>(this.getItem(this.ACCOUNT_ID_KEY));
  userId = signal<string | null>(this.getItem(this.USER_ID_KEY));
  userName = signal<string | null>(this.getItem(this.USER_NAME_KEY));
  currency = signal<string>(this.getItem(this.CURRENCY_KEY) ?? 'EUR');

  private getItem(key: string): string | null {
    if (isPlatformBrowser(this.platformId)) {
      return localStorage.getItem(key);
    }
    return null;
  }

  private setItem(key: string, value: string): void {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.setItem(key, value);
    }
  }

  private removeItem(key: string): void {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem(key);
    }
  }

  configure(accountId: string, userId: string, userName: string, currency: string): void {
    this.setItem(this.ACCOUNT_ID_KEY, accountId);
    this.setItem(this.USER_ID_KEY, userId);
    this.setItem(this.USER_NAME_KEY, userName);
    this.setItem(this.CURRENCY_KEY, currency);
    this.accountId.set(accountId);
    this.userId.set(userId);
    this.userName.set(userName);
    this.currency.set(currency);
  }

  isReady(): boolean {
    return !!(this.accountId() && this.userId());
  }

  reset(): void {
    this.removeItem(this.ACCOUNT_ID_KEY);
    this.removeItem(this.USER_ID_KEY);
    this.removeItem(this.USER_NAME_KEY);
    this.removeItem(this.CURRENCY_KEY);
    this.accountId.set(null);
    this.userId.set(null);
    this.userName.set(null);
    this.currency.set('EUR');
  }

  newUuid(): string {
    if (isPlatformBrowser(this.platformId) && typeof crypto !== 'undefined') {
      return crypto.randomUUID();
    }
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, (c) => {
      const r = (Math.random() * 16) | 0;
      return (c === 'x' ? r : (r & 0x3) | 0x8).toString(16);
    });
  }
}

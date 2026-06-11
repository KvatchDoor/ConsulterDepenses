import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Account, CreateAccountRequest } from '../models/account.model';

@Injectable({ providedIn: 'root' })
export class AccountService {
  private readonly http = inject(HttpClient);
  private readonly url = `${environment.apiUrl}/accounts`;

  create(req: CreateAccountRequest): Observable<Account> {
    return this.http.post<Account>(this.url, req);
  }

  getById(id: string): Observable<Account> {
    return this.http.get<Account>(`${this.url}/${id}`);
  }
}

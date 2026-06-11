import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Movement, CreateMovementRequest } from '../models/movement.model';

@Injectable({ providedIn: 'root' })
export class MovementService {
  private readonly http = inject(HttpClient);
  private readonly url = `${environment.apiUrl}/movements`;

  create(req: CreateMovementRequest): Observable<Movement> {
    return this.http.post<Movement>(this.url, req);
  }

  getByAccount(accountId: string): Observable<Movement[]> {
    return this.http.get<Movement[]>(this.url, { params: { accountId } });
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}

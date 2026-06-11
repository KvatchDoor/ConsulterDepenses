import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, map, of } from 'rxjs';
import { environment } from '../../environments/environment';
import { Category } from '../models/category.model';

const FALLBACK: Category[] = [
  { id: 'f1', label: 'Alimentation', color: '#FF6B6B', icon: '🍕' },
  { id: 'f2', label: 'Logement', color: '#4ECDC4', icon: '🏠' },
  { id: 'f3', label: 'Transport', color: '#45B7D1', icon: '🚗' },
  { id: 'f4', label: 'Santé', color: '#96CEB4', icon: '💊' },
  { id: 'f5', label: 'Loisirs', color: '#FFEAA7', icon: '🎮' },
  { id: 'f6', label: 'Revenus', color: '#A29BFE', icon: '💰' },
  { id: 'f7', label: 'Shopping', color: '#FD79A8', icon: '🛍️' },
  { id: 'f8', label: 'Voyage', color: '#00CEC9', icon: '✈️' },
  { id: 'f9', label: 'Éducation', color: '#6C5CE7', icon: '📚' },
  { id: 'f10', label: 'Factures', color: '#E17055', icon: '💡' },
  { id: 'f11', label: 'Cadeaux', color: '#00B894', icon: '🎁' },
  { id: 'f12', label: 'Autre', color: '#B2BEC3', icon: '❓' },
];

const ICON_BY_LABEL: Record<string, string> = Object.fromEntries(
  FALLBACK.map((c) => [c.label.toLowerCase(), c.icon])
);

@Injectable({ providedIn: 'root' })
export class CategoryService {
  private readonly http = inject(HttpClient);
  private readonly url = `${environment.apiUrl}/categories`;

  getAll(): Observable<Category[]> {
    return this.http.get<Category[]>(this.url).pipe(
      map((cats) => {
        if (cats.length === 0) return FALLBACK;
        return cats.map((c) => ({
          ...c,
          icon: c.icon || ICON_BY_LABEL[c.label.toLowerCase()] || '❓',
        }));
      }),
      catchError(() => of(FALLBACK)),
    );
  }

  getFallback(): Category[] {
    return FALLBACK;
  }
}

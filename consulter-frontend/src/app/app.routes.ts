import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./pages/dashboard/dashboard.component').then((m) => m.DashboardComponent),
  },
  {
    path: 'nouveau',
    loadComponent: () =>
      import('./pages/new-movement/new-movement.component').then((m) => m.NewMovementComponent),
  },
  {
    path: 'setup',
    loadComponent: () =>
      import('./pages/setup/setup.component').then((m) => m.SetupComponent),
  },
  { path: '**', redirectTo: '' },
];

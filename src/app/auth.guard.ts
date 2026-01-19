
import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const token = typeof window !== 'undefined' ? localStorage.getItem('token') : null;
  const role = localStorage.getItem('userRole') || '';

  if (!token) {
    router.navigate(['/login']);
    return false;
  }
  //Done
  // ✅ Role-based restriction for /admin
  if (state.url.startsWith('/admin') && role.toUpperCase() !== 'ADMIN') {
    router.navigate(['/']);
    return false;
  }

  return true;

};

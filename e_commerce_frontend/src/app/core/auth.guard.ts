import { ActivatedRouteSnapshot, CanActivateFn, RouterStateSnapshot } from '@angular/router';
import { AuthService } from './auth.service';
import { HotToastService } from '@ngxpert/hot-toast';
import { inject } from '@angular/core';

export const authGuard: CanActivateFn = ( route: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
  // const authService = inject(AuthService);
  // const toast = inject(HotToastService);

  // if (authService.isAuthenticated()) {
  //   return true;
  // }
  
  // toast.error('Please login to access this page');
  // return authService.router.createUrlTree(['/login']);

   const authService = inject(AuthService);
    const toast = inject(HotToastService);

  // Check authentication
  if (!authService.isAuthenticated()) {
    toast.error('Please login to access this page');
    return authService.router.createUrlTree(['/login']);
  }

  // Check roles (if provided)
  const requiredRoles = route.data['roles'] as string[];
  if (requiredRoles && requiredRoles.length > 0) {
    const user = authService.currentUserValue;
    const hasRole = user?.roles?.some(role => requiredRoles.includes(role));

    if (!hasRole) {
      toast.error('You are not authorized to view this page');
      return authService.router.createUrlTree(['/unauthorized']);
    }
  }

  return true;
};

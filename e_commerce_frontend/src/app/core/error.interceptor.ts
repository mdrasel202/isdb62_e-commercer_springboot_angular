import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { HotToastService } from '@ngxpert/hot-toast';
import { catchError, throwError } from 'rxjs';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const toast = inject(HotToastService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) =>{
      if(error.status === 401){
        toast.error('Please login to continue');
         router.navigate(['/login']);
      } else if (error.status === 403) {
        toast.error('You are not authorized to access this resource');
      } else if (error.status === 404) {
        toast.error('Resource not found');
      } else if (error.status >= 500) {
        toast.error('Server error occurred');
      }
      return throwError(() => error);
      
    })
  );
};

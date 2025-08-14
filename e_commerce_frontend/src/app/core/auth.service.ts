import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from './environment';
import { BehaviorSubject, map, Observable, single, Subject } from 'rxjs';
import { UserProfile } from '../model/userProfile.model';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Login } from '../model/login.model';
import { Register } from '../model/register.model';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
 private apiUrl = environment.apiUrl;
 private currentUserSubject: BehaviorSubject<UserProfile | null>;
 private currentUser: Observable<UserProfile | null>;
 private jwtHelper = new JwtHelperService();


  constructor(
     private http: HttpClient,
     public router: Router,
     private toastr: ToastrService
  ) {
    this.currentUserSubject = new BehaviorSubject<UserProfile | null>(
      JSON.parse(localStorage.getItem('currentUser') || 'null')
    );
    this.currentUser = this.currentUserSubject.asObservable();
   }

   public get currentUserValue(): UserProfile | null{
    return this.currentUserSubject.value;
   }

   public getToken(): string | null{
    return localStorage.getItem('token');
   }

   login(loginData: Login): Observable<UserProfile>{
    return this.http.post<any>(`${this.apiUrl}/api/auth/signin`, loginData).pipe(
      map(response => {
        localStorage.setItem('token', response.token);
        const userProfile = {
           id: response.id,
           email: response.email,
           firstName: response.firstName,
           lastName: response.lastName,
           roles: response.roles
        };
        localStorage.setItem('currentUser', JSON.stringify(userProfile));
        this.currentUserSubject.next(userProfile);
        return userProfile;
      })
    );
   }

   register(registerData: Register): Observable<any>{
    return this.http.post(`${this.apiUrl}/api/auth/signup`, registerData)
   }

   logout(): void{
    localStorage.removeItem('token');
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
    this.router.navigate(['/home']);
    this.toastr.success('Logged out successfully');
   }

   isAuthenticated(): boolean {
    const token = this.getToken();
    return token ? !this.jwtHelper.isTokenExpired(token) : false;
   }

   hasRole(role: string): boolean {
    const user = this.currentUserValue;
    if(!user || !user.roles) return false;
    return user.roles.includes(role);
   }
}

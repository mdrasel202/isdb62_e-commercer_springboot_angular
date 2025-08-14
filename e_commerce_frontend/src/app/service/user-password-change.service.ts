import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PasswordChange } from '../model/password_change';

@Injectable({
  providedIn: 'root'
})
export class UserPasswordChangeService {

  private baseUrl = 'http://localhost:8080/api/user';

  constructor(private http:HttpClient) { }

  passwordChanged(pass : PasswordChange) : Observable<any>{
    return this.http.post(`${this.baseUrl}/change-password`, pass);
  }
}

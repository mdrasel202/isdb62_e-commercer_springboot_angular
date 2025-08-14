import { Injectable } from '@angular/core';
import { environment } from './environment';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserProfile } from '../model/userProfile.model';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
 private imageBaseUrl = environment.imageBaseUrl;

  constructor(private http: HttpClient) { }

  // getProfile(): Observable<UserProfile>{
  //   return this.http.get<UserProfile>(`${this.imageBaseUrl}/profile`);
  // }
getProfile(): Observable<UserProfile> {
  const token = localStorage.getItem('token');
  const headers = new HttpHeaders({
    'Authorization': `Bearer ${token}`
  });

  return this.http.get<UserProfile>('http://localhost:8080/api/profile', { headers });
}

  // updateProfile(profileData: any):Observable<UserProfile>{
  //   return this.http.put<UserProfile>(`${this.imageBaseUrl}/profile`, profileData);
  // }

  uploadProfilePhoto(photo: File): Observable<any>{
    const formData = new FormData();
    formData.append('file', photo);
    return this.http.post(`${this.imageBaseUrl}/profile/photo`, formData);
  }

  editProfile(edit: any):Observable<UserProfile>{
    return this.http.put<UserProfile>(`${this.imageBaseUrl}/profile/editProfile/update`, edit);
  }
}

import { Component, OnInit } from '@angular/core';
import { UserProfile } from '../../model/userProfile.model';
import { ProfileService } from '../../core/profile.service';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/auth.service';

@Component({
  selector: 'app-admin-main-layout',
  imports: [RouterLink],
  templateUrl: './admin-main-layout.component.html',
  styleUrl: './admin-main-layout.component.css'
})
export class AdminMainLayoutComponent implements OnInit{

  userProfile : UserProfile | null = null;

  constructor(private profileService : ProfileService, private router : Router, private authService : AuthService){}

  ngOnInit(): void {
    this.profileService.getProfile().subscribe({
      next : (data) => (this.userProfile = data),
      error : (err) => console.error('Failed to load user profile', err)
    })
  }

  logout() : void {
    this.authService.logout();
  }

}

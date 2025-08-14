import { Component, OnInit } from '@angular/core';
import { UserProfile } from '../../model/userProfile.model';
import { ProfileService } from '../../core/profile.service';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/auth.service';

@Component({
  selector: 'app-user-main-component',
  imports: [RouterLink],
  templateUrl: './user-main-component.component.html',
  styleUrl: './user-main-component.component.css'
})
export class UserMainComponentComponent implements OnInit{

   userProfile : UserProfile | null = null;

  constructor(
    private profileSrvice : ProfileService,
    private router : Router,
    private authService : AuthService
  ){}

  ngOnInit(): void {
    this.profileSrvice.getProfile().subscribe({
      next : (data) => (this.userProfile =data),
      error : (err) => (console.error('Failed to load user profile', err))
    })
  }

  logout() : void{
    this.authService.logout();
  }
}

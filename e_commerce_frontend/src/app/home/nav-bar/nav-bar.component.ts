import { Component, OnInit } from '@angular/core';
import { ProfileService } from '../../core/profile.service';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/auth.service';
import { UserProfile } from '../../model/userProfile.model';

@Component({
  selector: 'app-nav-bar',
  imports: [RouterLink],
  templateUrl: './nav-bar.component.html',
  styleUrl: './nav-bar.component.css'
})
export class NavBarComponent implements OnInit{

  userProfile :  UserProfile | null = null;

  constructor(
    private profileService :  ProfileService,
    private router :  Router,
    private authService :  AuthService
  ){}
  

  ngOnInit(): void {
    this.profileService.getProfile().subscribe({
     next:(data) =>(this.userProfile = data),
     error:(err) => console.error('Failed to load user profile')
    })
}

logout(): void{
    this.authService.logout();
  }

  // getProfile(): void{
  //   this.router.navigate(['/profile']);
  // }
}
import { Component, OnInit } from '@angular/core';
import { UserProfile } from '../model/userProfile.model';
import { AuthService } from '../core/auth.service';
import { ProfileService } from '../core/profile.service';
import { ToastrService } from 'ngx-toastr';
import { NotExpr } from '@angular/compiler';
import { CommonModule, NgIf } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-profile',
  imports: [NgIf, CommonModule, ReactiveFormsModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit{

  editProfile: FormGroup
  userProfile : UserProfile | null = null;
  isLoading = false;
  selectedFile : File | null = null;
  previewUrl : string | ArrayBuffer | null = null;

  constructor(
    private fb : FormBuilder,
    private authService : AuthService,
    private profileService : ProfileService,
    private toastr : ToastrService
  ){
    this.editProfile = this.fb.group({
       firstName: [''],
      lastName: [''],
      phoneNumber: [''],
      birthDate: [''],
      gender: [''],
      doorNo: [''],
      street: [''],
      city: [''],
      district: [''],
      state: ['']
    });
  }


  ngOnInit(): void {
    this.loadProfile();
  }


  loadProfile(): void{
    this.isLoading = true;
    this.profileService.getProfile().subscribe({
      next: (profile) => {
        this.userProfile =profile;
        this.previewUrl = profile.profileImageUrl || './assets/images/default-profile.png';
        this.editProfile.patchValue({
           firstName: profile.firstName || '',
          lastName: profile.lastName || '',
          phoneNumber: profile.phoneNumber || '',
          birthDate: profile.birthDate || '',
          gender: profile.gender || ''
          // doorNo: profile.doorNo || '',
          // street: profile.street || '',
          // city: profile.city || '',
          // district: profile.district || '',
          // state: profile.state || ''
        })
      },
      error: (err) => {
        this.isLoading = false;
        this.toastr.error('Failed to load profile');
      },
      complete: () => {
        this.isLoading = false;
      }
    });
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;

    if (input.files && input.files.length > 0) {
      const file = input.files[0];

      // File type check
      if (!file.type.match('image.*')) {
        this.toastr.error('Only image files are allowed');
        return;
      }

      // File size check (max 5MB)
      if (file.size > 5 * 1024 * 1024) {
        this.toastr.error('File size exceeds 5MB limit');
        return;
      }

      this.selectedFile = file;

      // Preview image
      const reader = new FileReader();
      reader.onload = () => {
        this.previewUrl = reader.result;
      };
      reader.readAsDataURL(file);
    }
  }

  uploadProfilePhoto(): void {
    if (!this.selectedFile || !this.userProfile) return;

    this.isLoading = true;
    this.profileService.uploadProfilePhoto(this.selectedFile).subscribe({
      next: (response: any) => {
        if (this.userProfile) {
          this.userProfile.profileImageUrl = response.imageUrl;
        }
        this.toastr.success('Profile photo updated successfully');
        this.selectedFile = null;
        this.isLoading = false;
      },
      error: () => {
        this.toastr.error('Failed to upload profile photo');
        this.isLoading = false;
      }
    });
  }
  onSubmit(): void {
    if (this.editProfile.invalid || !this.userProfile) {
      this.toastr.warning('Please fill all required fields');
      return;
    }

    const updatedData = this.editProfile.value;

    this.profileService.editProfile(updatedData).subscribe({
      next: () => {
        this.toastr.success('Profile updated successfully');
      },
      error: () => {
        this.toastr.error('Failed to update profile');
      }
    });
  }

  logout(): void{
    this.authService.logout();
  }
}


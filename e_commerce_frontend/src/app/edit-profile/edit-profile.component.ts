// import { Component, OnInit } from '@angular/core';
// import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
// import { ProfileService } from '../core/profile.service';
// import { ActivatedRoute, Router } from '@angular/router';
// import { ToastrService } from 'ngx-toastr';
// import { CommonModule } from '@angular/common';

// @Component({
//   selector: 'app-edit-profile',
//   imports: [CommonModule, ReactiveFormsModule],
//   templateUrl: './edit-profile.component.html',
//   styleUrl: './edit-profile.component.css'
// })
// export class EditProfileComponent implements OnInit{

//   editProfile : FormGroup;
//   isLoading = false;

//   constructor(
//     private fb : FormBuilder,
//     private profileService : ProfileService,
//     private router : Router,
//     private toastr : ToastrService,
//     private route : ActivatedRoute
//   ){
//     this.editProfile = this.fb.group({
//       firstName: [''],
//       lastName: [''],
//       phoneNumber: [''],
//       birthDate: [''],
//       gender: [''],
//       profileImageUrl: ['']
//     });
//   }

//   loadProfileDate(): void{
//     this.isLoading = true;
//     this.profileService.getProfile().subscribe({
//       next: (profile) => {
//         this.editProfile.patchValue({
//               firstName: profile.firstName,
//               lastName: profile.lastName,
//               phoneNumber: profile.phoneNumber,
//               birthDate: profile.birthDate,
//               gender: profile.gender,
//               profileImageUrl: profile.profileImageUrl
           
//         });
//       },
//       error: (err) => {
//         this.toastr.error('Failed to load profile');
//         this.isLoading = false;
//       }
//     })
//   }

//   ngOnInit(): void {
//     this.loadProfileDate();
//   }


//   onSubmit():void {
//     if(this.editProfile.invalid){
//       return;
//     }

//     this.isLoading = true;
//     this.profileService.updateProfile(this.editProfile.value).subscribe({
//       next: () => {
//         this.toastr.success('Profile update successfully');
//         this.router.navigate(['/profile']);
//       },
//       error: (err) => {
//         this.toastr.error('Failed to update profile');
//         this.isLoading =false;
//       },
//       complete: () => {
//         this.isLoading = false;
//       }
//     });
//   }
// }

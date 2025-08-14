import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../core/auth.service';
import { Router, RouterLink } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { CommonModule } from '@angular/common';
import { Register } from '../model/register.model';

@Component({
  selector: 'app-register',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {

  registerForm: FormGroup;
  isLoading = false;
  showAddressFields = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private toastr: ToastrService
  ){
      this.registerForm = this.fb.group({
        firstName: ['', Validators.required],
        lastName: ['', Validators.required],
        email: ['', [Validators.required, Validators.email]],
        password: ['', [Validators.required, Validators.minLength(8)]],
        phoneNumber: [''],
        birthDate: [''],
        gender: [''],
        doorNo: [''],
        street: [''],
        city: [''],
        district: [''],
        state: [''],
        role: 'ROLE_USER'
      });
  }

  toggleAddressFields(): void{
    this.showAddressFields = !this.showAddressFields;
  }

  onSubmit(): void{
    if(this.registerForm.invalid){
      return;
    }

    this.isLoading = true;
    const registerData: Register = this.registerForm.value;

    this.authService.register(registerData).subscribe({
      next: () => {
        this.toastr.success('Registration successful. Please login.');
        this.router.navigate(['/login']);
      },
      error: (err) => {
        this.isLoading = false;
        this.toastr.error(err.error?.message || 'Registration failed');
      },
      complete: () =>{
        this.isLoading = false;
      }
    })
  }
}

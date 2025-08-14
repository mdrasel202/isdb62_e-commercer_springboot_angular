import { Component } from '@angular/core';
import { AuthService } from '../core/auth.service';
import { Route, Router, RouterLink } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Login } from '../model/login.model';

@Component({
  selector: 'app-login',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  loginForm : FormGroup;
  isLoading = false;

  constructor(
    private fb: FormBuilder,
    private authService : AuthService,
    private router : Router,
    private toastr : ToastrService
  ){
    this.loginForm  = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  onSubmit():void{
    if(this.loginForm.invalid){
      return;
    }

    this.isLoading = true;
    const loginData : Login = this.loginForm.value;

    this.authService.login(loginData).subscribe({
      next: (user) => {
        const roles = user.roles || [];
        if(roles.includes('ROLE_ADMIN')){
          this.router.navigate(['/admin']);
        }else if(roles.includes('ROLE_USER')){
             this.router.navigate(['/product-details']);
        }
        this.toastr.success('Loging successful');
      },
      error: (err) => {
        this.isLoading = false;
        this.toastr.error(err.error?.message || 'Login failed');
      },
      complete: () => {
        this.isLoading = false;
      }
    })
  }
}

import { Component } from '@angular/core';
import { PasswordChange } from '../../model/password_change';
import { UserPasswordChangeService } from '../../service/user-password-change.service';
import { ToastrService } from 'ngx-toastr';
import { FormsModule, NgModel } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-user-password-change',
  imports: [CommonModule, FormsModule],
  templateUrl: './user-password-change.component.html',
  styleUrl: './user-password-change.component.css'
})
export class UserPasswordChangeComponent {

  password : PasswordChange ={
    oldPassword : '',
    newPassword : '',
    confirmPassword : ''
  };

  constructor(
    private userPassword : UserPasswordChangeService,
    private toastr : ToastrService
  ){}

  onSubmit(): void{
    if(this.password.newPassword !== this.password.confirmPassword){
      this.toastr.error('new password and confirm password not matching');
      return;
    }

    this.userPassword.passwordChanged(this.password).subscribe({
      next : (any) => {
        this.toastr.success('Password change successful');
        this.password = {
          oldPassword : '',
          newPassword : '',
          confirmPassword : ''
        };
      },
      error : (err) => {
        this.toastr.error(err.error || 'Password not change');
      }
    })
  }


}

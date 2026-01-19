import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-forgotpassword',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './forgotpassword.component.html',
  styleUrls: ['./forgotpassword.component.css']
})
export class ForgotPasswordComponent {
  email = '';
  otp = '';
  newPassword = '';

  confirmPassword: string = '';

  message = '';
  otpSent = false;
  otpVerified = false;

  constructor(
    private http: HttpClient,
    private userService: UserService,
    private router: Router
  ) {}

  sendOtp() {
    if (!this.email.trim()) {
      this.message = 'Please enter your email.';
      return;
    }
    this.userService.sendOtp(this.email).subscribe({
      next: () => {
        this.message = '✅ OTP sent to your email.';
        this.otpSent = true;
      },
      error: () => {
        this.message = '❌ Failed to send OTP.';
      }
    });
  }

  verifyOtp() {
    if (!this.otp.trim()) {
      this.message = 'Please enter the OTP.';
      return;
    }
    this.userService.verifyOtp(this.email, this.otp).subscribe({
      next: () => {
        this.message = '✅ OTP verified. Please set your new password.';
        this.otpVerified = true;
      },
      error: () => {
        this.message = '❌ Invalid OTP.';
      }
    });
  }

  resetPassword() {
    if (!this.newPassword.trim()) {
      this.message = 'Please enter a new password.';
      return;
    }
    this.userService.resetPasswordWithOtp(this.email, this.otp, this.newPassword).subscribe({
      next: (res: any) => {
        this.message = res.message || 'Password reset successful. Redirecting to login...';
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 3000);
      },
      error: err => {
        this.message = err.error?.message || 'Failed to reset password.';
      }
    });
  }
}

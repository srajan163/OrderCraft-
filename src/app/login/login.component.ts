import { Component } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { UserService } from '../services/user.service';
import { InactivityService } from '../services/inactivity.service';
import { NgxCaptchaModule } from 'ngx-captcha';
 
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, HttpClientModule, RouterModule, NgxCaptchaModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  submitted = false;
  loginError = '';
  siteKey: string = '6LfGMoMrAAAAAM9GO7Q1eZ_tuWQmtojPClNmlBJc'; // 🔐 Your site key
  isBrowser = false;
 
  loginForm = this.fb.group({
    username: ['', Validators.required],
    password: ['', Validators.required],
    recaptcha: ['', Validators.required] // reCAPTCHA token
  });
 
 
  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private router: Router,
    private inactivityService: InactivityService
  ) {
 
    if (typeof window !== 'undefined') {
      this.isBrowser = true;
    }
  }
 
  onSubmit() {
 
    console.log("Login button clicked!");
    this.submitted = true;
 
    if (this.loginForm.invalid) {
      console.log("Form is invalid", this.loginForm.value);
      return;
    }
 
    const { username, password } = this.loginForm.value;
 
    console.log("Sending login request with:", username);
 
    this.userService.login(username!, password!).subscribe({
      next: (res) => {
        console.log("Login successful", res);
 
        // ✅ Store token, role, and userId in local storage
        localStorage.setItem('token', res.token);
        localStorage.setItem('role', res.role);
        if (res.userId) {
          localStorage.setItem('userId', res.userId.toString());
        }
 
        // Clear any previous error
        this.loginError = '';
 
        // If user must reset password
        if (res.mustReset === true) {
          console.log("User must reset password, redirecting...");
          this.router.navigate(['/reset-password']);
          return;
        }
 
        this.inactivityService.startMonitoring();
 
        // Role-based navigation
        switch (res.role) {
          case 'ADMIN':
            this.router.navigate(['/admin']);
            break;
          case 'PROCUREMENT_OFFICER':
            this.router.navigate(['/procurement']);
            break;
          case 'PRODUCTION_MANAGER':
            this.router.navigate(['/production']);
            break;
          case 'INVENTORY_MANAGER':
            this.router.navigate(['/inventory']);
            break;
          default:
            // ✅ fallback to profile instead of login loop
            this.router.navigate(['/profile']);
            break;
        }
      },
      error: (err) => {
        console.log("Login failed", err);
 
        // Default error message
        this.loginError = 'Login failed. Please try again.';
 
        // If backend provided structured error details
        if (err.status === 401 || err.status === 403) {
          const errorBody = err.error;
          console.log("Error body:", errorBody);
 
          if (errorBody.error) {
            this.loginError = errorBody.error;
          }
 
          if (errorBody.accountLocked) {
            this.loginError =
              'Your account has been locked due to multiple failed login attempts. Please contact support.';
          }
        }
 
        // Optionally reset captcha field
        this.loginForm.get('recaptcha')?.reset();
      }
    });
  }
 
  // 👇 Navigation method for Forgot Password
  navigateToForgotPassword() {
    this.router.navigate(['/forgot-password']);
  }
 
 
}
 
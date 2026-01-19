// import { Component, Inject, PLATFORM_ID } from '@angular/core';
// import { CommonModule, isPlatformBrowser } from '@angular/common';
// import { FormsModule } from '@angular/forms';
// import { Router } from '@angular/router';
// import { UserService } from '../../services/user.service';
// import { HeaderComponent } from '../header/header.component';
// import { NgxCaptchaModule } from 'ngx-captcha';
// import { InactivityService } from '../../services/inactivity.service';

// @Component({
//   selector: 'app-login-page',
//   standalone: true,
//   imports: [CommonModule, FormsModule, HeaderComponent, NgxCaptchaModule],
//   templateUrl: './login-page.component.html',
//   styleUrls: ['./login-page.component.css']
// })
// export class LoginPageComponent {
//   header = HeaderComponent.prototype;
//   siteKey: string = '6LfGMoMrAAAAAM9GO7Q1eZ_tuWQmtojPClNmlBJc'; 

//   formData = {
//     email: '',
//     password: '',
//     recaptcha: '',
//     rememberMe: false,
//   };

//   captchaToken: string | null = null;
//   captchaValid = false;
//   captchaError = '';
//   showPassword = false;
//   isLoading = false;
//   error = '';
//   isBrowser: boolean;

//   constructor(
//     private userService: UserService,
//     private router: Router,
//     private inactivityService: InactivityService,
//     @Inject(PLATFORM_ID) private platformId: Object
//   ) {
//     this.isBrowser = isPlatformBrowser(this.platformId);
//   }

//   get isFormValid(): boolean {
//     return (
//       this.formData.email.trim() !== '' &&
//       this.formData.password.trim() !== '' &&
//       this.captchaValid
//     );
//   }

//   handleCaptchaSuccess(token: string) {
//     this.captchaToken = token;
//     this.formData.recaptcha = token;
//     this.captchaValid = !!token;
//     this.captchaError = token ? '' : 'Captcha verification failed. Please try again.';
//   }

//   toggleShowPassword() {
//     this.showPassword = !this.showPassword;
//   }

//    handleInputChange() {
//     if (this.error) this.error = '';
//     if (this.captchaError) this.captchaError = '';
//   }

//   handleSubmit(event: Event) {
//     event.preventDefault();
//     if (!this.isFormValid) return;

//     this.isLoading = true;
//     this.error = '';

//     this.userService.login(this.formData.email, this.formData.password).subscribe({
//       next: (response) => {
//         // ✅ Save everything required
//         localStorage.setItem('isAuthenticated', 'true');
//         localStorage.setItem('userRole', response.role);
//         localStorage.setItem('token', response.token);   // 👈 FIX: store token
//         localStorage.setItem('userId', response.userId);

//         if (response.mustReset === true) {
//           this.router.navigate(['/reset-password']);
//           return;
//         }

//         this.inactivityService.startMonitoring();

//         const normalizedRole = response.role.toUpperCase();
//         const roleRouteMap: { [key: string]: string } = {
//           'ADMIN': '/admin',
//           'PROCUREMENT_OFFICER': '/procurement',
//           'INVENTORY_MANAGER': '/inventory',
//           'PRODUCTION_MANAGER': '/production'
//         };

//         const redirectPath = roleRouteMap[normalizedRole] || '/';
//         this.router.navigate([redirectPath]);
//       },
//       error: () => {
//         this.error = 'Invalid credentials. Please try again.';
//         this.isLoading = false;
//       },
//       complete: () => {
//         this.isLoading = false;
//       }
//     });
//   }

//   ngOnDestroy() {
//     if (!this.isBrowser) return;
//   }
// }
import { Component, Inject, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { HeaderComponent } from '../header/header.component';
import { NgxCaptchaModule } from 'ngx-captcha';
import { InactivityService } from '../../services/inactivity.service';
import { FooterComponent } from '../footer/footer.component';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [CommonModule, FormsModule, HeaderComponent, NgxCaptchaModule, FooterComponent],
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css']
})
export class LoginPageComponent {
  header = HeaderComponent.prototype;
  siteKey: string = '6LfGMoMrAAAAAM9GO7Q1eZ_tuWQmtojPClNmlBJc'; 

  formData = {
    email: '',
    password: '',
    recaptcha: '',
    rememberMe: false,
  };

  captchaToken: string | null = null;
  captchaValid = false;
  captchaError = '';
  showPassword = false;
  isLoading = false;
  error = '';
  isBrowser: boolean;

  constructor(
    private userService: UserService,
    private router: Router,
    private inactivityService: InactivityService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    this.isBrowser = isPlatformBrowser(this.platformId);
  }

  get isFormValid(): boolean {
    return (
      this.formData.email.trim() !== '' &&
      this.formData.password.trim() !== '' &&
      this.captchaValid
    );
  }

  handleCaptchaSuccess(token: string) {
    this.captchaToken = token;
    this.formData.recaptcha = token;
    this.captchaValid = !!token;
    this.captchaError = token ? '' : 'Captcha verification failed. Please try again.';
  }

  toggleShowPassword() {
    this.showPassword = !this.showPassword;
  }

  handleInputChange() {
    if (this.error) this.error = '';
    if (this.captchaError) this.captchaError = '';
  }

  handleSubmit(event: Event) {
    event.preventDefault();
    if (!this.isFormValid) return;

    this.isLoading = true;
    this.error = '';

    this.userService.login(this.formData.email, this.formData.password).subscribe({
      next: (response) => {
        // ✅ Save everything required
        localStorage.setItem('isAuthenticated', 'true');
        localStorage.setItem('userRole', response.role);
        localStorage.setItem('token', response.token);
        localStorage.setItem('userId', response.userId);

        if (response.mustReset === true) {
          this.router.navigate(['/reset-password']);
          return;
        }

        this.inactivityService.startMonitoring();

        const normalizedRole = response.role.toUpperCase();
        const roleRouteMap: { [key: string]: string } = {
          'ADMIN': '/admin',
          'PROCUREMENT_OFFICER': '/procurement',
          'INVENTORY_MANAGER': '/inventory',
          'PRODUCTION_MANAGER': '/production'
        };

        const redirectPath = roleRouteMap[normalizedRole] || '/profile';
        this.router.navigate([redirectPath]);
      },
      error: (err) => {
        console.error('Login failed', err);

        // Default error
        this.error = 'Login failed. Please try again.';

        // Handle backend structured errors
        if (err.status === 401 || err.status === 403) {
          const errorBody = err.error;
          if (errorBody?.error) {
            this.error = errorBody.error;
          }
          if (errorBody?.accountLocked) {
            this.error =
              'Your account has been locked due to multiple failed login attempts. Please contact support.';
          }
        }

        // Reset captcha
        this.captchaValid = false;
        this.formData.recaptcha = '';
        this.isLoading = false;
      },
      complete: () => {
        this.isLoading = false;
      }
    });
  }

  ngOnDestroy() {
    if (!this.isBrowser) return;
  }
}

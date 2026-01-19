
// import { Component, OnInit, inject } from '@angular/core';
// import { CommonModule } from '@angular/common';
// import {
//   FormBuilder,
//   FormGroup,
//   FormControl,
//   Validators,
//   ReactiveFormsModule
// } from '@angular/forms';
// import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
// import { StepperSelectionEvent } from '@angular/cdk/stepper';
// import { MatStepperModule } from '@angular/material/stepper';
// import { MatFormFieldModule } from '@angular/material/form-field';
// import { MatInputModule } from '@angular/material/input';
// import { MatSelectModule } from '@angular/material/select';
// import { MatButtonModule } from '@angular/material/button';
// import { MatIconModule } from '@angular/material/icon';
 
// import { Router } from '@angular/router';
// import { UserService, User } from '../../services/user.service';
// import { HttpErrorResponse } from '@angular/common/http';
 
// @Component({
//   selector: 'app-register',
//   standalone: true,
//   imports: [
//     CommonModule,
//     ReactiveFormsModule,
//     MatStepperModule,
//     MatFormFieldModule,
//     MatInputModule,
//     MatSelectModule,
//     MatButtonModule,
//     MatIconModule
//   ],
//   templateUrl: './register.component.html',
//   styleUrls: ['./register.component.css']
// })
// export class RegisterComponent implements OnInit {
//   private fb = inject(FormBuilder);
//   private breakpointObserver = inject(BreakpointObserver);
//   private userService = inject(UserService);
//   private router = inject(Router);
 
//   registerForm!: FormGroup;
//   stepperOrientation: 'horizontal' | 'vertical' = 'horizontal';
//   roles: any[] = [];
 
//   ngOnInit(): void {
//     // Form structure: nested groups + a top-level FormControl for role
//     this.registerForm = this.fb.group({
//       basicInfo: this.fb.group({
//         userFullName: ['', Validators.required],
//         userName: ['', [Validators.required, Validators.minLength(3)]],
//         userEmail: ['', [Validators.required, Validators.email]],
//         userMobile: ['', [Validators.required, Validators.pattern('^[0-9]{10}$')]],
//         userProfileImg: ['']
//       }),
//       // role stored as a FormControl which will hold the entire role object
//       role: new FormControl(null, Validators.required),
//       address: this.fb.group({
//         addressStreet: [''],
//         addressCity: [''],
//         addressState: [''],
//         addressPostalCode: ['', [Validators.pattern(/^[0-9]{6}$/)]],
//         addressCountry: ['']
//       })
//     });
 
//     // load roles from backend
//     this.userService.getRoles().subscribe({
//       next: (r) => {
//         this.roles = r || [];
//         console.debug('Roles loaded', this.roles);
//       },
//       error: (err) => {
//         console.error('Failed to load roles', err);
//       }
//     });
 
//     // responsive stepper orientation
//     this.breakpointObserver.observe([Breakpoints.Handset]).subscribe(result => {
//       this.stepperOrientation = result.matches ? 'vertical' : 'horizontal';
//     });
//   }
 
//   // getters for template safety (avoid registerForm.get(...) null typing in template)
//   get basicInfoForm(): FormGroup {
//     return this.registerForm.get('basicInfo') as FormGroup;
//   }
//   get roleControl(): FormControl {
//     return this.registerForm.get('role') as FormControl;
//   }
//   get addressForm(): FormGroup {
//     return this.registerForm.get('address') as FormGroup;
//   }
 
//   onStepChange(event: StepperSelectionEvent): void {
//     // optional debug
//     console.debug('step changed ->', event.selectedIndex);
//   }
 
//   onSubmit(): void {
//     if (this.registerForm.invalid) {
//       this.registerForm.markAllAsTouched();
//       return;
//     }
 
//     const fv = this.registerForm.value;
 
//     // Build payload that matches your User model
//     const payload: User = {
//       userId: '', // let backend generate
//       userName: fv.basicInfo.userName,
//       userFullName: fv.basicInfo.userFullName,
//       userEmail: fv.basicInfo.userEmail,
//       userMobile: fv.basicInfo.userMobile,
//       userProfileImg: fv.basicInfo.userProfileImg || '',
//       role: fv.role, // full object { roleId, roleName } coming from select
//       address: {
//         addressId: 0,
//         addressStreet: fv.address.addressStreet || '',
//         addressCity: fv.address.addressCity || '',
//         addressState: fv.address.addressState || '',
//         addressPostalCode: fv.address.addressPostalCode || '',
//         addressCountry: fv.address.addressCountry || ''
//       }
//     };
 
//     console.log('Submitting payload to registerUser:', payload);
 
//     this.userService.registerUser(payload).subscribe({
//       next: (res) => {
//         console.log('register success', res);
//         alert('User registered successfully!');
//         // reset or navigate
//         this.registerForm.reset();
//         this.router.navigate(['/admin']).catch(() => {});
//       },
//       error: (err: HttpErrorResponse) => {
//         // detailed reporting for debugging
//         console.error('Error registering user:', err);
//         if (err.status === 0) {
//           // Network / CORS / server unreachable
//           alert('Network error or CORS issue. Check backend is running and CORS enabled. See console/network tab.');
//         } else if (err.error && typeof err.error === 'object') {
//           // backend returned JSON error body
//           alert('Server error: ' + (err.error.message || JSON.stringify(err.error)));
//         } else {
//           alert('Server error: ' + (err.message || 'Unknown error'));
//         }
//       }
//     });
//   }
// }
 
 
import { Component, OnInit, inject } from '@angular/core';

import { CommonModule } from '@angular/common';

import {

  FormBuilder,

  FormGroup,

  FormControl,

  Validators,

  ReactiveFormsModule,

} from '@angular/forms';

import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';

import { StepperSelectionEvent } from '@angular/cdk/stepper';

import { MatStepperModule } from '@angular/material/stepper';

import { MatFormFieldModule } from '@angular/material/form-field';

import { MatInputModule } from '@angular/material/input';

import { MatSelectModule } from '@angular/material/select';

import { MatButtonModule } from '@angular/material/button';

import { MatIconModule } from '@angular/material/icon';

import { Router } from '@angular/router';

import { UserService, User } from '../../services/user.service';

import { HttpErrorResponse } from '@angular/common/http';
 
@Component({

  selector: 'app-register',

  standalone: true,

  imports: [

    CommonModule,

    ReactiveFormsModule,

    MatStepperModule,

    MatFormFieldModule,

    MatInputModule,

    MatSelectModule,

    MatButtonModule,

    MatIconModule,

  ],

  templateUrl: './register.component.html',

  styleUrls: ['./register.component.css'],

})

export class RegisterComponent implements OnInit {

  private fb = inject(FormBuilder);

  private breakpointObserver = inject(BreakpointObserver);

  private userService = inject(UserService);

  private router = inject(Router);
 
  registerForm!: FormGroup;

  stepperOrientation: 'horizontal' | 'vertical' = 'horizontal';

  roles: any[] = [];

  showValidationSummary = true; // toggle for dismissible box
 
  ngOnInit(): void {

    this.registerForm = this.fb.group({

      basicInfo: this.fb.group({

        userFullName: [

          '',

          [

            Validators.required,

            Validators.pattern(/^[A-Za-z][A-Za-z\s]*$/),

          ],

        ],

        userName: [

          '',

          [

            Validators.required,

            Validators.minLength(3),

            Validators.pattern(/^[A-Za-z][A-Za-z0-9@._\-#]*$/), // no spaces allowed

          ],

        ],

        userEmail: ['', [Validators.required, Validators.email]],

        userMobile: [

          '',

          [Validators.required, Validators.pattern('^[0-9]{10}$')],

        ],

        userProfileImg: [''],

      }),

      role: new FormControl(null, Validators.required),

      address: this.fb.group({

        addressStreet: [''],

        addressCity: [''],

        addressState: [''],

        addressPostalCode: ['', [Validators.pattern(/^[0-9]{6}$/)]],

        addressCountry: [''],

      }),

    });
 
    this.userService.getRoles().subscribe({

      next: (r) => (this.roles = r || []),

      error: (err) => console.error('Failed to load roles', err),

    });
 
    this.breakpointObserver

      .observe([Breakpoints.Handset])

      .subscribe((result) => {

        this.stepperOrientation = result.matches ? 'vertical' : 'horizontal';

      });

  }
 
  get basicInfoForm(): FormGroup {

    return this.registerForm.get('basicInfo') as FormGroup;

  }

  get roleControl(): FormControl {

    return this.registerForm.get('role') as FormControl;

  }

  get addressForm(): FormGroup {

    return this.registerForm.get('address') as FormGroup;

  }
 
  getAllErrors(): string[] {

    const errors: string[] = [];

    const basicInfo = this.basicInfoForm.controls;
 
    if (basicInfo['userFullName'].errors) {

      if (basicInfo['userFullName'].errors['required'])

        errors.push('Full name is required.');

      if (basicInfo['userFullName'].errors['pattern'])

        errors.push(

          'Full name must start with a letter and contain only letters/spaces.'

        );

    }
 
    if (basicInfo['userName'].errors) {

      if (basicInfo['userName'].errors['required'])

        errors.push('Username is required.');

      if (basicInfo['userName'].errors['minlength'])

        errors.push('Username must be at least 3 characters long.');

      if (basicInfo['userName'].errors['pattern'])

        errors.push(

          'Username must start with a letter and may contain letters, numbers, or special characters (@ . _ - #). Spaces are not allowed.'

        );

    }
 
    if (basicInfo['userEmail'].errors) {

      errors.push('Please enter a valid email address.');

    }
 
    if (basicInfo['userMobile'].errors) {

      errors.push('Mobile number must be exactly 10 digits.');

    }
 
    if (this.roleControl.errors) {

      errors.push('Please select a role.');

    }
 
    const address = this.addressForm.controls;

    if (address['addressPostalCode'].errors) {

      errors.push('Postal code must be 6 digits.');

    }
 
    return errors;

  }
 
  dismissSummary(): void {

    this.showValidationSummary = false;

  }
 
  onStepChange(event: StepperSelectionEvent): void {

    this.showValidationSummary = true; // re-show summary on step change

  }
 
  onSubmit(): void {

    if (this.registerForm.invalid) {

      this.registerForm.markAllAsTouched();

      this.showValidationSummary = true;

      return;

    }
 
    const fv = this.registerForm.value;

    const payload: User = {

      userId: '',

      userName: fv.basicInfo.userName,

      userFullName: fv.basicInfo.userFullName,

      userEmail: fv.basicInfo.userEmail,

      userMobile: fv.basicInfo.userMobile,

      userProfileImg: fv.basicInfo.userProfileImg || '',

      role: fv.role,

      address: {

        addressId: 0,

        addressStreet: fv.address.addressStreet || '',

        addressCity: fv.address.addressCity || '',

        addressState: fv.address.addressState || '',

        addressPostalCode: fv.address.addressPostalCode || '',

        addressCountry: fv.address.addressCountry || '',

      },

    };
 
    this.userService.registerUser(payload).subscribe({

      next: () => {

        alert('User registered successfully!');

        this.registerForm.reset();

        this.router.navigate(['/admin']).catch(() => {});

      },

      error: (err: HttpErrorResponse) => {

        console.error('Error registering user:', err);

        if (err.status === 0) {

          alert(

            'Network error or CORS issue. Check backend is running and CORS enabled.'

          );

        } else {

          alert(

            'Server error: ' +

              (err.error?.message || err.message || 'Unknown error')

          );

        }

      },

    });

  }

}


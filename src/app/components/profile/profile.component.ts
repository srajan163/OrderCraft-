import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { MatCardModule } from '@angular/material/card';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatListModule } from '@angular/material/list';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDividerModule } from '@angular/material/divider';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { UserService, User } from '../../services/user.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatToolbarModule,
    MatButtonModule,
    MatListModule,
    MatFormFieldModule,
    MatInputModule,
    MatDividerModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css'] // <-- corrected
})
export class ProfileComponent implements OnInit {
  private fb = inject(FormBuilder);
  private userService = inject(UserService);
  private router = inject(Router); // <-- injected Router

  user = signal<User | null>(null);
  loading = signal(true);
  isEditing = signal(false);
  errorMessage = signal('');
  previewImage = signal<string | ArrayBuffer | null>(null);

  profileForm = this.fb.group({
    userFullName: [''],
    userEmail: [''],
    userMobile: [''],
    address: this.fb.group({
      addressStreet: [''],
      addressCity: [''],
      addressState: [''],
      addressPostalCode: [''],
      addressCountry: ['']
    })
  });

  backendUrl = 'http://localhost:8086';
  imageUrl = signal('assets/default-avatar.png');

  ngOnInit(): void {
    this.loadUserProfile();
  }

  loadUserProfile() {
    const userId = this.userService.getUserIdFromToken();
    if (!userId) {
      this.errorMessage.set('User ID not found. Please login again.');
      this.loading.set(false);
      return;
    }

    this.userService.getUserById(userId).subscribe({
      next: (data: any) => {
        // Map flat roleName from backend → role object
        const mappedUser: User = {
          ...data,
          role: {
            roleId: 0,
            roleName: data.roleName
          }
        };

        this.user.set(mappedUser);
        this.imageUrl.set(
          mappedUser.userProfileImg ? this.backendUrl + mappedUser.userProfileImg : 'assets/default-avatar.png'
        );
        this.profileForm.patchValue({
          userFullName: mappedUser.userFullName,
          userEmail: mappedUser.userEmail,
          userMobile: mappedUser.userMobile,
          address: {
            addressStreet: mappedUser.address?.addressStreet,
            addressCity: mappedUser.address?.addressCity,
            addressState: mappedUser.address?.addressState,
            addressPostalCode: mappedUser.address?.addressPostalCode,
            addressCountry: mappedUser.address?.addressCountry
          }
        });

        this.loading.set(false);
      },
      error: () => {
        this.errorMessage.set('Failed to load user profile');
        this.loading.set(false);
      }
    });
  }

  toggleEdit() {
    this.isEditing.update((v) => !v);
    this.errorMessage.set('');
  }

  saveProfile() {
    const currentUser = this.user();
    if (!currentUser) return;

    const updated = { ...currentUser, ...this.profileForm.value };

    this.userService.updateUser(currentUser.userId, updated as User).subscribe({
      next: (data: any) => {
        const mappedUser: User = {
          ...data,
          role: {
            roleId: 0,
            roleName: data.roleName
          }
        };
        this.user.set(mappedUser);
        this.toggleEdit();
      },
      error: () => {
        this.errorMessage.set('Failed to update profile');
      }
    });
  }

  onFileSelected(event: any) {
    const currentUser = this.user();
    if (!currentUser) return;

    const file: File = event.target.files[0];
    if (file) {
      // preview before upload
      const reader = new FileReader();
      reader.onload = () => this.previewImage.set(reader.result);
      reader.readAsDataURL(file);

      this.userService.uploadProfileImage(currentUser.userId, file).subscribe({
        next: (data: any) => {
          const mappedUser: User = {
            ...data,
            role: {
              roleId: 0,
              roleName: data.roleName
            }
          };
          this.user.set(mappedUser);
          this.imageUrl.set(this.backendUrl + mappedUser.userProfileImg);
          this.previewImage.set(null);
        },
        error: () => {
          this.errorMessage.set('Failed to upload image');
        }
      });
    }
  }

  removeImage() {
    const currentUser = this.user();
    if (!currentUser) return;

    this.userService.removeProfileImage(currentUser.userId).subscribe({
      next: (data: any) => {
        const mappedUser: User = {
          ...data,
          role: {
            roleId: 0,
            roleName: data.roleName
          }
        };
        this.user.set(mappedUser);
        this.imageUrl.set('assets/default-avatar.png');
        this.previewImage.set(null);
      },
      error: () => {
        this.errorMessage.set('Failed to remove image');
      }
    });
  }

  logout() {
    this.userService.logout();
  }

  goBack() {
    // navigate back to dashboard (update route as needed)
    this.router.navigate(['/dashboard']);
  }
}

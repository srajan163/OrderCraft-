import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService, User } from '../services/user.service';

@Component({
  selector: 'app-update-user',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './update-user.component.html',
  styleUrls: ['./update-user.component.css'],
})
export class UpdateUserComponent implements OnInit {
  userId!: string;
  user?: User;
  editForm!: FormGroup;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService
  ) {}

  ngOnInit() {
    // Get userId from route param
    this.userId = this.route.snapshot.paramMap.get('id')!;

    // Fetch user by ID
    this.userService.getUserById(this.userId).subscribe({
      next: (userData) => {
        this.user = userData;
        this.initForm();
      },
      error: (err) => {
        console.error('Failed to load user', err);
        alert('Failed to load user details.');
        this.router.navigate(['/admin']); // Redirect back to admin on error
      },
    });
  }

  initForm() {
    this.editForm = new FormGroup({
      userFullName: new FormControl(this.user?.userFullName ?? '', [Validators.required, Validators.minLength(3)]),
      userEmail: new FormControl(this.user?.userEmail ?? '', [Validators.required, Validators.email]),
      userMobile: new FormControl(this.user?.userMobile ?? ''),
      // Role is readonly, so not editable here
    });
  }

  onSubmit() {
    if (!this.editForm.valid || !this.user) return;

    const updatedUser: User = {
      ...this.user,
      userFullName: this.editForm.value.userFullName,
      userEmail: this.editForm.value.userEmail,
      userMobile: this.editForm.value.userMobile,
    };

    this.userService.updateUser(this.userId, updatedUser).subscribe({
      next: () => {
        alert('User updated successfully');
        this.router.navigate(['/admin']); // Navigate back to admin after update
      },
      error: (err) => {
        console.error('Failed to update user', err);
        alert('Error updating user.');
      },
    });
  }

  cancel() {
    this.router.navigate(['/admin']);
  }
}

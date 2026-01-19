import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { FormsModule } from '@angular/forms'; // ✅ for ngModel
 
@Component({
  selector: 'app-unlock-user',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './unlock-user.component.html',
  styleUrl: './unlock-user.component.css'
})
export class UnlockUserComponent implements OnInit {
  lockedUsers: any[] = [];
  filteredUsers: any[] = []; // ✅ separate list for search results
  searchTerm: string = '';
  loading = false;
  error: string = '';
 
  constructor(private http: HttpClient) {}
 
  ngOnInit() {
    this.loadLockedUsers();
  }
 
  private getAuthHeaders() {
    const token = localStorage.getItem('token') || '';
    return new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
  }
 
  loadLockedUsers() {
    this.loading = true;
    this.error = '';
 
    const headers = this.getAuthHeaders();
 
    this.http
      .get<any[]>(`http://localhost:8086/api/users/locked-users`, { headers })
      .subscribe({
        next: (res) => {
          this.lockedUsers = res;
          this.filteredUsers = [...res]; // ✅ initialize filtered list
          this.loading = false;
        },
        error: (err) => {
          console.error(err);
          this.error = 'Failed to load locked users.';
          this.loading = false;
        }
      });
  }
 
  filterUsers() {
    const term = this.searchTerm.toLowerCase();
    this.filteredUsers = this.lockedUsers.filter(
      (user) =>
        user.userEmail.toLowerCase().includes(term) ||
        user.userName.toLowerCase().includes(term)
    );
  }
 
  unlock(email: string) {
    if (!confirm(`Unlock account for ${email}?`)) {
      return;
    }
 
    const headers = this.getAuthHeaders();
 
    this.http
      .put(
        `http://localhost:8086/api/users/unlock-account?email=${encodeURIComponent(
          email
        )}`,
        {},
        { headers }
      )
      .subscribe({
        next: () => {
          alert('Account unlocked successfully!');
          this.loadLockedUsers();
        },
        error: (err) => {
          console.error(err);
          alert('Failed to unlock account.');
        }
      });
  }
}
 
 
 
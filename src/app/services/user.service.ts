
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { Router } from '@angular/router';
import { InactivityService } from './inactivity.service';
 
export interface User {
  userId: string;   // <-- changed to string

  userName: string;
  userFullName: string;
  userEmail: string;
  userMobile: string;
  userProfileImg: string;
  role: {
    roleId: number;
    roleName: string;
  };
  address: {
    addressId: number;
    addressStreet: string;
    addressCity: string;
    addressState: string;
    addressPostalCode: string;
    addressCountry: string;
  };
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private baseUrl = 'http://localhost:8086/api/users';
  private authUrl = 'http://localhost:8086/auth';
 

  constructor(
    private http: HttpClient,
    private inactivityService: InactivityService,
    private router: Router
  ) {}

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token') || '';
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.baseUrl, { headers: this.getAuthHeaders() }).pipe(
      catchError(error => {
        console.error('Error fetching users:', error);
        return throwError(() => error);
      })
    );
  }

 
  getUserById(userId: string): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/${userId}/profile`, { headers: this.getAuthHeaders() }).pipe(
      catchError(error => {
        console.error('Error fetching user profile:', error);
        return throwError(() => error);
      })
    );
  }
 
  registerUser(user: User): Observable<User> {
    return this.http.post<User>(`${this.baseUrl}/register`, user, { headers: this.getAuthHeaders() });
  }
 
  updateUser(userId: string, updatedUser: User): Observable<User> {
    return this.http.put<User>(`${this.baseUrl}/${userId}/profile`, updatedUser, { headers: this.getAuthHeaders() }).pipe(
      catchError(error => {
        console.error('Error updating user profile:', error);
        return throwError(() => error);
      })
    );
  }
 
  deleteUser(userId: string): Observable<string> {
    return this.http.delete(`${this.baseUrl}/${userId}`, { headers: this.getAuthHeaders(), responseType: 'text' });
  }
 
  // ====== Auth ======
  login(username: string, password: string): Observable<{
    token: string;
    role: string;
    sessionExpiresAt: string;
    userId: string;
    username: string;
    message: string;
    mustReset: boolean;
  }> {
    return this.http.post<{
      token: string;
      role: string;
      sessionExpiresAt: string;
      userId: string;
      username: string;
      message: string;
      mustReset: boolean;
    }>(
      `${this.authUrl}/login`,
      { username, password }
    );
  }
 
  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('userId');
 
    this.http.post('/logout', {}, { headers: this.getAuthHeaders() }).subscribe({
      next: () => console.log('Logged out from backend'),
      error: err => console.error('Logout error', err)
    });
 
    this.inactivityService.logout();
    this.router.navigate(['/unlockuser']);
  }
 
  getRoles(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/roles`, { headers: this.getAuthHeaders() });
  }
 
  forgotPassword(email: string): Observable<string> {
    return this.http.post(`${this.baseUrl}/forgot-password`, null, { params: { email }, responseType: 'text' });
  }
 
  resetPassword(currentPassword: string, newPassword: string): Observable<string> {
    return this.http.put(`${this.baseUrl}/reset-password`, { currentPassword, newPassword }, {

      headers: this.getAuthHeaders(),
      responseType: 'text'
    });
  }

 
  sendOtp(email: string): Observable<string> {
    return this.http.post(`${this.baseUrl}/send-otp`, { email }, { headers: this.getAuthHeaders(), responseType: 'text' });
  }
 
  verifyOtp(email: string, otp: string): Observable<string> {
    return this.http.post(`${this.baseUrl}/verify-otp`, { email, otp }, { headers: this.getAuthHeaders(), responseType: 'text' });
  }
 
  resetPasswordWithOtp(email: string, otp: string, newPassword: string): Observable<string> {
    return this.http.post<string>(`${this.baseUrl}/reset-password-with-otp`, { email, otp, newPassword });
  }
 
  checkEmailExists(email: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.baseUrl}/check-email?email=${email}`, { headers: this.getAuthHeaders() });
  }
 
  checkUsernameExists(username: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.baseUrl}/check-username?uname=${username}`, { headers: this.getAuthHeaders() });
  }
 
  checkMobileExists(mobile: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.baseUrl}/check-mobile?mobile=${mobile}`, { headers: this.getAuthHeaders() });
  }
 
  // ====== Extract UserId ======
  getUserIdFromToken(): string | null {
    const token = localStorage.getItem('token');
    if (!token) return localStorage.getItem('userId'); // fallback if login saved userId
 
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload?.userId || localStorage.getItem('userId') || null;
    } catch (e) {
      console.error('Invalid token', e);
      return localStorage.getItem('userId');
    }
  }

    // ✅ Upload profile image
uploadProfileImage(userId: string, file: File): Observable<any> {
  const formData = new FormData();
  formData.append("file", file);
 
  const token = localStorage.getItem("token"); // 👈 adjust based on your login logic
  const headers = new HttpHeaders().set("Authorization", `Bearer ${token}`);
 
  return this.http.post<any>(
    `http://localhost:8086/api/users/${userId}/profile/image`,
    formData,
    { headers }
  );
}
removeProfileImage(userId: string) {
  return this.http.delete<User>(`http://localhost:8086/api/users/${userId}/profile/image`);
}
 
}
 
 


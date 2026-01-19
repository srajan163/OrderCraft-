import { Component, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { ProductionService, ProductionSchedule, ProductionUnit } from '../../services/production.service';

// Angular Material
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatNativeDateModule } from '@angular/material/core';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { Router, RouterOutlet } from '@angular/router';
import { ProductionAnalyticsComponent } from './production-analytics/production-analytics.component';

// ✅ Import the standalone ProductionAnalyticsCompo

@Component({
  selector: 'app-production',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    HttpClientModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatIconModule,
    RouterOutlet,
    MatTableModule,
    MatButtonModule,
    MatTooltipModule,
    MatSnackBarModule,
    // ✅ Add ProductionAnalyticsComponent to imports
    ProductionAnalyticsComponent
  ],
  templateUrl: './production.component.html',
  styleUrls: ['./production.component.css']
})
export class ProductionComponent implements OnInit {
  showProfileMenu = false;
  dropdownOpen = false;
  units: ProductionUnit[] = [];
  schedules: ProductionSchedule[] = [];
  loading = false;
  error: string | null = null;
  showAnalytics = false;

  editingDeadline: { [key: number]: boolean } = {};
  selectedDeadlines: { [key: number]: Date } = {}; // store as Date for mat-datepicker

  constructor(
    private router: Router,
    private productionService: ProductionService,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    this.loadUnits();
    this.refreshSchedules();
  }

  loadUnits(): void {
    this.productionService.getAllUnits().subscribe({
      next: (data) => (this.units = data),
      error: () => (this.error = 'Could not fetch production units.')
    });
  }

  refreshSchedules(): void {
    this.loading = true;
    this.productionService.getAllSchedules().subscribe({
      next: (data) => {
        this.schedules = data;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.error = 'Could not fetch production schedules.';
      }
    });
  }

  @HostListener('document:click', ['$event'])
  onClickOutside(event: Event) {
    const target = event.target as HTMLElement;
    if (!target.closest('.report-container')) {
      this.dropdownOpen = false;
    }
  }

  goToSchedule(): void {
    this.router.navigate(['production-schedule']);
  }

  toggleDropdown(): void {
    this.dropdownOpen = !this.dropdownOpen;
  }

  generateReport(format: string) {
    this.http.get(`/api/reports/production/export?format=${format}`, { responseType: 'blob' })
      .subscribe({
        next: (res) => {
          const blob = new Blob([res], {
            type: format === 'excel'
              ? 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
              : 'application/pdf'
          });
          const url = window.URL.createObjectURL(blob);
          const a = document.createElement('a');
          a.href = url;
          a.download = `production_report.${format === 'excel' ? 'xlsx' : format}`;
          a.click();
          window.URL.revokeObjectURL(url);
          this.dropdownOpen = false;
        },
        error: () => alert('Error generating report')
      });
  }

  enableDeadlineEdit(psId: number): void {
    this.editingDeadline[psId] = true;
    const schedule = this.schedules.find((s) => s.psId === psId);
    this.selectedDeadlines[psId] = schedule?.psDeadline ? new Date(schedule.psDeadline) : new Date();
  }

  saveDeadline(psId: number): void {
    const selected: Date = this.selectedDeadlines[psId];
    if (!selected) {
      alert('Please pick a valid date.');
      return;
    }

    const localDate = new Date(selected.getTime() - selected.getTimezoneOffset() * 60000);
    const year = localDate.getFullYear();
    const month = (localDate.getMonth() + 1).toString().padStart(2, '0');
    const day = localDate.getDate().toString().padStart(2, '0');
    const deadlineStr = `${year}-${month}-${day}`;
    const payload = { psDeadline: deadlineStr };

    this.productionService.updateDeadline(psId, payload).subscribe({
      next: () => {
        const schedule = this.schedules.find(s => s.psId === psId);
        if (schedule) schedule.psDeadline = deadlineStr;
        this.editingDeadline[psId] = false;
        alert('Deadline updated successfully!');
      },
      error: (err) => {
        console.error('❌ Failed to update deadline:', err);
        alert('Failed to update deadline. Make sure the date is valid.');
      }
    });
  }

  navigateToTimeline(): void {
    this.router.navigate(['/production/timeline']);
  }

  cancelDeadlineEdit(psId: number): void {
    this.editingDeadline[psId] = false;
    const schedule = this.schedules.find(s => s.psId === psId);
    this.selectedDeadlines[psId] = schedule?.psDeadline ? new Date(schedule.psDeadline) : new Date();
  }

  markDone(psId: number): void {
    this.productionService.markAsDone(psId).subscribe({ next: () => this.refreshSchedules() });
  }

  cancel(psId: number): void {
    this.productionService.cancelSchedule(psId).subscribe({ next: () => this.refreshSchedules() });
  }

  logout(): void {
    localStorage.removeItem('isAuthenticated');
    localStorage.removeItem('userRole');
    this.router.navigate(['/']);
  }

  /** Open Production Analytics modal */
  openAnalytics(): void {
    this.showAnalytics = true;
  }

  /** Close Production Analytics modal */
  closeAnalytics(): void {
    this.showAnalytics = false;
  }
  viewProfile(): void {
    this.showProfileMenu = false;
    this.router.navigate(['/view-profile']);
  }
   goToProfile(): void {
    this.router.navigate(['/profile']);
  }

  updatePassword(): void {
    this.showProfileMenu = false;
    this.router.navigate(['/reset-password']);
  }


goToEfficiencyReport(): void {
  this.router.navigate(['/production/production-efficiency-report']);
}

}


import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ProductionService, ProductionUnit } from '../../../services/production.service';
 
@Component({
  selector: 'app-production-schedule',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './production-schedule.component.html',
  styleUrls: ['./production-schedule.component.css']
})
export class ProductionScheduleComponent implements OnInit {
  /** List of available production units */
  units: ProductionUnit[] = [];
 
  /** Model for form data */
  schedule = {
    productId: 0,
    unitId: 0,
    psQuantity: 0,
    psStartDate: '',
    psEndDate: ''
  };
 
  /** Loading state */
  loading = false;
 
  constructor(
    private router: Router,
    private productionService: ProductionService
  ) {}
 
  /** Fetch available production units on init */
  ngOnInit(): void {
    this.loadUnits();
  }
 
  /** ✅ Load all production units */
  loadUnits(): void {
    this.productionService.getAllUnits().subscribe({
      next: (data) => {
        this.units = data;
        console.log('✅ Units loaded:', this.units);
      },
      error: (err) => {
        console.error('❌ Failed to load units:', err);
        alert('Failed to load production units.');
      }
    });
  }
 
  /** ✅ Create a new production schedule */
  createSchedule(): void {
    if (
      !this.schedule.productId ||
      !this.schedule.unitId ||
      !this.schedule.psQuantity ||
      !this.schedule.psStartDate ||
      !this.schedule.psEndDate
    ) {
      alert('Please fill in all required fields.');
      return;
    }
 
    const payload = {
      product: { productsId: this.schedule.productId },
      productionUnit: { unitId: this.schedule.unitId },
      psQuantity: this.schedule.psQuantity,
      psStartDate: this.schedule.psStartDate,
      psEndDate: this.schedule.psEndDate
    };
 
    this.loading = true;
    this.productionService.createSchedule(payload).subscribe({
      next: (res) => {
        this.loading = false;
        console.log('✅ Schedule created:', res);
        alert('✅ Production schedule created successfully!');
        this.router.navigate(['/production']);
      },
      error: (err) => {
        this.loading = false;
        console.error('❌ Failed to create schedule:', err);
        alert(err.error?.error || 'Failed to create production schedule.');
      }
    });
  }
 
  /** ✅ Cancel and go back to production dashboard */
  cancel(): void {
    this.router.navigate(['/production']);
  }
}
 
 
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';  // <-- add this
import { ProductionTrackingService, ProductionScheduleTrackingResponse } from '../../../services/production-tracking.service';
 
@Component({
  selector: 'app-production-tracking',
  standalone: true,
  imports: [CommonModule, FormsModule],  // <-- include FormsModule
  templateUrl: './production-tracking.component.html',
  styleUrls: ['./production-tracking.component.css']
})
export class ProductionTrackingComponent implements OnInit {
  orders: ProductionScheduleTrackingResponse[] = [];
  isLoading = true;
  errorMessage: string | null = null;
  statuses: string[] = ['CREATED', 'IN_PROGRESS', 'COMPLETED']; // allowed statuses
 
  constructor(private trackingService: ProductionTrackingService) {}
 
  ngOnInit(): void {
    this.loadOrders();
  }
 
  loadOrders(): void {
    this.isLoading = true;
    this.trackingService.getAllProductionOrders().subscribe({
      next: (data) => {
        this.orders = data.map(order => ({
          ...order,
          startDate: new Date(order.startDate),
          endDate: new Date(order.endDate)
        }) as unknown as ProductionScheduleTrackingResponse);
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error loading production orders:', err);
        this.errorMessage = 'Failed to load production orders';
        this.isLoading = false;
      }
    });
  }
 
  
 
  changeStatus(order: ProductionScheduleTrackingResponse, newStatus: string): void {
    if (order.status === 'CREATED' && newStatus === 'COMPLETED') {
      this.trackingService.completeProductionOrder(order.scheduleId, 'Inventory Manager').subscribe({
        next: (res: any) => {
          console.log('Order completed successfully:', res);
          alert('Order marked as COMPLETED');
          order.status = 'COMPLETED'; // update UI without refresh
          order.endDate = new Date().toISOString();
         },
        error: (err) => {
          console.error('Failed to complete order:', err);
          alert('Failed to update status');
        }
      });
    } else {
      // revert dropdown if invalid change
      alert('You can only change status from CREATED to COMPLETED.');
    }
  }
 
 
}
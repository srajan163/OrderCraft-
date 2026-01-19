

import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { OrderService } from '../../../services/order.service';
import { interval, Subscription } from 'rxjs';
import {
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
  Validators
} from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-track-order',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    ReactiveFormsModule,   // ✅ using ReactiveForms now
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatListModule,
    MatIconModule
  ],
  templateUrl: './track-order.component.html',
  styleUrls: ['./track-order.component.css']
})
export class TrackOrderComponent implements OnInit, OnDestroy {
  orderId: number | null = null;
  currentStatus: string = '';
  statusHistory: { status: string; time: string }[] = [];
  refreshSub: Subscription | null = null;
  isCancelled: boolean = false;

  trackForm!: FormGroup;
  serverError: string = '';
 

  statusStages: string[] = [
    'ORDER PLACED',
    'ORDER CREATED',
    'ORDER SHIPPED',
    'ORDER DELIVERED'
  ];

  constructor(
    private route: ActivatedRoute,
    private orderService: OrderService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    // ✅ Reactive Form setup
    this.trackForm = this.fb.group({
      inputOrderId: ['', [Validators.required, Validators.min(1)]]
    });

    const idParam = this.route.snapshot.paramMap.get('id');
    this.orderId = idParam ? +idParam : null;
 

    if (this.orderId) {
      this.loadTrackingData(this.orderId);
      this.refreshSub = interval(15000).subscribe(() => {
        this.loadTrackingData(this.orderId!);
      });
    }
  }

  get inputOrderId() {
    return this.trackForm.get('inputOrderId');
  }
 

  get progressPercent(): number {
    const index = this.statusStages.indexOf(this.currentStatus.toUpperCase());
    const stageCount = this.statusStages.length - 1;
    return index >= 0 ? (index / stageCount) * 100 : 0;
  }

  trackOrder(): void {
    this.serverError = '';
 

    if (this.trackForm.invalid) {
      this.trackForm.markAllAsTouched();
      return;
    }

    this.orderId = this.inputOrderId?.value;
    this.loadTrackingData(this.orderId!);
  }
 

  private loadTrackingData(orderId: number): void {
    this.fetchStatus(orderId);
    this.fetchStatusHistory(orderId);
  }

  fetchStatus(orderId: number): void {
    this.orderService.getOrderStatus(orderId).subscribe({
      next: (res) => {
        const cleanedStatus =
          res?.replace('Current Status: ', '')?.trim() || 'Unknown';
        const upperStatus = cleanedStatus.toUpperCase();
        this.currentStatus = upperStatus;

 
        if (upperStatus === 'CANCELLED') {
          this.isCancelled = true;
 

          // Stop further refreshes
          this.refreshSub?.unsubscribe();
          this.refreshSub = null;
        }
      },
      error: (err) => {
        console.error('Error fetching status:', err);
        this.serverError = '❌ Order not found or server error.';
        this.currentStatus = '';
      }
    });
  }

  fetchStatusHistory(orderId: number): void {
    if (this.isCancelled) return;
 

    this.orderService.getOrderStatusHistory(orderId).subscribe({
      next: (res) => {
        this.statusHistory = Object.entries(res).map(([status, time]) => ({
          status,
          time: new Date(time).toLocaleString()
        }));
      },
      error: (err) => {
        console.error('Error fetching status history:', err);
        this.statusHistory = [];
      }
    });
  }

  ngOnDestroy(): void {
    this.refreshSub?.unsubscribe();
  }
}


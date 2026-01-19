import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { OrderService } from '../../../services/order.service';

@Component({
  selector: 'app-order-cancel',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './cancel-order.component.html',
  styleUrls: ['./cancel-order.component.css']
})
export class OrderCancelComponent {
  orderId: string = '';
  order: any = null;
  loading: boolean = false;
  errorMsg: string = '';

  constructor(private http: HttpClient,private orderService: OrderService) {}

  loadOrder(): void {
  if (!this.orderId.trim()) {
    alert('Please enter a valid Order ID.');
    return;
  }

  this.loading = true;
  this.errorMsg = '';
  this.order = null;

  this.http.get<any>(`http://localhost:8086/api/orders/${this.orderId}`)
    .subscribe({
      next: data => {
        this.order = data;
        this.loading = false;
      },
      error: error => {
        console.error('Error loading order:', error);
        this.errorMsg = 'Order not found!';
        this.loading = false;
      }
    });
}

isCancellable(): boolean {
  const status = this.order?.poDeliveryStatus?.toUpperCase();
  return status === 'ORDER PLACED' || status === 'PENDING';
}

cancelOrder(): void {
  if (!this.orderId.trim()) {
    alert('Order ID is required.');
    return;
  }

  const confirmCancel = confirm(`Are you sure you want to cancel order ID: ${this.orderId}?`);
  if (!confirmCancel) return;

  this.loading = true;

  // 🔥 Call the service instead of raw HttpClient
  this.orderService.cancelOrder(+this.orderId).subscribe({
    next: (res) => {
      alert(res);
      this.order = null;
      this.orderId = '';
      this.loading = false;
    },
    error: error => {
      console.error('Cancel failed:', error);
      alert(error.error || 'Failed to cancel order.');
      this.loading = false;
    }
  });
}
}

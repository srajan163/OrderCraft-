import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { FormsModule, ReactiveFormsModule, FormControl } from '@angular/forms';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { Router, RouterModule } from '@angular/router';
import { OrderService } from '../../../services/order.service';
import {MatButtonToggleModule} from '@angular/material/button-toggle';
 
@Component({
  selector: 'app-order-history',
  standalone: true,
  imports: [
    CommonModule,
    HttpClientModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatExpansionModule,
    MatSelectModule,
    MatFormFieldModule,
    MatInputModule,
    MatSnackBarModule,
    MatDialogModule,
    ReactiveFormsModule,
    FormsModule,
    MatPaginatorModule,
    MatSortModule,
    MatProgressSpinnerModule,
    MatDatepickerModule,
    MatNativeDateModule,
    RouterModule,
    MatButtonToggleModule
  ],
  templateUrl: './order-history.component.html',
  styleUrl: './order-history.component.css',
 
})
export class OrderHistoryComponent implements OnInit {
  constructor(private orderService: OrderService){}
  private http = inject(HttpClient);
  private snackBar = inject(MatSnackBar);
  private dialog = inject(MatDialog);
  private router = inject(Router);
 
 
  orders: any[] = [];
  filteredOrders: any[] = [];
  selectedStatus: string = '';
  selectedSupplier: string = '';
  uniqueSuppliers: string[] = [];
  selectedOrderType: string = 'all';
  isLoading = true;
  sortField: string = 'poId';  // default
  sortDirection: 'asc' | 'desc' = 'asc';
 
  dateControl = new FormControl();
 
  ngOnInit() {
    this.fetchOrders();
  }
 
  fetchOrders() {
  this.isLoading = true;
  this.filteredOrders = [];
  this.orders = [];
 
  console.log("Fetching internal orders...");
 
  this.http.get<any[]>('/api/orders/internal').subscribe({
    next: internal => {
      console.log("Internal orders:", internal);
 
      this.http.get<any[]>('/api/orders/supplier').subscribe({
        next: supplier => {
          console.log("Supplier orders:", supplier);
 
          this.orders = [...internal, ...supplier];
          this.filteredOrders = [...this.orders];
          this.isLoading = false;
        },
        error: supplierErr => {
          console.error('Supplier orders fetch failed:', supplierErr);
          this.snackBar.open('Failed to load supplier orders', 'Close');
          this.isLoading = false;
        }
      });
    },
    error: internalErr => {
      console.error('Internal orders fetch failed:', internalErr);
      this.snackBar.open('Failed to load internal orders', 'Close');
      this.isLoading = false;
    }
  });
}
 
 
   applyFilters() {
  this.filteredOrders = this.orders.filter(order => {
    const statusMatch = this.selectedStatus ? order.poDeliveryStatus === this.selectedStatus : true;
    const supplierMatch = this.selectedSupplier ? order.supplierName?.toLowerCase().includes(this.selectedSupplier.toLowerCase()) : true;
    const typeMatch = this.selectedOrderType === 'all' ||
      (this.selectedOrderType === 'internal' && order.supplierName === 'BGSW') ||
      (this.selectedOrderType === 'supplier' && order.supplierName !== 'BGSW');
    return statusMatch && supplierMatch && typeMatch;
  });
 
  this.sortOrders();  // 🔁 Apply sorting after filtering
}
 
 
 
  filterByDate() {
  const selectedDate = this.dateControl.value;
  if (selectedDate) {
    this.filteredOrders = this.orders.filter(order =>
      new Date(order.poOrderDate).toDateString() === new Date(selectedDate).toDateString()
    );
  } else {
    this.filteredOrders = [...this.orders];
  }
 
  this.sortOrders();  // 🔁 Sort after date filtering
}
 
 
 
 
  updateOrder(orderId: number) {
    this.snackBar.open(`Update Order ID ${orderId}`, 'Close', { duration: 2000 });
  }
 
  cancelOrder(orderId: number) {
    this.snackBar.open(`Cancel Order ID ${orderId}`, 'Close', { duration: 2000 });
  }
 
  downloadInvoice(orderId: number, supplierName: string) {
  const isBGSW = supplierName?.toLowerCase() === 'bgsw';
 
  const download$ = isBGSW
    ? this.orderService.downloadCustomerInvoicePdf(orderId)  // for BGSW (internal order)
    : this.orderService.downloadSupplierInvoicePdf(orderId); // for others (supplier order)
 
  download$.subscribe({
    next: (blob) => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `invoice_${orderId}.pdf`;
      a.click();
      window.URL.revokeObjectURL(url);
    },
    error: (err) => {
      this.snackBar.open('Failed to download invoice', 'Close', { duration: 3000 });
      console.error(err);
    }
  });
}
 
 
  trackOrder(orderId: number) {
    this.router.navigate(['/track-order', orderId]);
  }
 
  toggleItem(order: any) {
    order.expanded = !order.expanded;
  }
 
    sortOrders() {
  this.filteredOrders.sort((a, b) => {
    const aValue = a[this.sortField];
    const bValue = b[this.sortField];
 
    if (aValue == null || bValue == null) return 0;
 
    let comparison = 0;
 
    // Convert date strings to Date objects
    const isDateField = this.sortField.toLowerCase().includes('date');
    const aVal = isDateField ? new Date(aValue) : aValue;
    const bVal = isDateField ? new Date(bValue) : bValue;
 
    if (typeof aVal === 'number' && typeof bVal === 'number') {
      comparison = aVal - bVal;
    } else if (aVal instanceof Date && bVal instanceof Date) {
      comparison = aVal.getTime() - bVal.getTime();
    } else {
      comparison = String(aVal).localeCompare(String(bVal));
    }
 
    return this.sortDirection === 'asc' ? comparison : -comparison;
  });
}
 
}
 
 
 
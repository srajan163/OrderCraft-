import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SupplierHistoryService } from '../../../services/supplier-history.service';

@Component({
  selector: 'app-supplier-history',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './supplier-history.component.html',
  styleUrls: ['./supplier-history.component.css']
})
export class SupplierHistoryComponent {

  supplierId!: number;
  paymentHistory: any[] = [];
  message: string = "";
  loading: boolean = false;

  constructor(private supplierHistoryService: SupplierHistoryService) {}

  getSupplierHistory() {
    if (!this.supplierId) {
      this.message = "⚠ Please enter a Supplier ID";
      this.paymentHistory = [];
      return;
    }

    this.loading = true;
    this.message = "";

    this.supplierHistoryService.getSupplierPaymentHistory(this.supplierId)
      .subscribe({
        next: (data) => {
          this.paymentHistory = data;
          if (data.length === 0) {
            this.message = "No payment history found.";
          }
          this.loading = false;
        },
        error: (err) => {
          if (err.status === 404) {
            this.message = "No payment history found for this Supplier ID.";
          } else {
            this.message = "Error loading history. Try again.";
          }
          this.paymentHistory = [];
          this.loading = false;
        }
      });
  }
}

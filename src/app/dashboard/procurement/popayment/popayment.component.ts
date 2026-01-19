import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { POPaymentService } from '../../../services/po-payment.service';

@Component({
  standalone: true,
  selector: 'app-po-payment',
  imports: [CommonModule, ReactiveFormsModule, HttpClientModule],
  templateUrl: './popayment.component.html',
  styleUrls: ['./popayment.component.css']
})
export class POPaymentComponent {
  paymentForm = this.fb.group({
    orderId: [null, [Validators.required, Validators.min(1)]],
    paymentMethod: ['', Validators.required]
  });

  message: string = '';
  isSuccess: boolean = false;

  constructor(
    private fb: FormBuilder,
    private poPaymentService: POPaymentService
  ) {}

  makePayment(): void {
    if (this.paymentForm.invalid) {
      this.message = 'Please fill out the form correctly.';
      this.isSuccess = false;
      return;
    }

    const { orderId, paymentMethod } = this.paymentForm.value;

    this.poPaymentService.makePayment(orderId!, paymentMethod!).subscribe({
      next: (response: string) => {
        this.message = response;
        this.isSuccess = true;
        this.paymentForm.reset();
      },
      error: (error: any) => {
        this.message = error.error || 'Payment failed. Please try again.';
        this.isSuccess = false;
      }
    });
  }
}

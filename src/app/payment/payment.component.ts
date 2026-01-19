import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
 
@Component({
  selector: 'app-payment',
  standalone: true,
  imports: [CommonModule, FormsModule], // Required for *ngIf and [(ngModel)]
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.css']
})
export class PaymentComponent {
  orderId: number | null = null;
  amount: number = 0;
  paymentMethod: string = '';
  paymentDone: boolean = false;
  showPaymentOptions: boolean = true;
 
  constructor(private route: ActivatedRoute) {}
 
  ngOnInit() {
    this.orderId = Number(this.route.snapshot.queryParamMap.get('orderId'));
  }
 
  processPayment() {
    this.paymentDone = true;
    this.showPaymentOptions = false;
  }
}
 
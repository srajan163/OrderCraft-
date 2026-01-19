import { Component, signal } from '@angular/core';
import {
  FormBuilder,
  FormArray,
  FormGroup,
  Validators,
  ReactiveFormsModule
} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { OrderService, PurchaseOrderUpdateRequest } from '../../../services/order.service';

@Component({
  selector: 'app-update-order',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './update-order.component.html',
  styleUrls: ['./update-order.component.css']
})
export class UpdateOrderComponent {
  orderForm: FormGroup;

  // Angular 17 Signals for reactive UI state
  loading = signal(false);
  errorMessage = signal<string | null>(null);

  constructor(private fb: FormBuilder, private orderService: OrderService) {
    this.orderForm = this.fb.group({
      orderId: [null, Validators.required],
      newExpectedDeliveryDate: ['', Validators.required],
      newDeliveryStatus: ['', Validators.required],
      items: this.fb.array([])
    });
  }

  get items(): FormArray {
    return this.orderForm.get('items') as FormArray;
  }

  createItemGroup(item?: any): FormGroup {
    return this.fb.group({
      itemId: [item?.itemId ?? null, Validators.required],
      newQuantity: [item?.newQuantity ?? item?.quantity ?? null, Validators.required]
    });
  }

  addItem() {
    this.items.push(this.createItemGroup());
  }

  removeItem(index: number) {
    this.items.removeAt(index);
  }

  onOrderIdChange(event: any) {
    const id = event.target.value;
    if (!id) return;

    this.loading.set(true);
    this.errorMessage.set(null);

    this.orderService.getOrderById(id).subscribe({
      next: (order) => {
        this.loading.set(false);
        this.populateForm(order);
      },
      error: (err) => {
        this.loading.set(false);
        console.error('Order fetch error:', err);
        this.errorMessage.set('Failed to load order details.');
      }
    });
  }

  populateForm(order: any) {
    this.orderForm.patchValue({
      orderId: order.id ?? order.orderId,
      newExpectedDeliveryDate: order.newExpectedDeliveryDate ?? order.expectedDeliveryDate,
      newDeliveryStatus: order.newDeliveryStatus ?? order.deliveryStatus
    });

    this.items.clear();
    (order.items || []).forEach((it: any) => {
      this.items.push(this.createItemGroup(it));
    });
  }

  submit() {
    if (this.orderForm.invalid) return;

    const orderId = this.orderForm.value.orderId;
    const request: PurchaseOrderUpdateRequest = {
      newExpectedDeliveryDate: this.orderForm.value.newExpectedDeliveryDate,
      newDeliveryStatus: this.orderForm.value.newDeliveryStatus,
      items: this.orderForm.value.items
    };

    this.orderService.updateOrder(orderId, request).subscribe({
      next: (res) => {
        alert('Order updated successfully!');
        this.errorMessage.set(null);
      },
      error: (err) => {
        console.error(err);
        this.errorMessage.set('Failed to update order.');
      }
    });
  }
}

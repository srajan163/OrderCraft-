// return-order.component.ts
import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-return-order',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, HttpClientModule, RouterModule],
  templateUrl: './return-order.component.html',
  styleUrls: ['./return-order.component.css']
})
export class ReturnOrderComponent {
  returnOrderForm: FormGroup;
  http = inject(HttpClient);
  fb = inject(FormBuilder);

  constructor() {
    this.returnOrderForm = this.fb.group({
      purchaseOrderId: [null, Validators.required],
      returnedByUserId: ['', Validators.required],
      returnDate: ['', Validators.required],
      returnReason: ['', Validators.required],
      status: ['', Validators.required],
      items: this.fb.array([]) // Start empty; will load from backend
    });
  }

  // Getter for items FormArray
  get items(): FormArray {
    return this.returnOrderForm.get('items') as FormArray;
  }

  // Add a new empty return item (optional manual add)
  addItem(): void {
    this.items.push(this.fb.group({
      purchaseOrderItemId: [null, Validators.required],
      productId: [null, Validators.required],
      productName: [''], // optional, display only
      quantity: [1, [Validators.required, Validators.min(1)]],
      conditionNote: ['']
    }));
  }

  // Remove a return item by index
  removeItem(index: number): void {
    this.items.removeAt(index);
  }

  // Ensure quantity is never below 1
  validateQuantity(index: number): void {
  const itemGroup = this.items.at(index);
  const quantityControl = itemGroup.get('quantity');
  const poItemId = itemGroup.get('purchaseOrderItemId')?.value;

  if (!quantityControl) return;

  // Ensure minimum 1
  if (quantityControl.value < 1) {
    quantityControl.setValue(1);
    return;
  }

  // Find the ordered quantity for this item
  const matched = this.purchaseOrderItemsList.find(i => i.purchaseOrderItemId === poItemId);
  if (matched && quantityControl.value > matched.quantity) {
    alert(`You cannot return more than ${matched.quantity} for this product.`);
    quantityControl.setValue(matched.quantity);
  }
}


  purchaseOrderItemsList: any[] = [];


loadPurchaseOrderItems(poId: number): void {
  if (!poId || poId <= 0) return;

  this.http.get<any>(`http://localhost:8086/api/purchase-order/${poId}/items`)
    .subscribe({
      next: (po) => {
        this.returnOrderForm.patchValue({ returnedByUserId: po.userId });
        this.purchaseOrderItemsList = po.items || []; // store separately
      },
      error: (err) => console.error('Error loading purchase order items:', err)
    });
}

onProductSelect(event: Event): void {
  const selectedId = Number((event.target as HTMLSelectElement).value);
  if (!selectedId) return;

  const selected = this.purchaseOrderItemsList.find(i => i.purchaseOrderItemId === selectedId);
  if (selected) {
    this.items.push(this.fb.group({
      purchaseOrderItemId: [selected.purchaseOrderItemId, Validators.required],
      productId: [selected.productId, Validators.required],
      productName: [selected.productName],
      quantity: [1, [Validators.required, Validators.min(1)]],
      conditionNote: ['']
    }));
  }
}


  // Submit return order
  onSubmit(): void {
  if (this.returnOrderForm.valid) {
    console.log('Submitting return order:', this.returnOrderForm.value);

    this.http.post('http://localhost:8086/api/return-orders', this.returnOrderForm.value)
      .subscribe({
        next: () => {
          alert('Return Order submitted successfully!');
          window.location.reload(); // refresh the page
        },
        error: (err) => console.error('Error submitting return order:', err)
      });
  }
}

}

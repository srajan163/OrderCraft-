import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormArray,
  Validators,
  ReactiveFormsModule,
  FormsModule
} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { OrderService } from '../../../services/order.service';
import { UserService } from '../../../services/user.service';

@Component({
  selector: 'app-supplier-order-create',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './create-order.component.html',
  styleUrls: ['./create-order.component.css']
})
export class SupplierOrderCreateComponent implements OnInit {
  orderForm: FormGroup;
  suppliers: any[] = [];
  rawMaterials: any[] = [];

  constructor(
    private fb: FormBuilder,
    private orderService: OrderService,
    private userService: UserService
  ) {
    this.orderForm = this.fb.group({
      supplierId: ['', Validators.required],
      items: this.fb.array([this.createItem()]),
      expectedDeliveryDate: ['', Validators.required]   // ✅ added to match template
    });
  }

  ngOnInit(): void {
    this.fetchSuppliers();
    this.fetchRawMaterials();
  }

  createItem(): FormGroup {
    return this.fb.group({
      rWId: ['', Validators.required],
      quantity: [1, [Validators.required, Validators.min(1)]],
      cost: [{ value: 0, disabled: true }]
    });
  }

  get orderItems(): FormArray {
    return this.orderForm.get('items') as FormArray;
  }

  addItem(): void {
    this.orderItems.push(this.createItem());
  }

  removeItem(index: number): void {
    if (this.orderItems.length > 1) {
      this.orderItems.removeAt(index);
    }
  }

  
  fetchSuppliers(): void {
  this.orderService.getAllSuppliers().subscribe({
    next: (data) => {
      // Exclude BGSW
      this.suppliers = data.filter(s => s.name !== 'BGSW');
      console.log('Suppliers loaded (without BGSW):', this.suppliers);
    },
    error: (err) => console.error('❌ Failed to load suppliers', err)
  });
}

  
  fetchRawMaterials(): void {
  this.orderService.getAllRawMaterials().subscribe({
    next: (data) => {
      console.log('✅ Raw Materials fetched:', data);
      this.rawMaterials = data;
    },
    error: (err) => console.error('❌ Failed to load raw materials', err)
  });
}


onRawMaterialSelected(index: number): void {
  const item = this.orderItems.at(index);
  const rWId = item.get('rWId')?.value;
  const rawMat = this.rawMaterials.find(rm => rm.rwId == rWId);

  if (rawMat) {
    const quantity = Number(item.get('quantity')?.value || 1);
    const cost = (rawMat.rwUnitPrice || 0) * quantity;
    item.get('cost')?.setValue(cost);
  } else {
    item.get('cost')?.setValue(0);
  }
}

onQuantityChanged(index: number): void {
  this.updateCost(index);
}

updateCost(index: number): void {
  const item = this.orderItems.at(index);
  const rWId = item.get('rWId')?.value;
  const quantity = Number(item.get('quantity')?.value || 1);
  const rawMat = this.rawMaterials.find(rm => rm.rwId == rWId);

  if (rawMat) {
    const cost = (rawMat.rwUnitPrice || 0) * quantity;
    item.get('cost')?.setValue(cost);
  } else {
    item.get('cost')?.setValue(0);
  }
}



  submitOrder(): void {
    if (this.orderForm.invalid) {
      alert('⚠️ Please fill all required fields before submitting.');
      return;
    }

    const supplierId = Number(this.orderForm.value.supplierId);

    const itemsPayload = this.orderItems.controls.map((item) => ({
      rWId: Number(item.get('rWId')?.value),
      quantity: Number(item.get('quantity')?.value)
    }));

    const requestPayload = {
      supplierId,
      expectedDeliveryDate: this.orderForm.value.expectedDeliveryDate, // ✅ include delivery date
      items: itemsPayload
    };

    console.log('✅ Final payload for backend:', requestPayload);

    this.orderService.createPurchaseOrder(requestPayload).subscribe({
      next: () => {
        alert('✅ Supplier order created successfully!');
        this.orderForm.reset();
        this.orderItems.clear();
        this.addItem();
      },
      error: (err) => {
        console.error('❌ Error creating supplier order:', err);
        alert('❌ Failed to create supplier order.');
      }
    });
  }
}

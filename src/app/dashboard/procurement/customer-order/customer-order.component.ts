import { Component } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { RouterModule, Router } from '@angular/router';

@Component({
  selector: 'app-customer-order',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './customer-order.component.html',
  styleUrls: ['./customer-order.component.css']
})
export class CustomerOrderComponent {
  orderForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router
  ) {
    this.orderForm = this.fb.group({
      customerName: ['', Validators.required],
      customerEmail: ['', [Validators.required, Validators.email]],
      customerMobile: ['', [Validators.required, Validators.pattern(/^\d{10}$/)]],
      customerAddress: ['', Validators.required],
      items: this.fb.array([this.createItem()])
    });
  }

  // Shortcut to access form controls
  get f() {
    return this.orderForm.controls;
  }

  // Accessor for FormArray
  get items(): FormArray {
    return this.orderForm.get('items') as FormArray;
  }

  createItem(): FormGroup {
    return this.fb.group({
      productId: ['', Validators.required],
      quantity: [1, [Validators.required, Validators.min(1)]]
    });
  }

  addItem(): void {
    this.items.push(this.createItem());
  }

  removeItem(index: number): void {
    if (this.items.length > 1) {
      this.items.removeAt(index);
    }
  }

  // onSubmit(): void {
  //   if (this.orderForm.invalid) {
  //     this.orderForm.markAllAsTouched();
  //     alert('Please fix the validation errors before submitting.');
  //     return;
  //   }

  //   const token = localStorage.getItem('token');

  //   const headers = new HttpHeaders({
  //     Authorization: `Bearer ${token}`
  //   });

  //   this.http.post('http://localhost:8086/api/internal-order', this.orderForm.value, { headers })
  //   .subscribe({
  //     next: () => {
  //       alert('Customer order submitted successfully!');
  //       this.orderForm.reset();
  //       // Clear all items and reset to one row
  //       while (this.items.length !== 0) {
  //         this.items.removeAt(0);
  //       }
  //       this.items.push(this.createItem());
  //     },
  //     error: (err) => {
  //       console.error('Order submission failed:', err);
  //       alert('Submited customer order.');
  //     }
  //   });
  // }

  onSubmit(): void {
    if (this.orderForm.invalid) {
      this.orderForm.markAllAsTouched();
      alert('Please fix the validation errors before submitting.');
      return;
    }

    const token = localStorage.getItem('token');
    if (!token) {
      alert('You are not logged in. Please log in first.');
      return;
    }

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    this.http.post('http://localhost:8086/api/internal-order', this.orderForm.value, { 
      headers,
      responseType: 'text'  // 👈 important
    })
    .subscribe({
      next: (res) => {
        alert(res); // res is plain text now
        this.orderForm.reset();
        while (this.items.length !== 0) {
          this.items.removeAt(0);
        }
        this.items.push(this.createItem());
      },
      error: (err) => {
        console.error('Order submission failed:', err);
        alert('Order submission failed. Check console for details.');
      }
    });
  }

}

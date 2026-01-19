import { Component, OnInit } from '@angular/core';
import { SupplierService } from '../../../services/suppliers.service';
import { Supplier } from '../../../models/supplier.model';

@Component({
  selector: 'app-suppliers',
  templateUrl: './suppliers.component.html',
  styleUrls: ['./suppliers.component.css']
})
export class SuppliersComponent implements OnInit {
  suppliers: Supplier[] = [];
  newSupplier: Supplier = { name: '', email: '', phone: '', address: '' };
  editingSupplier: Supplier | null = null;

  constructor(private supplierService: SupplierService) {}

  ngOnInit(): void {
    this.loadSuppliers();
  }

  loadSuppliers(): void {
    this.supplierService.getAllSuppliers().subscribe({
      next: (data) => (this.suppliers = data),
      error: (err) => alert('Error loading suppliers: ' + err.message)
    });
  }

  addSupplier(): void {
    if (!this.newSupplier.name || !this.newSupplier.email) {
      alert('Name and email are required!');
      return;
    }

    this.supplierService.addSupplier(this.newSupplier).subscribe({
      next: (data) => {
        alert('Supplier added successfully!');
        this.newSupplier = { name: '', email: '', phone: '', address: '' };
        this.loadSuppliers();
      },
      error: (err) => alert('Error adding supplier: ' + err.message)
    });
  }

  startEdit(supplier: Supplier): void {
    this.editingSupplier = { ...supplier };
  }

  cancelEdit(): void {
    this.editingSupplier = null;
  }

  updateSupplier(): void {
    if (!this.editingSupplier) return;

    this.supplierService.updateSupplier(this.editingSupplier.suppliersId!, this.editingSupplier).subscribe({
      next: () => {
        alert('Supplier updated successfully!');
        this.editingSupplier = null;
        this.loadSuppliers();
      },
      error: (err) => alert('Error updating supplier: ' + err.message)
    });
  }

  deleteSupplier(id: number | undefined): void {
    if (!id) return;
    if (confirm('Are you sure you want to delete this supplier?')) {
      this.supplierService.deleteSupplier(id).subscribe({
        next: () => this.loadSuppliers(),
        error: (err) => alert('Error deleting supplier: ' + err.message)
      });
    }
  }
}

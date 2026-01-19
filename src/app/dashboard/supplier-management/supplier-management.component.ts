import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SupplierService } from '../../services/suppliers.service';
import { Supplier } from '../../models/supplier.model';

@Component({
  selector: 'app-supplier-management',
  standalone: true,
  templateUrl: './supplier-management.component.html',
  styleUrls: ['./supplier-management.component.css'],
  imports: [CommonModule, FormsModule]
})
export class SupplierManagementComponent implements OnInit {

  suppliers: Supplier[] = [];
  searchText: string = '';

  // Form model
  form: Supplier = {
    suppliersName: '',
    suppliersPhone: '',
    suppliersEmail: '',
    suppliersContactPerson: '',
    supplierAddress: {
      addressStreet: '',
      addressCity: '',
      addressState: '',
      addressCountry: '',
      addressPostalCode: ''
    }
  };

  isEditing = false;

  constructor(private supplierService: SupplierService) {}

  ngOnInit(): void {
    this.loadSuppliers();
  }

  /* Load all suppliers */
  loadSuppliers() {
    this.supplierService.getAllSuppliers().subscribe(res => {
      this.suppliers = res;
    });
  }

  /* Filter suppliers */
  get filteredSuppliers() {
    return this.suppliers.filter(s =>
      s.suppliersName.toLowerCase().includes(this.searchText.toLowerCase())
    );
  }

  /* Reset form */
  resetForm() {
    this.isEditing = false;

    this.form = {
      suppliersName: '',
      suppliersPhone: '',
      suppliersEmail: '',
      suppliersContactPerson: '',
      supplierAddress: {
        addressStreet: '',
        addressCity: '',
        addressState: '',
        addressCountry: '',
        addressPostalCode: ''
      }
    };
  }

  /* Load Add Form */
  openAddForm() {
    this.resetForm();
    this.isEditing = false;
  }

  /* Load Edit Form */
  openEditForm(supplier: Supplier) {
    this.isEditing = true;

    this.form = {
      suppliersId: supplier.suppliersId,
      suppliersName: supplier.suppliersName,
      suppliersPhone: supplier.suppliersPhone,
      suppliersEmail: supplier.suppliersEmail,
      suppliersContactPerson: supplier.suppliersContactPerson,
      supplierAddress: {
        addressId: supplier.supplierAddress.addressId,
        addressStreet: supplier.supplierAddress.addressStreet,
        addressCity: supplier.supplierAddress.addressCity,
        addressState: supplier.supplierAddress.addressState,
        addressCountry: supplier.supplierAddress.addressCountry,
        addressPostalCode: supplier.supplierAddress.addressPostalCode
      }
    };
  }

  /* Save or Update Supplier */
  saveSupplier() {

    if (this.isEditing) {
      // UPDATE
      this.supplierService.updateSupplier(this.form.suppliersId!, this.form)
        .subscribe(() => {
          alert("Supplier Updated Successfully!");
          this.loadSuppliers();
          this.resetForm();
        });

    } else {
      // ADD
      this.supplierService.createSupplier(this.form)
        .subscribe(() => {
          alert("Supplier Added Successfully!");
          this.loadSuppliers();
          this.resetForm();
        });
    }
  }

  /* Delete Supplier */
  deleteSupplier(id: number) {
    if (!confirm("Are you sure you want to delete this supplier?")) return;

    this.supplierService.deleteSupplier(id).subscribe(() => {
      alert("Supplier Deleted!");
      this.loadSuppliers();
    });
  }
}

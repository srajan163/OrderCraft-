//product.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Product, ProductService } from '../../../services/product-categorization/product.service';
import { Category, CategoryService } from '../../../services/product-categorization/category.service';
import { Supplier, SupplierService } from '../../../services/product-categorization/supplier.service';
 
 

@Component({
  selector: 'app-product',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css']
})
export class ProductComponent implements OnInit {
  products: Product[] = [];
  categories: Category[] = [];
  suppliers: Supplier[] = [];
 
  newProduct: Product = {
    productsName: '',
    productsDescription: '',
    productsUnitPrice: null,
    productsQuantity: null,
    productsImage: '',
    minThreshold: null,
    maxThreshold: null
  };
 
  constructor(
    private productService: ProductService,
    private categoryService: CategoryService,
    private supplierService: SupplierService
  ) {}
 
  ngOnInit(): void {
    this.loadProducts();
    this.loadCategories();
    this.loadSuppliers();
  }
 
  loadProducts() {
    this.productService.getAllProducts().subscribe(data => this.products = data);
  }
 
  loadCategories() {
    this.categoryService.getAllCategories().subscribe(data => this.categories = data);
  }
 
  loadSuppliers() {
    this.supplierService.getAllSuppliers().subscribe(data => this.suppliers = data);
  }
 
  addProduct() {
    this.productService.addProduct(this.newProduct).subscribe(() => {
      this.loadProducts();
      this.newProduct = {
        productsName: '',
        productsDescription: '',
        productsUnitPrice: null,
        productsQuantity: null,
        productsImage: '',
        minThreshold: null,
        maxThreshold: null
      };
    });
  }
 
  deleteProduct(id: number) {
    this.productService.deleteProduct(id).subscribe(() => this.loadProducts());
  }
 
  assignCategory(productId: number, categoryId: number) {
    this.productService.assignCategory(productId, categoryId).subscribe(() => this.loadProducts());
  }
 
  assignSupplier(productId: number, supplierId: number) {
    this.productService.assignSupplier(productId, supplierId).subscribe(() => this.loadProducts());
  }
}
 
 
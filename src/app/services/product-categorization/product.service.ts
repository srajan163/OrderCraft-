import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Category {
  categoryId: number;  // Changed from categoriesId
  categoryName: string;
}

export interface Supplier {
  suppliersId: number;
  suppliersName: string;
  suppliersPhone: string;
  suppliersEmail: string;
  suppliersContactPerson: string;
}

export interface Product {
  productsId?: number;
  productsName: string;
  productsDescription: string;
  productsUnitPrice: number | null;
  productsQuantity: number | null;
  productsImage: string;
  minThreshold: number | null;
  maxThreshold: number | null;

  category?: Category;
  supplier?: Supplier;
}

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiUrl = 'http://localhost:8086/products';

  constructor(private http: HttpClient) {}

  getAllProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.apiUrl);
  }

  getProductById(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.apiUrl}/${id}`);
  }

  addProduct(product: Product): Observable<Product> {
    return this.http.post<Product>(this.apiUrl, product);
  }

  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  assignCategory(productId: number, categoryId: number): Observable<Product> {
    return this.http.put<Product>(`${this.apiUrl}/${productId}/category/${categoryId}`, {});
  }

  assignSupplier(productId: number, supplierId: number): Observable<Product> {
    return this.http.put<Product>(`${this.apiUrl}/${productId}/supplier/${supplierId}`, {});
  }

  getProductsByCategory(categoryId: number): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/category/${categoryId}`);
  }

  getProductsBySupplier(supplierId: number): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/supplier/${supplierId}`);
  }
}

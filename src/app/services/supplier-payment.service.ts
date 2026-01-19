import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

const API_BASE = 'http://localhost:8086/api/supplier-payments'; // change if needed

export interface Supplier {
  suppliersId: number;
  suppliersName?: string;
}

export interface SupplierPayment {
  paymentId?: number;
  supplier: Supplier;
  invoiceNumber: string;
  invoiceDate: string;  // yyyy-MM-dd
  dueDate: string;      // yyyy-MM-dd
  amount: number;
  paidAmount?: number;
  status?: 'PENDING' | 'PARTIAL' | 'PAID';
  createdAt?: string;
  updatedAt?: string;
}

@Injectable({ providedIn: 'root' })
export class SupplierPaymentService {
  constructor(private http: HttpClient) {}

  createPayment(payload: SupplierPayment): Observable<SupplierPayment> {
    return this.http.post<SupplierPayment>(API_BASE, payload);
  }

  getAll(): Observable<SupplierPayment[]> {
    return this.http.get<SupplierPayment[]>(API_BASE);
  }

  getBySupplier(supplierId: number): Observable<SupplierPayment[]> {
    return this.http.get<SupplierPayment[]>(`${API_BASE}/supplier/${supplierId}`);
  }

  getOutstanding(): Observable<SupplierPayment[]> {
    return this.http.get<SupplierPayment[]>(`${API_BASE}/outstanding`);
  }

  applyPayment(paymentId: number, amount: number): Observable<SupplierPayment> {
    return this.http.put<SupplierPayment>(`${API_BASE}/${paymentId}/pay`, { amount });
  }

  delete(paymentId: number): Observable<{ deleted: number }> {
    return this.http.delete<{ deleted: number }>(`${API_BASE}/${paymentId}`);
  }
}

// src/app/supplier-report.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SupplierReportService {

  private baseUrl = '/api/reports/suppliers';

  constructor(private http: HttpClient) { }

  getSupplierPerformance(id: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/${id}/performance`);
  }

  downloadSupplierPdf(id: number): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/${id}/performance/pdf`, { responseType: 'blob' });
  }

  downloadSupplierExcel(id: number): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/${id}/performance/excel`, { responseType: 'blob' });
  }

  getAllSuppliersPdf(): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/pdf`, { responseType: 'blob' });
  }

  getAllSuppliersExcel(): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/excel`, { responseType: 'blob' });
  }
}

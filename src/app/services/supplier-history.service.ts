import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SupplierHistoryService {

  private baseUrl = 'http://localhost:8086/api/suppliershistory';

  constructor(private http: HttpClient) {}

  getSupplierPaymentHistory(supplierId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/${supplierId}/history`);
  }
}

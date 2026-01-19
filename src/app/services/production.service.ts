import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ProductionUnit {
  unitId: number;
  unitName: string;
  capacity: number;
  available: boolean;
  category: { categoryId: number; categoryName: string };
}

export interface ProductionSchedule {
  psId: number;
  psQuantity: number;
  psStartDate: string;
  psDeadline: string;
  psEndDate?: string; // ✅ added
  status: string;
  product: { productsId: number; productsName: string };
  productionUnit?: ProductionUnit;
}

@Injectable({
  providedIn: 'root'
})
export class ProductionService {
  private apiUrl = 'http://localhost:8086/api';

  private http = inject(HttpClient);

  // ✅ Units
  getAllUnits(): Observable<ProductionUnit[]> {
    return this.http.get<ProductionUnit[]>(`${this.apiUrl}/units`);
  }

  getUnitsByCategory(catId: number): Observable<ProductionUnit[]> {
    return this.http.get<ProductionUnit[]>(`${this.apiUrl}/units/category/${catId}`);
  }

  // ✅ Schedules
  getAllSchedules(): Observable<ProductionSchedule[]> {
    return this.http.get<ProductionSchedule[]>(`${this.apiUrl}/schedules`);
  }

  createSchedule(schedule: any): Observable<ProductionSchedule> {
    return this.http.post<ProductionSchedule>(`${this.apiUrl}/schedules`, schedule);
  }

  markAsDone(id: number): Observable<ProductionSchedule> {
    return this.http.put<ProductionSchedule>(`${this.apiUrl}/schedules/${id}/done`, {});
  }

  cancelSchedule(id: number): Observable<ProductionSchedule> {
    return this.http.put<ProductionSchedule>(`${this.apiUrl}/schedules/${id}/cancel`, {});
  }

  /** Update deadline of a production schedule */
  
// production.service.ts
updateDeadline(psId: number, payload: { psDeadline: string }): Observable<any> {
    return this.http.put(`${this.apiUrl}/schedules/${psId}/deadline`, payload, {
      headers: { 'Content-Type': 'application/json' }
    });
  }


 
  /** Update status of a schedule (DONE / CANCELLED) */
  updateStatus(psId: number, status: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/schedules/${psId}/status`, {
      status: status
    });
  }
}

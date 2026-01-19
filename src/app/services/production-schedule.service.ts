import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ProductionSchedule {
  psId?: number;
  psQuantity: number;
  psStartDate?: string;
  psDeadline?: string;
  status?: string;
  product?: { productsId: number; productsName?: string };
  productionUnit?: { unitId: number; unitName?: string };
}

@Injectable({ providedIn: 'root' })
export class ProductionScheduleService {
  private baseUrl = 'http://localhost:8086/api/schedules';

  constructor(private http: HttpClient) {}

  getAll(): Observable<ProductionSchedule[]> {
    return this.http.get<ProductionSchedule[]>(this.baseUrl);
  }

  create(schedule: ProductionSchedule): Observable<ProductionSchedule> {
    return this.http.post<ProductionSchedule>(this.baseUrl, schedule);
  }

  setDeadline(id: number, deadline: string): Observable<ProductionSchedule> {
    return this.http.put<ProductionSchedule>(`${this.baseUrl}/${id}/deadline?deadline=${deadline}`, {});
  }

  markDone(id: number): Observable<ProductionSchedule> {
    return this.http.put<ProductionSchedule>(`${this.baseUrl}/${id}/done`, {});
  }

  cancel(id: number): Observable<ProductionSchedule> {
    return this.http.put<ProductionSchedule>(`${this.baseUrl}/${id}/cancel`, {});
  }
}

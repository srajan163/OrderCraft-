import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ProductionUnit {
  unitId: number;
  unitName: string;
  capacity: number;
  available: boolean;
  status: string;
  category?: {
    categoryId: number;
    categoryName: string;
  };
}

@Injectable({
  providedIn: 'root'
})
export class TrackProductionService {

  private baseUrl = 'http://localhost:8086/api/track-production';

  constructor(private http: HttpClient) {}

  getAllUnits(): Observable<ProductionUnit[]> {
    return this.http.get<ProductionUnit[]>(this.baseUrl);
  }

  getUnitsByStatus(status: string): Observable<ProductionUnit[]> {
    return this.http.get<ProductionUnit[]>(`${this.baseUrl}/status/${status}`);
  }

  updateStatus(id: number, status: string): Observable<ProductionUnit> {
    return this.http.put<ProductionUnit>(`${this.baseUrl}/${id}/status?status=${status}`, {});
  }
}

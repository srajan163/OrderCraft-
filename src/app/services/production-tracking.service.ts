import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ProductionScheduleTrackingResponse {
  scheduleId: number;
  productId: number;
  productName: string;
  quantity: number;
  startDate: string;
  endDate: string;
  status: string;
}

@Injectable({
  providedIn: 'root'
})
export class ProductionTrackingService {
  private apiUrl = 'http://localhost:8086/api/production-tracking';
  private headers = new HttpHeaders({ 'Content-Type': 'application/json' });
 

  constructor(private http: HttpClient) {}

  getAllProductionOrders(): Observable<ProductionScheduleTrackingResponse[]> {
    return this.http.get<ProductionScheduleTrackingResponse[]>(this.apiUrl);
  }

  getProductionOrderById(id: number): Observable<ProductionScheduleTrackingResponse> {
    return this.http.get<ProductionScheduleTrackingResponse>(`${this.apiUrl}/${id}`);
  }

  completeProductionOrder(scheduleId: number, performedBy: string): Observable<string> {
    return this.http.put(
      `http://localhost:8086/api/production/${scheduleId}/complete`,
      { performedBy },
      { headers: this.headers, responseType: 'text' as 'json' }
    ) as unknown as Observable<string>; // Cast to strict type
  }
 
}

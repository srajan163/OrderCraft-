import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProductionAnalyticsService {

  // Make sure this points directly to the endpoint
  private baseUrl = 'http://localhost:8086/api/analytics/production-summary';

  constructor(private http: HttpClient) {}

 getProductionSummary(): Observable<any> {
  return this.http.get(`${this.baseUrl}`, {
    headers: {
      Authorization: `Bearer ${localStorage.getItem('token')}`
    }
  });
}
}

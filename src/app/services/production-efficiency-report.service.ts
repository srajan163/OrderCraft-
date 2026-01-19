import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ProductionEfficiencyReportService {

  private baseUrl = 'http://localhost:8086/api/productionefficiencyreports';

  constructor(private http: HttpClient) {}

  getEfficiencyReport(unitId: any, startDate: string, endDate: string): Observable<any> {
    let url = `${this.baseUrl}/efficiency?startDate=${startDate}&endDate=${endDate}`;
    if (unitId) {
      url += `&unitId=${unitId}`;
    }
    return this.http.get(url);
  }

  getAllUnitsReport(startDate: string, endDate: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/efficiency/all?startDate=${startDate}&endDate=${endDate}`);
  }
}

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProductionReportService {

  private apiUrl = '/api/reports/production';

  constructor(private http: HttpClient) {}

  generateReport(request: any): Observable<any> {
    return this.http.post<any[]>(this.apiUrl, request);
  }

  exportReport(request: any, format: string): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/export?format=${format}`, {
      body: request,
      responseType: 'blob'
    });
  }
}

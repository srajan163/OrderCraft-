import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TrendAnalysisReportService {

  private apiUrl = 'http://localhost:8086/api/reports/trends';

  constructor(private http: HttpClient) {}

  getTrendReport(metric: string, startDate: string, endDate: string): Observable<any> {
    let params = new HttpParams()
      .set('metric', metric)
      .set('startDate', startDate)
      .set('endDate', endDate);

    return this.http.get<any>(this.apiUrl, { params });
  }
}

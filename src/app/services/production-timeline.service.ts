import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ProductionTimelineDto {
  status: string;
  updatedBy: string;
  updatedAt: string; // backend returns ISO, parse in UI if needed
  remarks: string;
  quantity: number;
}

@Injectable({
  providedIn: 'root'
})
export class ProductionTimelineService {
  private baseUrl = '/api/production-timeline';

  constructor(private http: HttpClient) {}

  getAll(): Observable<ProductionTimelineDto[]> {
    return this.http.get<ProductionTimelineDto[]>(this.baseUrl);
  }

  getByPsId(psId: number | string): Observable<ProductionTimelineDto[]> {
    return this.http.get<ProductionTimelineDto[]>(`${this.baseUrl}/${psId}`);
  }
}

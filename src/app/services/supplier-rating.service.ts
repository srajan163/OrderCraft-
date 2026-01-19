import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SupplierRatingRequest, SupplierRatingResponse } from '../models/supplier-rating.model';


@Injectable({
  providedIn: 'root'
})
export class SupplierRatingService {
  
  private baseUrl = 'http://localhost:8086/api/ratings';

  constructor(private http: HttpClient) {}

  submitRating(req: SupplierRatingRequest): Observable<SupplierRatingResponse> {
    return this.http.post<SupplierRatingResponse>(this.baseUrl, req);
  }
}

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Supplier {
  suppliersId: number;
  suppliersName: string;
}

@Injectable({
  providedIn: 'root'
})
export class SupplierService {
  private apiUrl = 'http://localhost:8086/suppliers'; // assuming backend has SupplierController

  constructor(private http: HttpClient) {}

  getAllSuppliers(): Observable<Supplier[]> {
  return this.http.get<Supplier[]>(this.apiUrl + '/all');
  }
}

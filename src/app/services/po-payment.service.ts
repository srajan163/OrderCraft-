import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class POPaymentService {
  private baseUrl = 'http://localhost:8086/orders';

  constructor(private http: HttpClient) {}

  makePayment(orderId: number, paymentMethod: string): Observable<string> {
    const url = `${this.baseUrl}/${orderId}/pay`;
    const headers = new HttpHeaders({ 'Content-Type': 'text/plain' });

    // Send paymentMethod as plain text, not as JSON
    return this.http.post(url, paymentMethod, { headers, responseType: 'text' });
  }
}

//http.post('http://localhost:8086/api/orders/1001/pay', null);
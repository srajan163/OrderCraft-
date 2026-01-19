import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { forkJoin, map, Observable } from 'rxjs';

export interface OrderItemUpdate {
  itemId: number;
  newQuantity: number;
}

export interface ProductStockView {
  productId: number;
  productName: string;
  availableQuantity: number;
  minThreshold: number;
  maxThreshold: number;
  alert: string;
}

export interface PurchaseOrderResponse {
  orderId: number;
  newExpectedDeliveryDate: string;
  newDeliveryStatus: string;
  items: OrderItemUpdate[];
}

export interface PurchaseOrderUpdateRequest {
  newExpectedDeliveryDate: string;
  newDeliveryStatus: string;
  items?: { itemId: number; newQuantity: number }[];
}

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private baseUrl = 'http://localhost:8086/api';

  

  constructor(private http: HttpClient) {}

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
  }

  getInternalOrders(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/orders/internal`, {
      headers: this.getAuthHeaders()
    });
  }

  getOrderById(orderId: number): Observable<PurchaseOrderResponse> {
    return this.http.get<PurchaseOrderResponse>(`${this.baseUrl}/orders/${orderId}`);
  }

  getSupplierOrders(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/orders/supplier`, {
      headers: this.getAuthHeaders()
    });
  }

  getAllOrders(): Observable<any[]> {
    return forkJoin([this.getInternalOrders(), this.getSupplierOrders()]).pipe(
      map(([internal, supplier]) => [...internal, ...supplier])
    );
  }

  getOrderStatus(id: number): Observable<string> {
  return this.http.get(`${this.baseUrl}/${id}/status`, {
    headers: this.getAuthHeaders(),
    responseType: 'text'
  });
}

getOrderStatusHistory(id: number): Observable<{ [key: string]: string }> {
  return this.http.get<{ [key: string]: string }>(`${this.baseUrl}/${id}/status/history`, {
    headers: this.getAuthHeaders()
  });
}

cancelOrder(id: number): Observable<string> {
  const headers = new HttpHeaders({
    Authorization: `Bearer ${localStorage.getItem('token') || ''}`
  });

  return this.http.put(
    `${this.baseUrl}/orders/${id}/cancel`,
    {}, // empty body
    { headers, responseType: 'text' }
  );
}

  

  updateOrder(orderId: number, request: PurchaseOrderUpdateRequest): Observable<string> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${localStorage.getItem('token') || ''}`
    });
    return this.http.put(`${this.baseUrl}/orders/${orderId}`, request, { responseType: 'text' });
  }

  downloadInvoice(id: number): Observable<Blob> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${localStorage.getItem('token') || ''}`
    });
    return this.http.get(`/api/orders/${id}/invoice`, {
      headers,
      responseType: 'blob'
    });
  }

downloadCustomerInvoicePdf(orderId: number): Observable<Blob> {
  const headers = new HttpHeaders({
      Authorization: `Bearer ${localStorage.getItem('token') || ''}`
    });
    return this.http.get(`/api/invoices/pdf/${orderId}`, {
      responseType: 'blob'
    });
  }

  downloadSupplierInvoicePdf(orderId: number): Observable<Blob> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${localStorage.getItem('token') || ''}`
    });
  return this.http.post(`/api/supplier-invoices/${orderId}/generate`, null, {
    responseType: 'blob',
    withCredentials: true
  });
}

getAllProducts(): Observable<any[]> {
  return this.http.get<any[]>(`${this.baseUrl}/products`, {
    headers: this.getAuthHeaders()
  });
}

getAllRawMaterials(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/raw-materials`, {
      headers: this.getAuthHeaders()
    });
  }

createPurchaseOrder(data: any): Observable<any> {
  const headers = new HttpHeaders({
  Authorization: `Bearer ${localStorage.getItem('token') || ''}`
  });
  return this.http.post(`${this.baseUrl}/purchase-orders`, data, {
    headers,
    responseType: 'text'  // ✅ avoids JSON parse error
  });
}

getAllSuppliers(): Observable<any[]> {
  return this.http.get<any[]>(`${this.baseUrl}/suppliers`);
}

getAllProductsStockViews(): Observable<ProductStockView[]> {
    const token = localStorage.getItem('token'); // or sessionStorage
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<ProductStockView[]>(`${this.baseUrl}/products/stock-view`, { headers });
  }

}




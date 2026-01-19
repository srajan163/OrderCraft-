import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';

@Component({
  selector: 'app-reports',
  standalone: true,
  imports: [],
  templateUrl: './reports.component.html',
  styleUrl: './reports.component.css'
})
export class ReportsComponent {

  supplierReport: any = null;   // <-- ADD THIS LINE

  constructor(private http: HttpClient) {}

downloadSupplierPDF() {
  this.http.get("http://localhost:8086/api/reports/suppliers/pdf", { responseType: 'blob' })
    .subscribe(blob => this.downloadFile(blob, "Supplier_Report.pdf"));
}

downloadSupplierExcel() {
  this.http.get("http://localhost:8086/api/reports/suppliers/excel", { responseType: 'blob' })
    .subscribe(blob => this.downloadFile(blob, "Supplier_Report.xlsx"));
}

downloadProductionPDF() {
  this.http.get("/api/reports/production/pdf", { responseType: 'blob' })
    .subscribe(blob => this.downloadFile(blob, "Production_Report.pdf"));
}

downloadProductionExcel() {
  this.http.get("/api/reports/production/excel", { responseType: 'blob' })
    .subscribe(blob => this.downloadFile(blob, "Production_Report.xlsx"));
}

private downloadFile(data: Blob, fileName: string) {
  const url = window.URL.createObjectURL(data);
  const a = document.createElement('a');
  a.href = url;
  a.download = fileName;
  a.click();
  window.URL.revokeObjectURL(url);
}



}



 
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { saveAs } from 'file-saver';
import { ReportService } from '../../../services/report.service';
 
@Component({
  selector: 'app-report',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.css']
})
export class ReportComponent {
  isLoading: boolean = false;
  currentReport: string = '';
 
  constructor(private reportService: ReportService) {}
 
  downloadReport(reportType: 'stock' | 'transaction' | 'production', fileType: 'pdf' | 'csv') {
    this.isLoading = true;
    this.currentReport = reportType;
 
    let filter: any = { categoryId: null, startDate: null, endDate: null };
    if (reportType === 'production') filter.status = null;
 
    let request$;
    if (reportType === 'stock') {
      request$ = this.reportService.downloadStockReport(fileType, filter);
    } else if (reportType === 'transaction') {
      request$ = this.reportService.downloadTransactionReport(fileType, filter);
    } else {
      request$ = this.reportService.downloadProductionReport(fileType, filter);
    }
 
    request$.subscribe({
      next: (blob: Blob) => {
        const fileName = `${reportType}_report.${fileType}`;
        saveAs(blob, fileName);
        this.isLoading = false;
      },
      error: (err) => {
        console.error(`❌ ${reportType} report download failed:`, err);
        alert(`Failed to download ${reportType} report! Please try again.`);
        this.isLoading = false;
      }
    });
  }
}
 
 
 
 
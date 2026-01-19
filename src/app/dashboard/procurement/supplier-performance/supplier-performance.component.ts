import { Component, OnDestroy } from '@angular/core';
import { SupplierReportService } from '../../../services/supplier-report.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Chart, ChartConfiguration, ChartOptions, registerables } from 'chart.js';

// Register Chart.js components globally
Chart.register(...registerables);

@Component({
  selector: 'app-supplier-performance',
  templateUrl: './supplier-performance.component.html',
  styleUrls: ['./supplier-performance.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class SupplierPerformanceComponent implements OnDestroy {
  Math = Math;

  selectedSupplierId?: number;
  supplierReport?: any;

  // Typed chart instances
  private barChart: Chart<'bar', number[], string> | null = null;
  private pieChart: Chart<'doughnut', number[], string> | null = null;
  private horizontalChart: Chart<'bar', number[], string> | null = null;

  constructor(private service: SupplierReportService) {}

  ngOnDestroy() {
    this.destroyCharts();
  }

  fetchReport() {
    if (!this.selectedSupplierId) return;

    this.service.getSupplierPerformance(this.selectedSupplierId).subscribe(data => {
      this.supplierReport = data;
      // wait for DOM update
      setTimeout(() => this.createCharts(), 0);
    });
  }

  private destroyCharts() {
    this.barChart?.destroy();
    this.pieChart?.destroy();
    this.horizontalChart?.destroy();

    this.barChart = this.pieChart = this.horizontalChart = null;
  }

  private createCharts() {
    this.destroyCharts();
    if (!this.supplierReport) return;

    const ratings = [1, 2, 3, 4, 5];
    const counts = ratings.map(r => this.supplierReport.ratingCounts[r] || 0);
    const percentages = ratings.map(r => this.supplierReport.ratingPercentages[r] || 0);

    this.createBarChart(counts);
    this.createPieChart(ratings, percentages);
    this.createHorizontalChart(percentages);
  }

  private createBarChart(counts: number[]) {
    const barCtx = document.getElementById('ratingBarChart') as HTMLCanvasElement;
    if (!barCtx) return;

    const config: ChartConfiguration<'bar', number[], string> = {
      type: 'bar',
      data: {
        labels: ['1 Star', '2 Stars', '3 Stars', '4 Stars', '5 Stars'],
        datasets: [{
          label: 'Number of Reviews',
          data: counts,
          backgroundColor: [
            'rgba(244, 67, 54, 0.7)',
            'rgba(255, 152, 0, 0.7)',
            'rgba(255, 193, 7, 0.7)',
            'rgba(139, 195, 74, 0.7)',
            'rgba(76, 175, 80, 0.7)'
          ],
          borderColor: [
            'rgba(244, 67, 54, 1)',
            'rgba(255, 152, 0, 1)',
            'rgba(255, 193, 7, 1)',
            'rgba(139, 195, 74, 1)',
            'rgba(76, 175, 80, 1)'
          ],
          borderWidth: 2,
          borderRadius: 8,
          borderSkipped: false
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: true,
        plugins: {
          legend: { display: false },
          tooltip: {
            backgroundColor: 'rgba(0, 0, 0, 0.8)',
            titleColor: '#fff',
            bodyColor: '#fff',
            padding: 12,
            displayColors: false,
            callbacks: {
              label: context => `Reviews: ${context.parsed.y}`
            }
          }
        },
        scales: {
          y: {
            beginAtZero: true,
            ticks: {
              stepSize: 1,
              font: { size: 12 }
            },
            grid: { color: 'rgba(0, 0, 0, 0.05)' }
          },
          x: {
            ticks: { font: { size: 12 } },
            grid: { display: false }
          }
        }
      }
    };

    this.barChart = new Chart<'bar', number[], string>(barCtx, config);
  }

  private createPieChart(ratings: number[], percentages: number[]) {
    const pieCtx = document.getElementById('ratingPieChart') as HTMLCanvasElement;
    if (!pieCtx) return;

    // filter zeros
    const filteredData = percentages.filter(p => p > 0);
    const filteredLabels = ratings
      .filter((r, i) => percentages[i] > 0)
      .map(r => `${r} Star${r > 1 ? 's' : ''}`);

    const colors = [
      'rgba(244, 67, 54, 0.8)',
      'rgba(255, 152, 0, 0.8)',
      'rgba(255, 193, 7, 0.8)',
      'rgba(139, 195, 74, 0.8)',
      'rgba(76, 175, 80, 0.8)'
    ];
    const filteredColors = ratings
      .map((r, i) => (percentages[i] > 0 ? colors[i] : null))
      .filter((c): c is string => c !== null);

    const options: ChartOptions<'doughnut'> = {
      responsive: true,
      maintainAspectRatio: true,
      plugins: {
        legend: {
          position: 'right',
          labels: {
            padding: 15,
            font: { size: 12 },
            usePointStyle: true,
            pointStyle: 'circle'
          }
        },
        tooltip: {
          backgroundColor: 'rgba(0, 0, 0, 0.8)',
          padding: 12,
          callbacks: {
            label: context => {
              if (filteredData.length === 0) return 'No reviews yet';
              return `${context.label}: ${context.parsed}%`;
            }
          }
        }
      }
    };

    const config: ChartConfiguration<'doughnut', number[], string> = {
      type: 'doughnut',
      data: {
        labels: filteredLabels.length ? filteredLabels : ['No Data'],
        datasets: [{
          data: filteredData.length ? filteredData : [1],
          backgroundColor: filteredData.length ? filteredColors : ['rgba(200, 200, 200, 0.5)'],
          borderColor: '#fff',
          borderWidth: 3
        }]
      },
      options
    };

    this.pieChart = new Chart<'doughnut', number[], string>(pieCtx, config);
  }

  private createHorizontalChart(percentages: number[]) {
    const horizontalCtx = document.getElementById('ratingHorizontalChart') as HTMLCanvasElement;
    if (!horizontalCtx) return;

    const config: ChartConfiguration<'bar', number[], string> = {
      type: 'bar',
      data: {
        labels: ['1 Star', '2 Stars', '3 Stars', '4 Stars', '5 Stars'],
        datasets: [{
          label: 'Percentage (%)',
          data: percentages,
          backgroundColor: 'rgba(30, 136, 229, 0.7)',
          borderColor: 'rgba(30, 136, 229, 1)',
          borderWidth: 2,
          borderRadius: 8,
          borderSkipped: false
        }]
      },
      options: {
        indexAxis: 'y',
        responsive: true,
        maintainAspectRatio: true,
        plugins: {
          legend: { display: false },
          tooltip: {
            backgroundColor: 'rgba(0, 0, 0, 0.8)',
            padding: 12,
            callbacks: {
              label: context => `${context.parsed.x}% of reviews`
            }
          }
        },
        scales: {
          x: {
            beginAtZero: true,
            max: 100,
            ticks: {
              font: { size: 12 },
              callback: value => value + '%'
            },
            grid: { color: 'rgba(0, 0, 0, 0.05)' }
          },
          y: {
            ticks: { font: { size: 12 } },
            grid: { display: false }
          }
        }
      }
    };

    this.horizontalChart = new Chart<'bar', number[], string>(horizontalCtx, config);
  }

  // -----------------------------
  // Download Methods (PDF / Excel)
  // -----------------------------

  downloadPdf() {
    if (!this.selectedSupplierId) return;

    this.service.downloadSupplierPdf(this.selectedSupplierId).subscribe({
      next: blob => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `supplier_performance_${this.selectedSupplierId}.pdf`;
        a.click();
        window.URL.revokeObjectURL(url);
      },
      error: error => {
        console.error('Error downloading PDF:', error);
        alert('Failed to download PDF. Please try again.');
      }
    });
  }

  downloadAllSuppliersPdf() {
    this.service.getAllSuppliersPdf().subscribe({
      next: blob => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'all_suppliers_report.pdf';
        a.click();
        window.URL.revokeObjectURL(url);
      },
      error: error => {
        console.error('Error downloading all suppliers PDF:', error);
        alert('Failed to download PDF. Please try again.');
      }
    });
  }

  downloadAllSuppliersExcel() {
    this.service.getAllSuppliersExcel().subscribe({
      next: blob => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'all_suppliers_report.xlsx';
        a.click();
        window.URL.revokeObjectURL(url);
      },
      error: error => {
        console.error('Error downloading all suppliers Excel:', error);
        alert('Failed to download Excel. Please try again.');
      }
    });
  }

  downloadExcel() {
    if (!this.selectedSupplierId) return;

    this.service.downloadSupplierExcel(this.selectedSupplierId).subscribe({
      next: blob => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `supplier_performance_${this.selectedSupplierId}.xlsx`;
        a.click();
        window.URL.revokeObjectURL(url);
      },
      error: error => {
        console.error('Error downloading Excel:', error);
        alert('Failed to download Excel file. Please try again.');
      }
    });
  }
}

import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgChartsModule } from 'ng2-charts';
import { ChartData, ChartOptions } from 'chart.js';
import { ProductionEfficiencyReportService } from '../../../services/production-efficiency-report.service';

// production-efficiency-report 
@Component({
  selector: 'app-production-efficiency-report',
  standalone: true,
  imports: [CommonModule, FormsModule, NgChartsModule],
  templateUrl: './production-efficiency-report.component.html',
})
export class ProductionEfficiencyReportComponent {

  unitId: number | null = null;
  startDate!: string;
  endDate!: string;

  report: any = null;
  allReports: any[] = [];
  loading = false;
  errorMessage = '';

  // Chart for single unit
  singleUnitChartData: ChartData<'bar'> = {
    labels: ['Planned Qty', 'Actual Qty'],
    datasets: [
      { label: 'Production Efficiency', data: [] }
    ]
  };

  singleUnitOptions: ChartOptions = { responsive: true };

  // Chart for all units
  allUnitsChartData: ChartData<'bar'> = {
    labels: [],
    datasets: [
      { label: 'Efficiency (%)', data: [] }
    ]
  };

  allUnitsOptions: ChartOptions = { responsive: true };

  constructor(private reportService: ProductionEfficiencyReportService) {}

  

  generateReport() {
    this.loading = true;

    this.reportService.getEfficiencyReport(
      this.unitId,
      this.startDate,
      this.endDate
    ).subscribe({
      next: (data) => {
        this.report = data;

        this.singleUnitChartData.datasets[0].data = [
          data.plannedQuantity, data.actualQuantity
        ];

        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'Error fetching report';
        this.loading = false;
      }
    });
  }

  generateAllReports() {
    this.loading = true;

    this.reportService.getAllUnitsReport(this.startDate, this.endDate)
      .subscribe({
        next: (data) => {
          this.allReports = data;

          this.allUnitsChartData.labels = data.map((u: any) => 'Unit ' + u.unitId);
          this.allUnitsChartData.datasets[0].data = data.map((u: any) => u.efficiencyPercentage);

          this.loading = false;
        },
        error: () => {
          this.errorMessage = 'Error loading data';
          this.loading = false;
        }
      });
  }
}

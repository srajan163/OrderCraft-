import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgChartsModule } from 'ng2-charts';
import { ChartData, ChartOptions } from 'chart.js';
import { ProductionAnalyticsService } from '../../../services/production-analytics.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-production-analytics',
  templateUrl: './production-analytics.component.html',
  standalone: true,
  imports: [CommonModule, FormsModule, NgChartsModule],
  styleUrls: ['./production-analytics.component.css']
})
export class ProductionAnalyticsComponent implements OnInit {

  summary: any;
  isLoading = true;
  errorMsg = '';

  // Pie chart configuration
  public pieChartData: ChartData<'pie', number[], string | string[]> = {
    labels: ['Completed', 'Hold', 'Delayed', 'Other'],
    datasets: [
      {
        data: [],
        backgroundColor: ['#16a34a', '#f59e0b', '#dc2626', '#1d4ed8']
      }
    ]
  };

  public pieChartOptions: ChartOptions<'pie'> = {
    responsive: true,
    plugins: {
      legend: { position: 'bottom' }
    }
  };

  public pieChartType: 'pie' = 'pie';

  constructor(private analyticsService: ProductionAnalyticsService) {}

  ngOnInit(): void {
    this.loadAnalytics();
  }
// all done and working 
  loadAnalytics(): void {
    this.isLoading = true;
    this.analyticsService.getProductionSummary().subscribe({
      next: (data) => {
        this.summary = data;

        const other = data.totalSchedules - (data.completedSchedules + data.holdSchedules + data.delayedSchedules);
        this.pieChartData.datasets[0].data = [
          data.completedSchedules,
          data.holdSchedules,
          data.delayedSchedules,
          other > 0 ? other : 0
        ];

        this.isLoading = false;
      },
      error: (err) => {
        console.error('Analytics load error:', err);
        this.errorMsg = 'Failed to load analytics data';
        this.isLoading = false;
      }
    });
  }
}

import { Component, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TrendAnalysisReportService } from '../../../services/trend-analysis-report.service';
import { Chart, registerables } from 'chart.js';

Chart.register(...registerables);

// trend analysis

@Component({
  selector: 'app-trend-analysis',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './trend-analysis-report.component.html',
  styleUrls: ['./trend-analysis-report.component.css']
})
export class TrendAnalysisComponent implements OnDestroy {

  metric = '';
  startDate = '';
  endDate = '';
  reportData: any;

  private lineChart: any;
  private pieChart: any;

  constructor(private trendService: TrendAnalysisReportService) {}

  ngOnDestroy() {
    this.destroyCharts();
  }

  objectKeys(obj: any) {
    return Object.keys(obj);
  }

  generateReport() {
    this.trendService.getTrendReport(this.metric, this.startDate, this.endDate)
      .subscribe({
        next: res => {
          this.reportData = res;
          setTimeout(() => this.createCharts(), 0);
        },
        error: err => alert("Failed to fetch report")
      });
  }

  private destroyCharts() {
    this.lineChart?.destroy();
    this.pieChart?.destroy();
  }

  private createCharts() {
    this.destroyCharts();

    if (!this.reportData?.trendValues) return;

    const labels = this.reportData.labels || ['A','B','C','D','E'];
    const values = this.reportData.trendValues || [5,10,7,12,9];

    // Line Chart
    const lineCtx = document.getElementById('trendLineChart') as HTMLCanvasElement;
    this.lineChart = new Chart(lineCtx, {
      type: 'line',
      data: {
        labels,
        datasets: [{
          label: 'Trend',
          data: values,
          borderColor: '#1e88e5',
          backgroundColor: 'rgba(30,136,229,0.2)',
          borderWidth: 3,
          fill: true,
          tension: .3
        }]
      },
      options: { responsive: true }
    });

    // Pie Chart
    const pieCtx = document.getElementById('trendPieChart') as HTMLCanvasElement;
    this.pieChart = new Chart(pieCtx, {
      type: 'doughnut',
      data: {
        labels,
        datasets: [{
          data: values,
          backgroundColor: [
            '#1e88e5','#43a047','#fb8c00','#e53935','#8e24aa'
          ]
        }]
      }
    });
  }
}

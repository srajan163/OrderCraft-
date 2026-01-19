import { Component, OnInit } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { Router } from '@angular/router';
import { ProductionTimelineService, ProductionTimelineDto } from '../../../services/production-timeline.service';
import { NgIf, NgFor } from '@angular/common';

@Component({
  selector: 'app-production-timeline',
  standalone: true,
  imports: [CommonModule, NgIf, NgFor, DatePipe],
  templateUrl: './production-timeline.component.html',
  styleUrls: ['./production-timeline.component.css']
})
export class ProductionTimelineComponent implements OnInit {
  timelines: { psId: number; productName?: string; startDate?: string; endDate?: string; status?: string; dto?: ProductionTimelineDto }[] = [];
  isLoading = false;
  error = '';

  constructor(
    private svc: ProductionTimelineService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.isLoading = true;
    this.error = '';
    this.svc.getAll().subscribe({
      next: (data) => {
        // Backend returns DTO per schedule — your backend currently maps schedule -> DTO
        // For list view we may display each DTO as a row. If your backend returns additional fields, adapt mapping below.
        this.timelines = data.map((d, i) => ({
          psId: i + 1, // fallback id if backend doesn't provide psId in DTO
          productName: d.remarks ?? 'Product',
          startDate: d.updatedAt ?? '',
          endDate: '',
          status: d.status,
          dto: d
        }));
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Failed to load production timelines', err);
        this.error = 'Failed to load production timelines';
        this.isLoading = false;
      }
    });
  }

  viewDetails(psId: number | string) {
    // navigate to child detail route under production
    this.router.navigate(['/production', 'timeline', psId]);
  }
}

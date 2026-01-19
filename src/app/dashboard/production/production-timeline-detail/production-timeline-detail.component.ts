import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductionTimelineService, ProductionTimelineDto } from '../../../services/production-timeline.service';
import { NgIf, NgFor } from '@angular/common';

@Component({
  selector: 'app-production-timeline-detail',
  standalone: true,
  imports: [CommonModule, NgIf, NgFor],
  templateUrl: './production-timeline-detail.component.html',
  styleUrls: ['./production-timeline-detail.component.css']
})
export class ProductionTimelineDetailComponent implements OnInit {
  psId!: number;
  timeline: ProductionTimelineDto[] = [];
  isLoading = false;
  error = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private svc: ProductionTimelineService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    this.psId = id ? Number(id) : NaN;
    if (isNaN(this.psId)) {
      this.error = 'Invalid schedule id';
      return;
    }
    this.load();
  }

  load(): void {
    this.isLoading = true;
    this.error = '';
    this.svc.getByPsId(this.psId).subscribe({
      next: (data) => {
        this.timeline = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Failed to load timeline detail', err);
        this.error = 'Failed to load timeline details';
        this.isLoading = false;
      }
    });
  }

  goBack() {
    this.router.navigate(['/production', 'timeline']);
  }
}

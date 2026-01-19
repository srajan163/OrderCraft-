import { TestBed } from '@angular/core/testing';

import { TrendAnalysisReportService } from './trend-analysis-report.service';

// Trend analysis service

describe('TrendAnalysisReportService', () => {
  let service: TrendAnalysisReportService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TrendAnalysisReportService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

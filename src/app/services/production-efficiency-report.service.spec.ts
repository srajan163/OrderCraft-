import { TestBed } from '@angular/core/testing';

import { ProductionEfficiencyReportService } from './production-efficiency-report.service';

describe('ProductionEfficiencyReportService', () => {
  let service: ProductionEfficiencyReportService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProductionEfficiencyReportService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

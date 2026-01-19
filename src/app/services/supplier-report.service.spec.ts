import { TestBed } from '@angular/core/testing';

import { SupplierReportService } from './supplier-report.service';

describe('SupplierReportService', () => {
  let service: SupplierReportService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SupplierReportService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

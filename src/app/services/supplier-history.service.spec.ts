import { TestBed } from '@angular/core/testing';

import { SupplierHistoryService } from './supplier-history.service';

describe('SupplierHistoryService', () => {
  let service: SupplierHistoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SupplierHistoryService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

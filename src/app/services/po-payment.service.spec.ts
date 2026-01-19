import { TestBed } from '@angular/core/testing';

import { PoPaymentService } from './po-payment.service';

describe('PoPaymentService', () => {
  let service: PoPaymentService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PoPaymentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

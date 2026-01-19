import { TestBed } from '@angular/core/testing';

import { TrackProductionService } from './track-production.service';

describe('TrackProductionService', () => {
  let service: TrackProductionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TrackProductionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

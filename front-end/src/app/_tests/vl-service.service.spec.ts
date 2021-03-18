import {TestBed} from '@angular/core/testing';

import {VlServiceService} from '../services/vl-service.service';

describe('VlServiceService', () => {
  let service: VlServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VlServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

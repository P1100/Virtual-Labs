import {TestBed} from '@angular/core/testing';

import {BackendService} from '../app/services/backend.service';
import {HttpClientModule} from '@angular/common/http';

// Needed to add import of HttpClientModule to make it work
describe('BackendService', () => {
  let service: BackendService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [BackendService],
      imports: [HttpClientModule]
    });
    service = TestBed.inject(BackendService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

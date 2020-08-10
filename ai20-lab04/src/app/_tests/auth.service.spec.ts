import {TestBed} from '@angular/core/testing';

import {AuthService} from '../services/auth.service';
import {HttpClientModule} from '@angular/common/http';

// Needed to add import of HttpClientModule to make it work
describe('AuthService', () => {
  let service: AuthService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AuthService],
      imports: [HttpClientModule]
    });
    service = TestBed.inject(AuthService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

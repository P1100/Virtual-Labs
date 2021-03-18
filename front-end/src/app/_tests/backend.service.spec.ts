import {TestBed} from '@angular/core/testing';

import {StudentService} from '../services/student.service';
import {HttpClientModule} from '@angular/common/http';

// Needed to add import of HttpClientModule to make it work
describe('StudentService', () => {
  let service: StudentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [StudentService],
      imports: [HttpClientModule]
    });
    service = TestBed.inject(StudentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

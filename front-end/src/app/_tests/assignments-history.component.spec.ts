import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {AssignmentsHistoryComponent} from './assignments-history.component';

describe('AssignmentsHistoryComponent', () => {
  let component: AssignmentsHistoryComponent;
  let fixture: ComponentFixture<AssignmentsHistoryComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [AssignmentsHistoryComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AssignmentsHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AssignmentsHistoryComponent } from './assignments-history.component';

describe('AssignmentsHistoryComponent', () => {
  let component: AssignmentsHistoryComponent;
  let fixture: ComponentFixture<AssignmentsHistoryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AssignmentsHistoryComponent ]
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

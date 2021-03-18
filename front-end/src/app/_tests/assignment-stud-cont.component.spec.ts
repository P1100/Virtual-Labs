import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {AssignmentStudContComponent} from './assignment-stud-cont.component';

describe('AssignmentStudContComponent', () => {
  let component: AssignmentStudContComponent;
  let fixture: ComponentFixture<AssignmentStudContComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [AssignmentStudContComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AssignmentStudContComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

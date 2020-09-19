import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {AssignmentStudContComponent} from './assignment-stud-cont.component';

describe('AssignmentStudContComponent', () => {
  let component: AssignmentStudContComponent;
  let fixture: ComponentFixture<AssignmentStudContComponent>;

  beforeEach(async(() => {
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

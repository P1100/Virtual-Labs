import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {AssignmentProfContComponent} from './assignment-prof-cont.component';

describe('AssignmentProfContComponent', () => {
  let component: AssignmentProfContComponent;
  let fixture: ComponentFixture<AssignmentProfContComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [AssignmentProfContComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AssignmentProfContComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {AssignmentStudComponent} from './assignment-stud.component';

describe('AssignmentStudComponent', () => {
  let component: AssignmentStudComponent;
  let fixture: ComponentFixture<AssignmentStudComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [AssignmentStudComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AssignmentStudComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

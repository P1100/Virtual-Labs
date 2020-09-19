import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {AssignmentProfComponent} from './assignment-prof.component';

describe('AssignmentProfComponent', () => {
  let component: AssignmentProfComponent;
  let fixture: ComponentFixture<AssignmentProfComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [AssignmentProfComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AssignmentProfComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {EditRemoveCourseComponent} from './edit-remove-course.component';

describe('EditRemoveCourseComponent', () => {
  let component: EditRemoveCourseComponent;
  let fixture: ComponentFixture<EditRemoveCourseComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [EditRemoveCourseComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditRemoveCourseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

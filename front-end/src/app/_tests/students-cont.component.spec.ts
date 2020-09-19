import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {StudentsContComponent} from '../r2-inner-tab/professor/students/students-cont.component';
import {RouterTestingModule} from '@angular/router/testing';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('StudentsContComponent', () => {
  let component: StudentsContComponent;
  let fixture: ComponentFixture<StudentsContComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [StudentsContComponent],
      imports: [RouterTestingModule, HttpClientTestingModule]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StudentsContComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

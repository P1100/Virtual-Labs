import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {VmsStudContComponent} from '../r2-inner-tab/student/vms/vms-stud-cont.component';

describe('VmsContComponent', () => {
  let component: VmsStudContComponent;
  let fixture: ComponentFixture<VmsStudContComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [VmsStudContComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VmsStudContComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

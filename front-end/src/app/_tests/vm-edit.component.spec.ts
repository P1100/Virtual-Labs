import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {VmEditComponent} from './vm-edit.component';

describe('VmEditComponent', () => {
  let component: VmEditComponent;
  let fixture: ComponentFixture<VmEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [VmEditComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VmEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

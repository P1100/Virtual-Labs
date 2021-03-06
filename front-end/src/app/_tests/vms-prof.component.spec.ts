import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {VmsProfComponent} from '../r2-inner-tab/professor/vms-prof/vms-prof.component';

describe('VmsProfComponent', () => {
  let component: VmsProfComponent;
  let fixture: ComponentFixture<VmsProfComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [VmsProfComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VmsProfComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

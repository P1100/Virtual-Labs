import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {VmCreateComponent} from '../dialogs/vm-create/vm-create.component';

describe('VmCreateComponent', () => {
  let component: VmCreateComponent;
  let fixture: ComponentFixture<VmCreateComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [VmCreateComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VmCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

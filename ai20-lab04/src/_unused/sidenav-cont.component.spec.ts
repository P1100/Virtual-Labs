import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {SidenavContComponent} from '../app/home/sidenav-cont.component';

describe('SidenavContComponent', () => {
  let component: SidenavContComponent;
  let fixture: ComponentFixture<SidenavContComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [SidenavContComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SidenavContComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

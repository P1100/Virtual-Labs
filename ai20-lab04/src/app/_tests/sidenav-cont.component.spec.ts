import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {SidenavContentComponent} from '../r1-tabs/sidenav-content.component';
import {RouterTestingModule} from '@angular/router/testing';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('SidenavContComponent', () => {
  let component: SidenavContentComponent;
  let fixture: ComponentFixture<SidenavContentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [SidenavContentComponent],
      imports: [RouterTestingModule, HttpClientTestingModule]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SidenavContentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

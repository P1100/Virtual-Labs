import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {TabsNavComponent} from '../r1-tabs-menu/tabs-nav.component';
import {RouterTestingModule} from '@angular/router/testing';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('SidenavContComponent', () => {
  let component: TabsNavComponent;
  let fixture: ComponentFixture<TabsNavComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [TabsNavComponent],
      imports: [RouterTestingModule, HttpClientTestingModule]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TabsNavComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

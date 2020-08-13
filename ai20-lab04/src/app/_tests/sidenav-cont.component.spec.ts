import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {TabsMenuComponent} from '../r1-content/tabs-menu.component';
import {RouterTestingModule} from '@angular/router/testing';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('SidenavContComponent', () => {
  let component: TabsMenuComponent;
  let fixture: ComponentFixture<TabsMenuComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [TabsMenuComponent],
      imports: [RouterTestingModule, HttpClientTestingModule]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TabsMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

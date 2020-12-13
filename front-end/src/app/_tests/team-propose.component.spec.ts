import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {TeamProposeComponent} from '../dialogs/team-propose/team-propose.component';

describe('TeamProposeComponent', () => {
  let component: TeamProposeComponent;
  let fixture: ComponentFixture<TeamProposeComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [TeamProposeComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TeamProposeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

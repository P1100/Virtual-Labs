import {Component, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {AlertsService} from '../../../services/alerts.service';
import {CourseService} from '../../../services/course.service';
import {Team} from '../../../models/team.model';
import {Subscription} from 'rxjs';
import {VlServiceService} from '../../../services/vl-service.service';

@Component({
  selector: 'app-vms-prof-cont',
  template: `
    <app-vms-prof (forceRefreshData)="onForceRefreshData($event)"
                  [courseId]="courseId" [idStringLoggedStudent]="idStringLoggedStudent"
                  [teams]="teams"
    >
    </app-vms-prof>
  `,
  styleUrls: []
})
export class VmsProfContComponent implements OnDestroy {
  // enrolledWithoutTeams: Student[] = []; // always includes logged user
  courseId = '0';
  // subEnrolledWithTeams: Subscription = null;
  subRouteParam: Subscription = null;
  // subCurrentCourse: Subscription;
  idStringLoggedStudent: string;
  // activeTeam: Team = null;
  // hideAllGUItillActiveTeamIsChecked = true; // to avoid loading flicker
  teams: Team[];

  constructor(private courseService: CourseService, private activatedRoute: ActivatedRoute, private alertsService: AlertsService,
              private vlServiceService: VlServiceService) {
    this.idStringLoggedStudent = localStorage.getItem('id');
    this.subRouteParam = this.activatedRoute.paramMap.subscribe(() => {
        this.courseId = this.activatedRoute.parent.snapshot.paramMap.get('id');
        this.onForceRefreshData(null);
      }
    );
  }
  ngOnDestroy(): void {
    this.subRouteParam?.unsubscribe();
    // this.subEnrolledWithTeams?.unsubscribe();
    // this.subCurrentCourse?.unsubscribe();
  }
  onForceRefreshData($event: any) {
    this.vlServiceService.getActiveTeamForCourse(this.courseId).subscribe(teams => {
        this.teams = teams;
      },
      error => this.alertsService.setAlert('danger', 'Couldn\'t get course teams! ' + error)
    );
  }
}

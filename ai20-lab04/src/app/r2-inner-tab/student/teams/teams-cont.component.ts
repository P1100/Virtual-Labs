import {Component, OnDestroy} from '@angular/core';
import {Student} from '../../../models/student.model';
import {Subscription} from 'rxjs';
import {ActivatedRoute} from '@angular/router';
import {AlertsService} from '../../../services/alerts.service';
import {CourseService} from '../../../services/course.service';
import {VlServiceService} from '../../../services/vl-service.service';
import {Team} from '../../../models/team.model';

@Component({
  selector: 'app-teams-cont',
  template: `
    <app-teams [enrolledWithoutTeams]="enrolledWithoutTeams" (forceUploadData)="onForceUploadData($event)"
               [courseId]="courseId" [courseMin]="courseMin" [courseMax]="courseMax"
               [activeTeam]="activeTeam" [notActiveTeams]="notActiveTeams" [hideAllGUItillActiveTeamIsChecked]="hideAllGUItillActiveTeamIsChecked"
    [idStringLoggedStudent]="idStringLoggedStudent">
    </app-teams>
  `,
  styleUrls: []
})
export class TeamsContComponent implements OnDestroy {
  enrolledWithoutTeams: Student[] = []; // always includes logged user
  courseId = '0';
  subEnrolledWithTeams: Subscription = null;
  subRouteParam: Subscription = null;
  subCurrentCourse: Subscription;
  courseMin: number;
  courseMax: number;
  idStringLoggedStudent: string;
  activeTeam: Team = null;
  notActiveTeams: Team[];
  hideAllGUItillActiveTeamIsChecked = true; // to avoid loading flicker

  constructor(private courseService: CourseService, private activatedRoute: ActivatedRoute, private alertsService: AlertsService,
              private vlServiceService: VlServiceService) {
    this.idStringLoggedStudent = localStorage.getItem('id');
    this.subRouteParam = this.activatedRoute.paramMap.subscribe(() => {
        this.courseId = this.activatedRoute.parent.snapshot.paramMap.get('id');
        this.onForceUploadData(null);
      }
    );
  }
  ngOnDestroy(): void {
    this.subRouteParam?.unsubscribe();
    this.subEnrolledWithTeams?.unsubscribe();
    this.subCurrentCourse?.unsubscribe();
  }
  onForceUploadData($event: any) {
    this.subEnrolledWithTeams = this.courseService.getEnrolledWithoutTeam(this.courseId).subscribe((students: Student[]) => {
        this.enrolledWithoutTeams = Array.isArray(students) ? [...students] : [];
      }, error => this.alertsService.setAlert('danger', 'Couldn\'t get enrolled without team! ' + error)
    );
    this.vlServiceService.getTeamsUser(+this.idStringLoggedStudent, this.courseId).subscribe(teams => {
        let countActive = 0;
        this.activeTeam = null;
        this.notActiveTeams = [];
        for (let team of teams) {
          if (team.active == true) {
            this.activeTeam = team;
            countActive++;
          } else {
            this.notActiveTeams.push(team);
          }
        }
        if (countActive > 1) {
          this.activeTeam = undefined;
          this.alertsService.setAlert('danger', 'Error! Multiple active teams for the student, please concat the administrator');
          throw new Error('Corrupted Team data: ' + JSON.stringify(teams));
        }
        this.hideAllGUItillActiveTeamIsChecked = false;
      }, error => this.alertsService.setAlert('danger', 'Couldn\'t get student teams! ' + error)
    );
    this.subCurrentCourse = this.courseService.getCourse(this.courseId).subscribe(c => {
      this.courseMin = c[0].minSizeTeam;
      this.courseMax = c[0].maxSizeTeam;
      if (this.courseMin >= this.courseMax) {
        this.alertsService.setAlert('danger', 'Error! Invalid courses team constrains, please concat the administrator');
        throw new Error('Error! Invalid courses team constrains, please concat the administrator');
      }
    }, error => this.alertsService.setAlert('danger', 'Couldn\'t get course info! ' + error));
  }
}

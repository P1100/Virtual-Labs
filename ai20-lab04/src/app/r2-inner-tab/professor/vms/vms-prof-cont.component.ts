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
    <app-vms-prof [courseId]="courseId" [idStringLoggedStudent]="idStringLoggedStudent"
                  [teams]="teams"
    >
    </app-vms-prof>
  `,
  styleUrls: []
})
export class VmsProfContComponent implements OnDestroy {
  courseId = '0';
  subRouteParam: Subscription = null;
  idStringLoggedStudent: string;
  teams: Team[];

  constructor(private courseService: CourseService, private activatedRoute: ActivatedRoute, private alertsService: AlertsService,
              private vlServiceService: VlServiceService) {
    this.idStringLoggedStudent = localStorage.getItem('id');
    this.subRouteParam = this.activatedRoute.paramMap.subscribe(() => {
        this.courseId = this.activatedRoute.parent.snapshot.paramMap.get('id');
        this.vlServiceService.getActiveTeamForCourse(this.courseId).subscribe(teams => {
            this.teams = teams;
          },
          error => this.alertsService.setAlert('danger', 'Couldn\'t get course teams! ' + error)
        );
      }
    );
  }
  ngOnDestroy(): void {
    this.subRouteParam?.unsubscribe();
  }
}

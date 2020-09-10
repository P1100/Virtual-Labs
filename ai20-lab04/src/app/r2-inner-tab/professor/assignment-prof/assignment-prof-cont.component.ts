import {Component, OnDestroy} from '@angular/core';
import {Subscription} from 'rxjs';
import {CourseService} from '../../../services/course.service';
import {ActivatedRoute} from '@angular/router';
import {AlertsService} from '../../../services/alerts.service';
import {VlServiceService} from '../../../services/vl-service.service';
import {Assignment} from '../../../models/assignment.model';

@Component({
  selector: 'app-assignment-prof-cont',
  template: `
    <app-assignment-prof [assignments]="assignments"
    >
    </app-assignment-prof>
  `,
  styleUrls: []
})
export class AssignmentProfContComponent implements OnDestroy {
  courseId = '0';
  subRouteParam: Subscription = null;
  idStringLoggedStudent: string;
  assignments: Assignment[];
  assignmentsSub: Subscription;

  constructor(private courseService: CourseService, private activatedRoute: ActivatedRoute, private alertsService: AlertsService,
              private vlServiceService: VlServiceService) {
    this.subRouteParam = this.activatedRoute.paramMap.subscribe(() => {
        this.courseId = this.activatedRoute.parent.snapshot.paramMap.get('id');
        this.onForceRefreshData(null);
      }
    );
  }
  ngOnDestroy(): void {
    this.subRouteParam?.unsubscribe();
    this.assignmentsSub.unsubscribe();
  }
  onForceRefreshData(event: any) {
    this.assignmentsSub = this.vlServiceService.getAssignments(this.courseId).subscribe((assignments: Assignment[]) => {
        this.assignments = assignments;
      },
      error => this.alertsService.setAlert('danger', 'Couldn\'t get assignments! ' + error)
    );
  }
}

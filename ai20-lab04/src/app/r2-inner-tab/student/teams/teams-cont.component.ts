import {Component, OnDestroy} from '@angular/core';
import {Student} from '../../../models/student.model';
import {Observable, Subscription} from 'rxjs';
import {ActivatedRoute} from '@angular/router';
import {AlertsService} from '../../../services/alerts.service';
import {CourseService} from '../../../services/course.service';

@Component({
  selector: 'app-teams-cont',
  template: `
    <app-teams [enrolledWithoutTeams]="enrolledWithoutTeams"
               (forceUploadData)="onForceUploadData($event)">
      <!--                  [students]="allStudents"-->
      <!--                  (enrolledEvent)="onStudentsToEnroll($event)"-->
      <!--                  (disenrolledEvent)="onStudentsToDisenroll($event)"-->
      <!--                  (uploadCsvEvent)="onCsvUpload($event)"-->
    </app-teams>
  `,
  styleUrls: []
})
export class TeamsContComponent implements OnDestroy {
  enrolledWithoutTeams: Student[] = [];
  courseId = '0';
  private observableEnrolledWithoutTeam: Observable<Student[]>;
  subEnrolledWithTeams: Subscription = null;
  subRouteParam: Subscription = null;

  constructor(private courseService: CourseService, private activatedRoute: ActivatedRoute, private alertsService: AlertsService) {
    this.subRouteParam = this.activatedRoute.paramMap.subscribe(() => {
        this.courseId = this.activatedRoute.parent.snapshot.paramMap.get('id');
        this.observableEnrolledWithoutTeam = this.courseService.getEnrolledWithoutTeam(this.courseId); // reusable only for the same course id
        this.subEnrolledWithTeams = this.observableEnrolledWithoutTeam
          .subscribe((students: Student[]) => {
              console.log('observableEnrolledWithoutTeam init', students);
              this.enrolledWithoutTeams = Array.isArray(students) ? [...students] : [];
            }, error => this.alertsService.setAlert('danger', 'Couldn\'t get enrolled without team! ' + error)
          );
      }
    );
  }
  ngOnDestroy(): void {
    this.subRouteParam?.unsubscribe();
    this.subEnrolledWithTeams?.unsubscribe();
  }
  onForceUploadData($event: any) {
    this.observableEnrolledWithoutTeam.subscribe((students: Student[]) => {
        console.log('observableEnrolledWithoutTeam update', students);
        this.enrolledWithoutTeams = Array.isArray(students) ? [...students] : [];
      }, error => this.alertsService.setAlert('danger', 'Couldn\'t get enrolled without team! ' + error)
    );
  }
}

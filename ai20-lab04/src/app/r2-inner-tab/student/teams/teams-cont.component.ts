import {Component, OnDestroy} from '@angular/core';
import {Student} from '../../../models/student.model';
import {Observable, Subscription} from 'rxjs';
import {ActivatedRoute} from '@angular/router';
import {AlertsService} from '../../../services/alerts.service';
import {CourseService} from '../../../services/course.service';

@Component({
  selector: 'app-teams-cont',
  template: `
    <app-teams [enrolledWithoutTeams]="enrolledWithoutTeams" (forceUploadData)="onForceUploadData($event)"
               [courseId]="courseId" [courseMin]="courseMin" [courseMax]="courseMax">
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
  private subCurrentCourse: Subscription;
  courseMin: number;
  courseMax: number;

  constructor(private courseService: CourseService, private activatedRoute: ActivatedRoute, private alertsService: AlertsService) {
    this.subRouteParam = this.activatedRoute.paramMap.subscribe(() => {
        this.courseId = this.activatedRoute.parent.snapshot.paramMap.get('id');
        console.log('COURSE ID', this.courseId);
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
    this.subEnrolledWithTeams = this.courseService.getEnrolledWithoutTeam(this.courseId)
      .subscribe((students: Student[]) => {
          console.log('observableEnrolledWithoutTeam init', students);
          this.enrolledWithoutTeams = Array.isArray(students) ? [...students] : [];
        }, error => this.alertsService.setAlert('danger', 'Couldn\'t get enrolled without team! ' + error)
      );
    this.subCurrentCourse = this.courseService.getCourse(this.courseId).subscribe(c => {
      console.log('getCourse team', c, c[0]);
      this.courseMin = c[0].minSizeTeam;
      this.courseMax = c[0].maxSizeTeam;
    });
  }
}

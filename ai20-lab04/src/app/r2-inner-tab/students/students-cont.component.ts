import {Component, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {concatMap, toArray} from 'rxjs/operators';
import {BackendService} from '../../services/backend.service';
import {from, Observable, Subscription} from 'rxjs';
import {Student} from '../../models/student.model';
import {getSafeDeepCopyArray} from '../../app-settings';

/* API:
* - Data taken from backend service: one new request each time active route changes.
* --- Then passed to student component by html-template property binding
* - Angular events are used to receive commands from the child student component
*/
@Component({
  selector: 'app-students-cont',
  template: `
    <app-students [enrolled]="enrolledStudents"
                  [students]="allStudents"
                  (enrolledEvent)="onStudentsToEnroll($event)"
                  (disenrolledEvent)="onStudentsToDisenroll($event)"
    ></app-students>
  `,
  styleUrls: []
})
export class StudentsContComponent implements OnDestroy {
  // updated after every route change, inside constructor
  allStudents: Student[] = [];
  enrolledStudents: Student[] = [];
  courseId = '0';
  // constructor subscriptions to paramMap
  subAllStudents: Subscription = null;
  subEnrolledStudentsCourse: Subscription = null;
  subRouteParam: Subscription = null;

  // Routing change update (e.g. when changing course)
  constructor(private backendService: BackendService, private activatedRoute: ActivatedRoute) {
    this.subRouteParam = this.activatedRoute.paramMap.subscribe(() => {
        this.courseId = this.activatedRoute.parent.snapshot.paramMap.get('id');
        console.log('activeCourse: ' + this.courseId);
        this.subEnrolledStudentsCourse = this.backendService.getEnrolledStudents(this.courseId)
          .subscribe((students: Student[]) => {
            this.enrolledStudents = Array.isArray(students) ? [...students] : [];
          });
        this.subAllStudents = this.backendService.getAllStudents()
          .subscribe((students: Student[]) => {
            this.allStudents = Array.isArray(students) ? [...students] : [];
          });
      }
    );
  }
  ngOnDestroy(): void {
    this.subRouteParam.unsubscribe();
    this.subAllStudents.unsubscribe();
    this.subEnrolledStudentsCourse.unsubscribe();
  }

  onStudentsToEnroll(studentsToEnroll: Student[]) {
    if (studentsToEnroll === null || studentsToEnroll.length === 0) {
      return;
    }
    console.log('onStudentsToEnroll', studentsToEnroll);
    const observable: Observable<Student[]> = from([...studentsToEnroll])
      .pipe(
        concatMap((student: Student) =>
          this.backendService.enroll(student, this.courseId) as Observable<any>
        ),
        toArray()  // so it waits for all inner observables to collect
      ) as Observable<any>;
    this.updateEnrolledStudents(observable);
  }
  onStudentsToDisenroll(studentsToDisenroll: Student[]) {
    if (studentsToDisenroll === null || studentsToDisenroll.length === 0) {
      return;
    }
    const observable: Observable<Student[]> = from(studentsToDisenroll).pipe(
      concatMap((student: Student) =>
        this.backendService.disenroll(student, this.courseId) as Observable<any>
      ),
      toArray()  // so it waits for all inner observables to collect
    ) as Observable<any>;
    this.updateEnrolledStudents(observable);
  }

  private updateEnrolledStudents(o: Observable<Student[]>) {
    o.subscribe(array => {
        this.backendService.getEnrolledStudents(this.courseId).subscribe(
          (ss: Student[]) => {
            this.enrolledStudents = getSafeDeepCopyArray(ss);
          }
        );
      }
    );
  }
}

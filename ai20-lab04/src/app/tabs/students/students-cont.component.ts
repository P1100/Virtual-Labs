import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {concatMap, tap, toArray} from 'rxjs/operators';
import {BackendService} from '../../services/backend.service';
import {from, Observable, Subscription} from 'rxjs';
import {Student} from '../../models/student.model';

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
export class StudentsContComponent implements OnInit, OnDestroy {
  // updated after every route change, inside constructor
  allStudents: Student[] = [];
  enrolledStudents: Student[] = [];
  courseId = 0;
  // constructor subscriptions to Observable<ParamMap> of ActivatedRoute.paramMap()
  subAllStudents: Subscription = null;
  subEnrolledStudentsCourse: Subscription = null;
  subRouteParam: Subscription = null;

  // update students and enrolled on routing change (executed once, e.g. when changing course for the tab student)
  constructor(private backendService: BackendService, private activatedRoute: ActivatedRoute) {
    this.subRouteParam = this.activatedRoute.paramMap.subscribe(url => {
        this.courseId = +this.activatedRoute.parent.snapshot.paramMap.get('id');
        console.log('activeCourse: ' + this.courseId);
        this.subAllStudents = this.backendService.getAllStudents()
          .subscribe(
            (students: Student[]) => {
              console.log('---- allStudents', students);
              this.allStudents = [...(students || [])];
              console.log('Subscription allStudents:', this.enrolledStudents);
            });
        this.subEnrolledStudentsCourse = this.backendService.getEnrolledStudents(this.courseId)
          .subscribe((
            students: Student[]) => {
            console.log('---- enrolledStudents', students);
            this.enrolledStudents = [...(students || [])];
            console.log('Subscription enrolledStudents:', this.enrolledStudents);
          });
      }
    );
  }
  ngOnInit(): void {
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
    const observable: Observable<Student[]> = from(studentsToEnroll) // Observable<ObservedValueOf<Student[]>>
      .pipe(
        tap(student => console.log('ConcatMap pipe in. Current student:', student)),
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
    const observable: Observable<Student[]> = from(studentsToDisenroll).pipe( // Observable<ObservedValueOf<Student[]>>
      tap(student => console.log('ConcatMap pipe in. Current student:', student)),
      concatMap((student: Student) =>
        this.backendService.disenroll(student, this.courseId) as Observable<any>
      ),
      toArray()  // so it waits for all inner observables to collect
    ) as Observable<any>;
    this.updateEnrolledStudents(observable);
  }
  // I need to force the update on the tab (modified data, EnrolledStudents in this case). Executed once
  private updateEnrolledStudents(o: Observable<Student[]>) {
    o.subscribe(array => {
        console.log('onEnroll update date subscribe outer: ', array);
        this.backendService.getEnrolledStudents(this.courseId).subscribe(
          (ss: Student[]) => {
            this.enrolledStudents = [...(ss || [])];
            console.log('onEnroll date subscribe inner: ', ss);
          }
        );
      }
    );
  }
}

// TODO: muovere indietro a teacher folder prima di consegna
import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {concatMap, tap, toArray} from 'rxjs/operators';
import {BackendService} from '../../services/backend.service';
import {from, Observable, Subscription} from 'rxjs';
import {Student} from '../../models/student.model';

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
  // allStudents & enrolledStudents are updated: 1) when routing changes 2) when there are operations that modify these arrays
  allStudents: Student[] = [];
  enrolledStudents: Student[] = []; // TODO: enrolledStudents should be course dependent
  subAllStudents: Subscription;
  subEnrolledStudentsCourse: Subscription = null;
  courseId = 1;
  private paramSubscription: Subscription;

  // update students and enrolled on routing change (executed once, e.g. when changing course for the tab student)
  constructor(private backendService: BackendService, private route: ActivatedRoute) {
    // this.paramSubscription = this.route.url.subscribe(url => { });
    this.paramSubscription = this.route.paramMap.subscribe(url => {
        this.courseId = +this.route.parent.snapshot.paramMap.get('id');
        console.log('student-cont route.paramMap activeCourse: ' + this.courseId);
        this.subAllStudents = this.backendService.getAllStudents()
          .subscribe(
            (students: Student[]) => {
              this.allStudents = [...students];
              console.log('Subscription allStudents:', this.enrolledStudents);
            });
        this.subEnrolledStudentsCourse = this.backendService.getEnrolledStudents(this.courseId)
          .subscribe((
            students: Student[]) => {
            this.enrolledStudents = [...students];
            console.log('Subscription enrolledStudents:', this.enrolledStudents);
          });
      }
    );
  }
  ngOnInit(): void {
    console.log('student-cont ngOnInit');
  }
  ngOnDestroy(): void {
    console.log('student-cont ngOnDestroy');
    this.paramSubscription.unsubscribe();
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
          this.backendService.enroll(student, this.courseId) as Observable<any> // this.httpClient.get(`item/${student}`))
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
        this.backendService.disenroll(student, this.courseId) as Observable<any> // this.httpClient.get(`item/${student}`))
      ),
      toArray()  // so it waits for all inner observables to collect
    ) as Observable<any>;
    this.updateEnrolledStudents(observable);
  }
  // I need to force the update on the tab modified data, which is EnrolledStudents in this case
  private updateEnrolledStudents(o: Observable<Student[]>) {
    o.subscribe(array => {
        console.log('onEnroll update date subscribe outer: ', array);
        this.backendService.getEnrolledStudents(this.courseId).subscribe(
          (ss: Student[]) => {
            this.enrolledStudents = [...ss];
            console.log('onEnroll date subscribe inner: ', ss);
          }
        );
      }
    );
  }
}

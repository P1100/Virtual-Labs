import {Component, OnDestroy, OnInit} from '@angular/core';
import {Student} from '../../model/student.model';
import {StudentService} from '../../services/student.service';
import {from, Observable, Subscription} from 'rxjs';
import {ActivatedRoute} from '@angular/router';
import {concatMap, tap, toArray} from 'rxjs/operators';

// TODO: muovere indietro a teacher folder prima di consegna
@Component({
  selector: 'app-students-cont',
  template: `
    <app-students [enrolled]="enrolledStudents"
                  [students]="allStudents"
                  (enrolledEvent)="onStudentsToEnroll($event)"
                  (disenrolledEvent)="onStudentsToDisenroll($event)"
    ></app-students>
  `,
  styleUrls: ['../../../_unused/students-cont.component.css']
})
export class StudentsContComponent implements OnInit, OnDestroy {
  allStudents: Student[] = [];
  enrolledStudents: Student[] = []; // TODO: enrolledStudents should be course dependent
  subAllStudents: Subscription;
  subEnrolledStudentsCourse: Subscription = null;
  courseId = 1;
  private paramSubscription: Subscription;

  constructor(private studentService: StudentService, private route: ActivatedRoute) {
    this.paramSubscription = this.route.url.subscribe(url => {
    });
    this.paramSubscription = this.route.paramMap.subscribe(url => {
      this.courseId = +this.route.parent.snapshot.paramMap.get('id');
      console.log('student-cont route.paramMap activeCourse: ' + this.courseId);
      this.subAllStudents = this.studentService.getAllStudents()
        .subscribe((students: Student[]) => this.allStudents = [...students]);
      this.subEnrolledStudentsCourse = this.studentService.getEnrolledStudents(this.courseId)
        .subscribe((students: Student[]) => this.enrolledStudents = [...students]);
    });
  }
  ngOnInit(): void {
    console.log('student-cont.ngOnInit');
  }
  ngOnDestroy(): void {
    console.log('student-cont.ngOnDestroy');
    this.subAllStudents.unsubscribe();
    this.subEnrolledStudentsCourse.unsubscribe();
    this.paramSubscription.unsubscribe();
  }

  onStudentsToEnroll(studentsToEnroll: Student[]) {
    const observable = from(studentsToEnroll).pipe(
      tap(student => console.log('ConcatMap pipe in. Current student:')),
      tap(student => console.log(student)),
      concatMap((student: Student) =>
        this.studentService.enroll(student, this.courseId) as Observable<any> // this.httpClient.get(`item/${student}`))
      ),
      tap(student => console.log('ConcatMap pipe out')),
      toArray(),
      tap(student => console.log('toArray'))
    ) as Observable<any>;
    console.log('out onStudentsToEnroll observable');
    this.updateData(observable);
  }
  onStudentsToDisenroll(studentsToDisenroll: Student[]) {
    console.log('in studentsToDisenroll');
    const observable = from(studentsToDisenroll).pipe(
      tap(student => console.log('ConcatMap pipe in. Current student:', student)),
      concatMap((student: Student) =>
        this.studentService.disenroll(student, this.courseId) as Observable<any> // this.httpClient.get(`item/${student}`))
      ),
      tap(student => console.log('ConcatMap pipe out')),
      toArray(),
      tap(student => console.log('toArray pipe out'))
    ) as Observable<any>;
    console.log('out onStudentsToDisenroll observable');
    this.updateData(observable);
  }
  private updateData(o: Observable<any>) {
    console.log('in updateData');
    o.subscribe(array => {
        console.log('onStudentsToDisenroll update date subscribe outer: ', array);
        this.studentService.getEnrolledStudents(this.courseId).subscribe(
          (ss: Student[]) => {
            this.enrolledStudents = [...ss];
            console.log('onStudentsToEnroll date subscribe inner: ', ss);
          }
        );
      }
    );
    console.log('out updateData');
  }
}

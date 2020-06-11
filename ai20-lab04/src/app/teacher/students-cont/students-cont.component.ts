import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {Student} from '../../model/student.model';
import {StudentService} from '../../services/student.service';
import {Subscription} from 'rxjs';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-students-cont',
  template: `
    <app-students [enrolled]="enrolledStudents"
                  [students]="allStudents"
                  (enroll)="onStudentsToEnroll($event)"
                  (disenroll)="onStudentsToDisenroll($event)"
    ></app-students>
  `,
  styleUrls: ['../../../_unused/students-cont.component.css']
})
export class StudentsContComponent implements OnInit, OnDestroy {
  @Input()
  title;

  // Important to initialize the arrays! Otherwise undefined is passed to the presentational component
  allStudents: Student[] = [];  // = DB_STUDENT;
  // TODO: this list below should be course dependent
  enrolledStudents: Student[] = [];  // = [...DB_STUDENT.slice(1, 10)];
  subAllStudents: Subscription;
  subEnrolledStudentsCourse: Subscription = null;
  courseId = 1;

  constructor(private studentService: StudentService, private route: ActivatedRoute) {
    // this.allStudents = studentService.getStudents();
    // this.enrolledStudents = [...this.allStudents.slice(1, 6)];
    this.route.url.subscribe(url => {
      this.courseId = +this.route.parent.snapshot.paramMap.get('id');
      console.log('@Student-cont route.url: ' + url.toString() + '-' + this.courseId);
      // necessary to update student tab list of students, when changing course ----> NOPE, i update verything on the the sidenav container
      // this.subEnrolledStudentsCourse = this.studentService.getEnrolledStudents(this.courseId)
      //   .subscribe((students: Student[]) => this.enrolledStudents = [...students]);
    });
  }
  ngOnInit(): void {
    // this.courseId = +this.route.snapshot.paramMap.get('id');
    // console.log('StudentCont ngOnInit route course id: ' + this.courseId);
    this.subAllStudents = this.studentService.getAllStudents()
      .subscribe((students: Student[]) => this.allStudents = [...students]);
    // // TODO: change courseId later
    this.subEnrolledStudentsCourse = this.studentService.getEnrolledStudents(this.courseId)
      .subscribe((students: Student[]) => this.enrolledStudents = [...students]);
  }
  ngOnDestroy(): void {
    // throw new Error("Method not implemented.");
    this.subAllStudents.unsubscribe();
    this.subEnrolledStudentsCourse.unsubscribe();
  }
  // Necessary to reallocate the array, to trigger angular checkAndUpdate of the property
  onStudentsToEnroll(students: Student[]) {
    const newEnrolledStudentsArray = [...this.enrolledStudents];
    for (const selectedStudentToAdd of students) {
      newEnrolledStudentsArray.push(selectedStudentToAdd);
    }
    // this.enrolledStudents = null;
    this.enrolledStudents = newEnrolledStudentsArray.slice();
  }
  onStudentsToDisenroll(selectedStudentsToRemove: Student[]) {
    const newEnrolledStudentsArray: Student[] = [...this.enrolledStudents];
    for (const studentToRemove of selectedStudentsToRemove) {
      // The first index of the element in the array; -1 if not found
      const index = newEnrolledStudentsArray.indexOf(studentToRemove);
      if (index !== -1) {
        newEnrolledStudentsArray.splice(index, 1);
      }
    }
    // this.studentService.testDeleteDB();
    this.enrolledStudents = newEnrolledStudentsArray.slice();
  }
  private updateData() {
  }
}

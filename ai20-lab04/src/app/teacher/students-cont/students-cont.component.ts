import {Component, Input, OnInit} from '@angular/core';
import {Student} from '../../services/student';
import {StudentService} from '../../services/student.service';

@Component({
  selector: 'app-students-cont',
  templateUrl: './students-cont.component.html',
  styleUrls: ['../../../_unused/students-cont.component.css']
})
export class StudentsContComponent implements OnInit {
  allStudents: Student[];
  // = DB_STUDENT;
  // TODO: this list below should be course dependent
  enrolledStudentsCourse: Student[];
  // = [...DB_STUDENT.slice(1, 10)];
  @Input()
  title;

  constructor(private studentService: StudentService) {
    this.allStudents = studentService.getStudents();
    this.enrolledStudentsCourse = [...this.allStudents.slice(1, 6)];
  }
  ngOnInit(): void {
  }

  // Necessary to reallocate the array, to trigger angular checkAndUpdate of the property
  onStudentsToAdd(students: Student[]) {
    const newEnrolledStudentsArray = [...this.enrolledStudentsCourse];
    for (const selectedStudentToAdd of students) {
      newEnrolledStudentsArray.push(selectedStudentToAdd);
    }
    // this.enrolledStudentsCourse = null;
    this.enrolledStudentsCourse = newEnrolledStudentsArray.slice();
  }
  onStudentsToRemove(selectedStudentsToRemove: Student[]) {
    const newEnrolledStudentsArray: Student[] = [...this.enrolledStudentsCourse];
    for (const studentToRemove of selectedStudentsToRemove) {
      // The first index of the element in the array; -1 if not found
      const index = newEnrolledStudentsArray.indexOf(studentToRemove);
      if (index !== -1) {
        newEnrolledStudentsArray.splice(index, 1);
      }
    }
    this.enrolledStudentsCourse = newEnrolledStudentsArray.slice();
  }
}

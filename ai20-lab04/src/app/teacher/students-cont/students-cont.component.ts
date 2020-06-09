import {Component, EventEmitter, Input, OnInit} from '@angular/core';
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
    console.log('loaded_students');
  }

  ngOnInit(): void {
  }

  set adds(value: EventEmitter<Student[]>) {
    this._adds = value;
  }
}

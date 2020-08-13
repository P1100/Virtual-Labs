import {Component, OnInit, ViewChild} from '@angular/core';
import {BackendService} from '../../services/backend.service';
import {CourseService} from '../../services/course-service';
import {FormGroup, NgForm} from '@angular/forms';
import {Student} from '../../models/student.model';
import {Course} from '../../models/course.model';

@Component({
  selector: 'app-testing',
  templateUrl: './testing.component.html',
  styleUrls: ['./testing.component.css']
})
export class TestingComponent implements OnInit {
  getCourses: FormGroup;
  student: Student;
  arrs: Student[];
  course: Course;
  arrc: Course[];
  @ViewChild('heroForm')
  heroForm: NgForm; // ElementRef;

  constructor(private backendService: BackendService, private courseService: CourseService) {
  }

  ngOnInit(): void {
  }

  onGetCoursesSubmit() {
    this.courseService.getCourses().subscribe(x => console.log(x));
  }
  onGetCourseSubmit(s: string) {
    this.courseService.getCourse(s).subscribe(x => console.log(x, s));
  }
}

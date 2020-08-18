import {Component, ViewChild} from '@angular/core';
import {BackendService} from '../../services/backend.service';
import {CourseService} from '../../services/course-service';
import {FormGroup, NgForm} from '@angular/forms';
import {Student} from '../../models/student.model';
import {Course} from '../../models/course.model';

@Component({
  selector: 'app-testing',
  templateUrl: './testing.component.html',
  styles: ['#testing_container {\n  margin: 30px;\n}', 'input {  margin: 5px;}', 'button {margin: 2px}']
})
export class TestingComponent {
  getCourses: FormGroup;
  student: Student;
  arrs: Student[];
  course: Course;
  arrc: Course[];
  @ViewChild('heroForm')
  heroForm: NgForm; // ElementRef;

  constructor(private backendService: BackendService, private courseService: CourseService) {
  }

  onGetCoursesSubmit() {
    this.courseService.getCourses().subscribe(x => console.log('TT', x));
  }
  getCourse(s: string) {
    this.courseService.getCourse(s).subscribe(x => console.log('TT', s, x));
  }
  getEnrolledStudents(s: string) {
    this.courseService.getEnrolledStudents(s).subscribe(x => console.log('TT', s, x));
  }
  getAllStudents(s: string) {
  }

  enableCourse(c: string) {
    this.courseService.enableCourse(c).subscribe();
  }
  disableCourse(c: string) {
    this.courseService.disableCourse(c).subscribe();
  }
  addCourse(c: string) {
    this.courseService.addCourse(new Course(c, 'testCourse', 1, 11, true, 'Dockerfile')).subscribe();
  }
  updateCourse(c: string) {
    this.courseService.updateCourse(new Course(c, 'testCourseUpdate', 1, 22, false, 'Dockerfile')).subscribe();
  }
  deleteCourse(c: string) {
    this.courseService.deleteCourse(c).subscribe();
  }
}

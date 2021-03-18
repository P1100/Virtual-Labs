import {Component} from '@angular/core';
import {StudentService} from '../../services/student.service';
import {CourseService} from '../../services/course.service';
import {Student} from '../../models/student.model';
import {Course} from '../../models/course.model';
import {ImageService} from '../../services/image.service';
import {DomSanitizer} from '@angular/platform-browser';

@Component({
  selector: 'app-testing',
  templateUrl: './testing.component.html',
  styles: ['#testing_container {\n  margin: 30px;\n}', 'input {  margin: 5px;}', 'button {margin: 2px}'],
  // styles: ['./testing.component.css']
})
export class TestingComponent {
  student: Student;
  arrs: Student[];
  course: Course;
  arrc: Course[];

  constructor(private studentService: StudentService, private courseService: CourseService, private imageService: ImageService,
              private sanitizer: DomSanitizer) {
  }
  getAllCourses() {
    this.courseService.getCourses().subscribe();
  }
  getCourse(s: string) {
    this.courseService.getCourse(s).subscribe();
  }
  getEnrolledStudents(s: string) {
    this.studentService.getEnrolledStudents(s).subscribe();
  }
  getAllStudents() {
    this.studentService.getAllStudents().subscribe();
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
  disenrollStudent(s: string, c: string) {
    this.courseService.disenrollStudent(+s, c).subscribe();
  }
  enrollStudent(s: string, c: string) {
    this.courseService.enrollStudent(new Student(+s, 'Paolo', 'Ferri', 'paofer@studenti.polito.it'), c).subscribe();
  }
  enrollStudents(s1: string, s2: string, c: string) {
    this.courseService.enrollStudents(
      [+s1, +s2], c).subscribe();
  }

  getTeamsForCourse(c: string) {
    this.courseService.getTeamsForCourse(c).subscribe();
  }
  getStudentsInTeams(c: string) {
    this.courseService.getEnrolledWithTeam(c).subscribe();
  }
  getAvailableStudents(c: string) {
    this.courseService.getEnrolledWithoutTeam(c).subscribe();
  }
}

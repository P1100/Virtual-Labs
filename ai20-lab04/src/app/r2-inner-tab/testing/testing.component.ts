import {Component, ElementRef, ViewChild} from '@angular/core';
import {StudentService} from '../../services/student.service';
import {CourseService} from '../../services/course.service';
import {FormGroup, NgForm} from '@angular/forms';
import {Student} from '../../models/student.model';
import {Course} from '../../models/course.model';
import {ImageService} from '../../services/image.service';
import {DomSanitizer} from '@angular/platform-browser';

@Component({
  selector: 'app-testing',
  templateUrl: './testing.component.html',
  styles: ['#testing_container {\n  margin: 30px;\n}', 'input {  margin: 5px;}', 'button {margin: 2px}'],
  styleUrls: ['./testing.component.css']
})
export class TestingComponent {
  getCourses: FormGroup;
  student: Student;
  arrs: Student[];
  course: Course;
  arrc: Course[];
  @ViewChild('heroForm')
  heroForm: NgForm; // ElementRef;
  panelOpenState = false;

  constructor(private studentService: StudentService, private courseService: CourseService, private imageService: ImageService,
              private sanitizer: DomSanitizer) {
  }

  selectedFile: File;
  retrievedImage: any;
  base64Data: any;
  retrieveResonse: any;
  message: string;
  imageId: any;
  uploadImageData: FormData;

  @ViewChild('labelFileName')
  labelFileName: ElementRef;

  uploadCSVData: FormData;

  // Gets called when the user clicks on submit to upload the image
  onCSVUpload(c: string) {
    console.log(this.selectedFile);
    this.uploadCSVData = new FormData();
    this.uploadCSVData.append('file', this.selectedFile);
    console.log(this.uploadCSVData, this.uploadCSVData.get('file'));
    this.courseService.enrollStudentsCSV(c, this.uploadCSVData).subscribe(
      ((response) => {
          console.log(response);
        }
      ));
  }
  // Gets called when the user selects an image
  public onFileChanged(event) {
    this.selectedFile = event.target.files[0];
    console.log(this.selectedFile);
    // To update bootstrap input text, missing JQuery
    this.labelFileName.nativeElement.innerText = this.selectedFile.name;
  }
  // Gets called when the user clicks on submit to upload the image
  onImageUpload() {
    // console.log(this.selectedFile);
    // FormData API provides methods and properties to allow us easily prepare form data to be sent with POST HTTP requests.
    this.uploadImageData = new FormData();
    this.uploadImageData.append('imageFile', this.selectedFile, this.selectedFile.name);
    console.log(this.uploadImageData, this.uploadImageData.get('imageFile'));
    this.imageService.uploadImage(this.uploadImageData)
      .subscribe((response) => {
          // console.log(response);
          if (response.status === 200) {
            this.message = 'Image uploaded successfully';
          } else {
            this.message = 'Image not uploaded successfully';
          }
        }
      );
  }
  // Gets called when the user clicks on retieve image button to get the image from back end
  getImage() {
    console.log('getImage', this.imageId);
    this.imageService.getImage(this.imageId)
      .subscribe(
        res => {
          this.retrieveResonse = res;
          this.base64Data = this.retrieveResonse.imageStringBase64;
          console.log(this.base64Data);
          this.retrievedImage = this.sanitizer.bypassSecurityTrustResourceUrl('data:image/jpeg;base64,' + this.base64Data);
        }
      );
  }

  getAllCourses() {
    this.courseService.getCourses().subscribe();
  }
  getCourse(s: string) {
    this.courseService.getCourse(s).subscribe();
  }
  getEnrolledStudents(s: string) {
    this.courseService.getEnrolledStudents(s).subscribe();
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
    this.courseService.getStudentsInTeams(c).subscribe();
  }
  getAvailableStudents(c: string) {
    this.courseService.getAvailableStudents(c).subscribe();
  }
}

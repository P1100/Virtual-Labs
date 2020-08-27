import {Component, Inject} from '@angular/core';
import {AppSettings} from '../../app-settings';
import {CourseService} from '../../services/course.service';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';

@Component({
  selector: 'app-course-delete',
  templateUrl: './course-delete.component.html',
  styles: []
})
export class CourseDeleteComponent {
  courseToDeleteId: string;
  courseToDeleteName: string;
  confirm: string;
  noValidateForTesting = AppSettings.devShowTestingComponents;

  constructor(private courseService: CourseService,
              public dialogRef: MatDialogRef<CourseDeleteComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any) {
    // data: {courseName: this.nameActiveCourse, courseId: this.idActiveCourse}
    this.courseToDeleteId = data?.courseId;
    this.courseToDeleteName = data?.courseName;
  }
  onSubmit() {
    const subscription = this.courseService.deleteCourse(this.courseToDeleteId).subscribe(
      x => this.dialogRef.close('success'),
      e => this.dialogRef.close(e));
  }
}

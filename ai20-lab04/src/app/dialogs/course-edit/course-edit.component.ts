import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {Course} from '../../models/course.model';
import {CourseService} from '../../services/course.service';
import {AppSettings} from '../../app-settings';

@Component({
  selector: 'app-course-edit',
  templateUrl: './course-edit.component.html',
  styles: ['mat-form-field {width: 100%}']
})
export class CourseEditComponent {
  course = new Course('null', '', 0, 0, false, '');
  addressForm: any;
  private selectedFile: File;
  noValidateForTesting = false;
  showNoValidateCheckboxForTesting = AppSettings.devShowTestingComponents;

  constructor(private courseService: CourseService,
              public dialogRef: MatDialogRef<CourseEditComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any) {
    // data: {courseName: this.nameActiveCourse, courseId: this.idActiveCourse}
    const subscription = courseService.getCourse(data?.courseId).subscribe((c: Course[]) => this.course = c[0]);
  }

  onCancelClick(): void {
    this.dialogRef.close(); // same value as when you press ESC (undefined)
  }
  onSubmit() {
    this.courseService.updateCourse(this.course).subscribe(
      x => this.dialogRef.close('success'),
      e => this.dialogRef.close(e)
    );
  }

  public onFileChanged(event) {
    this.course.enabled = !this.course.enabled;
    this.selectedFile = event.target.files[0];
    // To update bootstrap input text, missing JQuery
    this.course.vmModelPath = this.selectedFile.name;
  }
}

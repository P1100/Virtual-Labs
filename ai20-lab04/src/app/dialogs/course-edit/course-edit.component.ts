import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {Course} from '../../models/course.model';
import {CourseService} from '../../services/course.service';
import {AppSettings} from '../../app-settings';
import {dialogCourseData} from '../../r0-topheader-leftsidebar/home.component';
import {Router} from '@angular/router';
import {AlertsService} from '../../services/alerts.service';

@Component({
  selector: 'app-course-edit',
  templateUrl: './course-edit.component.html',
  styles: ['mat-form-field {width: 100%}']
})
export class CourseEditComponent {
  course = new Course('null', '', 0, 0, false, '');
  private selectedFile: File;
  checkboxNoValidate = false;
  showCheckboxNoValidateForTesting = AppSettings.devtest;

  constructor(private courseService: CourseService,
              public dialogRef: MatDialogRef<CourseEditComponent>,
              @Inject(MAT_DIALOG_DATA) public data: dialogCourseData, private router: Router, private alertsService: AlertsService) {
    const subscription = courseService.getCourse(data?.courseId).subscribe(
      (c: Course[]) => this.course = c[0],
      error => {
        this.dialogRef.close();
        this.alertsService.setAlert('danger', 'Course edit: failed to get course info. ' + error);
      });
  }

  onCancelClick(): void {
    this.dialogRef.close(); // same value as when you press ESC (undefined)
  }
  onSubmit() {
    this.courseService.updateCourse(this.course).subscribe(
      x => {
        this.dialogRef.close('success');
        this.router.navigateByUrl('/');
        this.alertsService.setAlert('success', 'Course updated!');
      },
      e => {
        this.dialogRef.close();
        this.alertsService.setAlert('danger', 'Couldn\'t update course! ' + e);
      }
    );
  }

  public onFileChanged(event) {
    this.course.enabled = !this.course.enabled;
    this.selectedFile = event.target.files[0];
    this.course.vmModelPath = this.selectedFile.name;
  }
}

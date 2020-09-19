import {Component} from '@angular/core';
import {Course} from '../../models/course.model';
import {AppSettings} from '../../app-settings';
import {CourseService} from '../../services/course.service';
import {MatDialogRef} from '@angular/material/dialog';
import {Router} from '@angular/router';
import {AlertsService} from '../../services/alerts.service';

@Component({
  selector: 'app-course-add',
  templateUrl: './course-add.component.html',
  styles: ['mat-form-field {width: 100%}']
})
export class CourseAddComponent {
  course = new Course('', '', 1, 10, false, '');
  private selectedFile: File;
  checkboxNoValidate = false;
  showCheckboxNoValidateForTesting = AppSettings.devtest;

  constructor(private courseService: CourseService, private dialogRef: MatDialogRef<CourseAddComponent>, private router: Router, private alertsService: AlertsService) {
  }

  onCancelClick(): void {
    this.dialogRef.close(); // same value as when you press ESC (undefined)
  }
  onSubmit() {
    this.courseService.addCourse(this.course).subscribe(
      x => {
        this.dialogRef.close('success');
        this.router.navigateByUrl('/');
        this.alertsService.setAlert('success', 'Course added!');
      },
      e => {
        this.dialogRef.close();
        this.alertsService.setAlert('danger', 'Couldn\'t add course! ' + e);
      }
    );
  }

  public onFileChanged(event) {
    this.course.enabled = !this.course.enabled;
    this.selectedFile = event.target.files[0];
    this.course.vmModelPath = this.selectedFile.name;
  }
}

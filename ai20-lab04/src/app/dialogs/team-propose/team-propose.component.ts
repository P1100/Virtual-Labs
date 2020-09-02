import {Component, Inject} from '@angular/core';
import {Course} from '../../models/course.model';
import {AppSettings} from '../../app-settings';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {AlertsService} from '../../services/alerts.service';
import {CourseService} from '../../services/course.service';
import {Router} from '@angular/router';
import {Student} from '../../models/student.model';

@Component({
  selector: 'app-team-propose',
  templateUrl: './team-propose.component.html',
  styles: ['mat-form-field {width: 100%}']
})
export class TeamProposeComponent {
  course = new Course('', '', 1, 10, false, '');
  private selectedFile: File;
  checkboxNoValidate = false;
  showCheckboxNoValidateForTesting = AppSettings.devtest;
  current: string;
  nameTeam: string;
  timeoutTeam: number;

  constructor(private courseService: CourseService, private dialogRef: MatDialogRef<TeamProposeComponent>,
              @Inject(MAT_DIALOG_DATA) public data: Student[], private router: Router, private alertsService: AlertsService) {
    this.current = localStorage.getItem('id');
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
        this.alertsService.setAlert('danger', 'Couldn\'t add course! ' + e);
        this.dialogRef.close();
      }
    );
  }

  public onFileChanged(event) {
    this.course.enabled = !this.course.enabled;
    this.selectedFile = event.target.files[0];
    this.course.vmModelPath = this.selectedFile.name;
  }
}

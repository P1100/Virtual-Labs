import {Component, Inject} from '@angular/core';
import {AppSettings} from '../../app-settings';
import {CourseService} from '../../services/course.service';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {dialogCourseData} from '../../r0-topheader-leftsidebar/home.component';
import {Router} from '@angular/router';
import {AlertsService} from '../../services/alerts.service';

@Component({
  selector: 'app-course-delete',
  templateUrl: './course-delete.component.html',
  styles: []
})
export class CourseDeleteComponent {
  courseToDeleteId: string;
  courseToDeleteName: string;
  confirm: string;
  noValidateForTesting = AppSettings.devModeShowAll;

  constructor(private courseService: CourseService,
              public dialogRef: MatDialogRef<CourseDeleteComponent>,
              @Inject(MAT_DIALOG_DATA) public data: dialogCourseData, private router: Router, private alertsService: AlertsService) {
    this.courseToDeleteId = data?.courseId;
    this.courseToDeleteName = data?.courseName;
  }
  onSubmit() {
    const subscription = this.courseService.deleteCourse(this.courseToDeleteId).subscribe(
      x => {
        this.dialogRef.close('success');
        this.router.navigateByUrl('/');
        this.alertsService.setAlert({type: 'success', message: 'Course deleted!'});
      },
      e => {
        this.alertsService.setAlert({type: 'danger', message: 'Couldn\'t delete course! ' + e});
        this.dialogRef.close();
      }
    );
  }
}

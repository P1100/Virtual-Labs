import {Component} from '@angular/core';
import {AppSettings} from '../../app-settings';
import {CourseService} from '../../services/course.service';
import {MatDialogRef} from '@angular/material/dialog';
import {Router} from '@angular/router';
import {AlertsService} from '../../services/alerts.service';
import {User} from '../../models/user.model';
import {AuthService} from '../../services/auth.service';
import {Observable} from 'rxjs';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styles: ['mat-form-field {width: 100%} mat-radio-button {margin-right: 12px}']
})
export class RegisterComponent {
  user = new User(null, null, null, null, []);
  checkboxNoValidate = false;
  showCheckboxNoValidateForTesting = AppSettings.devModeShowAll;
  isStudentRadio = false;

  constructor(private courseService: CourseService, public dialogRef: MatDialogRef<RegisterComponent>, private router: Router,
              private alertsService: AlertsService, private authService: AuthService) {
  }

  onCancelClick(): void {
    this.dialogRef.close(); // same value as when you press ESC (undefined)
  }
  onSubmit() {
    let obs: Observable<any>;
    console.log(this.isStudentRadio, this.isStudentRadio);
    if (this.isStudentRadio) {
      obs = this.authService.registerStudent(this.user);
    } else {
      obs = this.authService.registerProfessor(this.user);
    }
    obs.subscribe(
      x => {
        this.dialogRef.close('success');
        this.router.navigateByUrl('/');
        this.alertsService.setAlert('success', 'User registered. Check email for confirmation');
      },
      e => {
        this.dialogRef.close();
        this.alertsService.setAlert('danger', 'Couldn\'t register user! ' + e);
      }
    );
  }
}

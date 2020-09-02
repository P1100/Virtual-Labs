import {Component, OnDestroy} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Subscription} from 'rxjs';
import {MatDialogRef} from '@angular/material/dialog';
import {AuthService} from '../../services/auth.service';
import {ActivatedRoute, Router} from '@angular/router';
import {filter} from 'rxjs/operators';
import {User} from '../../models/user.model';
import {AlertsService} from '../../services/alerts.service';

@Component({
  selector: 'app-login-dialog',
  templateUrl: './login.component.html',
  styleUrls: []
})

export class LoginComponent implements OnDestroy {
  user = new User(null, null, null, null, null, null, []);
  form: FormGroup;
  subscriptionLogin: Subscription;

  constructor(public dialogRef: MatDialogRef<LoginComponent>,
              public fb: FormBuilder,
              public authService: AuthService,
              public router: Router,
              public activatedRoute: ActivatedRoute,
              public alertsService: AlertsService) {
    this.form = this.fb.group(
      {
        serial: ['111111', [Validators.required]],
        password: ['222222', [Validators.required]],
      }
    ) as FormGroup;
    this.form.valueChanges.pipe(
      filter(() => this.form.valid)
    ).subscribe(form => {
      this.user.username = form?.serial;
      this.user.password = form?.password;
    });
  }
  ngOnDestroy(): void {
    this.dialogRef.close(); // Destroys the dialog!
    this.router.navigateByUrl('/home');
  }
  onCancelClick(): void {
    this.subscriptionLogin?.unsubscribe();
    this.dialogRef.close(); // Destroys the dialog!
  }
  login() {
    if (!this.form.valid) {
      return;
    }
    if (this.form.value?.serial && this.form.value?.password) {
      this.subscriptionLogin = this.authService.login(this.form.value.serial, this.form.value.password)
        .subscribe(
          x => {
            this.dialogRef.close('success');
            this.router.navigateByUrl('/');
            this.alertsService.setAlert('success', 'Logged in!');
          },
          e => {
            this.dialogRef.close();
            this.alertsService.setAlert('danger', 'Login failed: ' + e);
          }
        );
    }
  }
  logout() {
    this.authService.logout();
  }
}

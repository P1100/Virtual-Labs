import {Component, OnDestroy} from '@angular/core';
import {FormBuilder, FormGroup, ValidationErrors, Validators} from '@angular/forms';
import {Subscription} from 'rxjs';
import {MatDialogRef} from '@angular/material/dialog';
import {AuthService} from '../../services/auth.service';
import {ActivatedRoute, Router} from '@angular/router';
import {filter} from 'rxjs/operators';
import {User} from '../../models/user.model';

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
              private fb: FormBuilder,
              private authService: AuthService,
              private router: Router,
              private activatedRoute: ActivatedRoute) {
    this.form = this.fb.group(
      {
        email: ['000000', [Validators.required]],
        password: ['000000', [Validators.required]],
      },
      {
        validators: fakeNameValidator
      }) as FormGroup;
    this.form.valueChanges.pipe(
      filter(() => this.form.valid)
    ).subscribe(form => {
      this.user.username = form?.email;
      this.user.username = form?.password;
    });
  }
  ngOnDestroy(): void {
    this.onCancelClick();
  }
  onCancelClick(): void {
    this.subscriptionLogin?.unsubscribe();
    this.dialogRef.close(); // Destroys the dialog!
    this.router.navigateByUrl('/home');
  }
  login() {
    const val = this.form.value;
    if (val?.email && val?.password) {
      this.subscriptionLogin = this.authService.login(val.email, val.password)
        .subscribe(() => {
            this.dialogRef.close();
            this.router.navigateByUrl('/');
          }
        );
    }
  }
  logout() {
    this.authService.logout();
  }
}

function fakeNameValidator(control: FormGroup): ValidationErrors | null {
  const email = control.get('password');
  const password = control.get('email');
  const ret = password && email && password.value === email.value ? {fakeName: true} : null;
  return ret;
}

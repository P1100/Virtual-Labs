import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ValidationErrors, Validators} from '@angular/forms';
import {Subscription} from 'rxjs';
import {MatDialogRef} from '@angular/material/dialog';
import {AuthService} from '../../services/auth.service';
import {ActivatedRoute, Router} from '@angular/router';
import {filter, map, tap} from 'rxjs/operators';

@Component({
  selector: 'app-login-dialog',
  templateUrl: './login-dialog.component.html',
  styleUrls: []
})

export class LoginDialogComponent implements OnInit, OnDestroy {
  public user;
  form: FormGroup;
  subscriptionLogin: Subscription;

  constructor(public dialogRef: MatDialogRef<LoginDialogComponent>,
              private fb: FormBuilder, private authService: AuthService,
              private router: Router, activatedRoute: ActivatedRoute) {
    this.form = this.fb.group({
      email: ['olivier@mail.com', [forbiddenNameValidator(/bob/i), Validators.required]],
      password: ['bestPassw0rd', [Validators.required]],
    }, {
      validators: fakeNameValidator,
      // updateOn: 'blur'
    }) as FormGroup;
    this.form.valueChanges.pipe(
      filter(() => this.form.valid),
      tap(formValue => console.log('Valuechanges: ' + JSON.stringify(formValue))),
      map(value => this.user = {id: value.email, password: value.password}), // , date: new Date()
    ).subscribe((user) => {
      this.user = user;
      console.log(this.user);
    });
  }
  ngOnInit(): void {
  }
  ngOnDestroy(): void {
    console.log('LoginDialogComponent destroyed!');
  }
  onCancelClick(): void {
    this.subscriptionLogin?.unsubscribe();
    this.dialogRef.close(); // Destroys the dialog!
    this.router.navigateByUrl('/home');
  }
  login() {
    const val = this.form.value;
    if (val.email && val.password) {
      this.subscriptionLogin = this.authService.login(val.email, val.password)
        .subscribe((accessToken) => {
            console.log('User is logged in. Received: ' + JSON.stringify(accessToken), accessToken);
            console.log('LoginDialogComponent ended login http sub');
            this.dialogRef.close();
            console.log('LoginDialogComponent after dialogRef.close()');
            this.router.navigateByUrl('/');
            console.log('LoginDialogComponent after navigateByUrl!');
          }
        );
    }
  }
  logout() {
    this.authService.logout();
  }
}

function forbiddenNameValidator(nameRe: RegExp) {
  return (control) => {
    const forbidden = nameRe.test(control.value);
    // console.log(`is this name ${control.value} forbidden? ${forbidden}`);
    return forbidden ? {forbiddenName: {value: control.value}} : null;
  };
}
/** A hero's name can't match the hero's alter ego */
function fakeNameValidator(control: FormGroup): ValidationErrors | null {
  const email = control.get('password');
  const password = control.get('email');
  const ret = password && email && password.value === email.value ? {fakeName: true} : null;
  // if (ret) { console.log(`${email.value} === ${password.value}`); }
  // console.log(`are email and password equal? ${email.value} === ${password.value} ${email.value === password.value}`);
  return ret;
}

import {Component, Inject, OnDestroy, OnInit} from '@angular/core';
import {Course} from '../model/course.model';
import {Title} from '@angular/platform-browser';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material/dialog';
import {FormBuilder, FormGroup, ValidationErrors, Validators} from '@angular/forms';
import {filter, map, tap} from 'rxjs/operators';
import {AuthService} from '../auth/auth.service';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs';

export interface DialogData {
  animal: string;
  name: string;
}

const DB_COURSES: Course[] = [
  {id: 1, label: 'Applicazioni Internet', path: 'applicazioni-internet'},
  {id: 2, label: 'Programmazione di sistema', path: 'programmazione-di-sistema'},
  {id: 3, label: 'Mobile development', path: 'mobile-development'}
];

// TODO: integrate es1 into project dialog login/registration
@Component({
  // selector changed from app-root, inserted in index.html!
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit, OnDestroy {
  title = 'VirtualLabs';
  courses = DB_COURSES;
  isLogged = false;
  loggedUser = '';
  subscription: Subscription;
  subscriptionRoute: Subscription;

  animal: string;
  name: string;

  constructor(private titleService: Title, public dialog: MatDialog, private auth: AuthService,
              private router: Router, private route: ActivatedRoute) {
    titleService.setTitle(this.title);
    this.isLogged = this.auth.isLoggedIn();
    this.subscription = this.auth.getSub().subscribe(x => {
      this.isLogged = x;
      if (x === true) {
        this.loggedUser = localStorage.getItem('user');
      } else {
        localStorage.removeItem('user');
      }
    });
  }
  ngOnInit(): void {
    console.log('# HomeController.ngOninit START');
    this.subscriptionRoute = this.route.queryParams.subscribe(params => {
      console.log('inside_Route', params, params.doLogin, params['doLogin']);
      // this.doLogin = params['doLogin'];
      if (params.doLogin == 'true') {
        console.log('inside_DoLogin');
        this.openLoginDialogReactive();
      }
    });
  }
  openLoginDialogTemplate(): void {
    const dialogRef = this.dialog.open(LoginDialogTemplateComponent, {
      maxWidth: '600px', hasBackdrop: true,
      data: {name: this.name, animal: this.animal}
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }
  ngOnDestroy(): void {
    this.subscription.unsubscribe();
    this.subscriptionRoute.unsubscribe();
  }
  openLoginDialogReactive(): void {
    const dialogRef = this.dialog.open(LoginDialogReactiveComponent, {
      maxWidth: '600px',
      autoFocus: true,
      disableClose: false, // Esc key will close it
      hasBackdrop: false, // clicking outside wont close it
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed', result);
    });
  }
  clickLoginLogout() {
    if (this.isLogged) {
      console.log('logout');
      this.auth.logout();
      this.router.navigateByUrl('/home');
    } else {
      console.log('login');
      this.router.navigateByUrl('/home?doLogin=true');
      // this.openLoginDialogReactive();
    }
  }
}

@Component({
  selector: 'app-login-dialog',
  styleUrls: ['../auth/login-dialog.component.css'],
  templateUrl: '../auth/login-dialog-reactive.component.html',
})
export class LoginDialogReactiveComponent {
  public user;
  form: FormGroup;

  constructor(public dialogRef: MatDialogRef<LoginDialogReactiveComponent>,
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
  onNoClick(): void {
    this.dialogRef.close();
    this.router.navigateByUrl('/home');
  }
  login() {
    const val = this.form.value;
    if (val.email && val.password) {
      this.authService.login(val.email, val.password)
        .subscribe((accessToken) => {
            console.log('User is logged in. Received: ' + JSON.stringify(accessToken), accessToken);
          }
        );
      this.dialogRef.close();
      this.router.navigateByUrl('/');
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

@Component({
  selector: 'app-login-dialog',
  styleUrls: ['../auth/login-dialog.component.css'],
  templateUrl: '../auth/login-dialog-template.component.html',
})
export class LoginDialogTemplateComponent {
  constructor(
    public dialogRef: MatDialogRef<LoginDialogTemplateComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData) {
  }
  onNoClick(): void {
    this.dialogRef.close();
  }
}

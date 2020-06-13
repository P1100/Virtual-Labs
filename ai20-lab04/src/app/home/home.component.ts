import {Component, Inject, OnInit} from '@angular/core';
import {Course} from '../model/course.model';
import {Title} from '@angular/platform-browser';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material/dialog';
import {FormBuilder, FormGroup, ValidationErrors, Validators} from '@angular/forms';
import {filter, map, tap} from 'rxjs/operators';

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
// TODO: turn Ivy on (angular 9)
@Component({
  // selector changed from app-root, inserted in index.html!
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  title = 'VirtualLabs';
  courses = DB_COURSES;
  loginOrLogout = 'Login';
  loggedUser = '';

  animal: string;
  name: string;

  constructor(private titleService: Title, public dialog: MatDialog) {
    titleService.setTitle(this.title);
  }
  ngOnInit(): void {
    console.log('# HomeController.ngOninit START');
  }
  openLoginDialogTemplate(): void {
    const dialogRef = this.dialog.open(HomeControllerLoginDialogTemplate, {
      maxWidth: '600px',
      data: {name: this.name, animal: this.animal}
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      this.animal = result;
    });
  }
  openLoginDialogReactive(): void {
    const dialogRef = this.dialog.open(HomeControllerLoginDialogReactive, {
      maxWidth: '600px',
      autoFocus: true,
      disableClose: false, // Esc key will close it
      hasBackdrop: false, // clicking outside wont close it
      data: {name: this.name, animal: this.animal}
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      this.animal = result;
    });
  }
}

@Component({
  selector: 'app-login-dialog',
  styleUrls: ['./login-dialog.component.css'],
  templateUrl: './login-dialog-template.component.html',
})
export class HomeControllerLoginDialogTemplate {
  constructor(
    public dialogRef: MatDialogRef<HomeControllerLoginDialogTemplate>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData) {
  }
  onNoClick(): void {
    this.dialogRef.close();
  }
}


function forbiddenNameValidator(nameRe: RegExp) {
  return (control) => {
    const forbidden = nameRe.test(control.value);
    console.log(`is this name ${control.value} forbidden? ${forbidden}`);
    return forbidden ? { forbiddenName: { value: control.value } } : null;
  };
}

/** A hero's name can't match the hero's alter ego */
function fakeNameValidator(control: FormGroup): ValidationErrors | null {
  const firstName = control.get('lastName');
  const lastName = control.get('firstName');

  const ret = lastName && firstName && lastName.value === firstName.value ? { fakeName: true } : null;
  // if (ret) { console.log(`${firstName.value} === ${lastName.value}`); }
  console.log(`are firstName and lastName equal? ${firstName.value} === ${lastName.value} ${firstName.value === lastName.value}`);

  return ret;
}
@Component({
  selector: 'app-login-dialog',
  styleUrls: ['./login-dialog.component.css'],
  templateUrl: './login-dialog-reactive.component.html',
})
export class HomeControllerLoginDialogReactive {
  public user;
  profileForm = this.fb.group({
    firstName: ['not matching bob and required', [forbiddenNameValidator(/bob/i), Validators.required]],
    lastName: ['required', [Validators.required]],
  }, {
    validators: fakeNameValidator,
    // updateOn: 'blur'
  });

  constructor(
    public dialogRef: MatDialogRef<HomeControllerLoginDialogReactive>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    private fb: FormBuilder) {

    this.profileForm.valueChanges.pipe(
      filter(() => this.profileForm.valid),
      tap(formValue => console.log('Valuechanges: ' + JSON.stringify(formValue))),
      map(value => this.user = {firstName: value.firstName, lastName: value.lastName, date: new Date()}),
    ).subscribe((user) => {
      this.user = user;
      console.log(this.user);
    });
  }
  onNoClick(): void {
    this.dialogRef.close();
  }
}

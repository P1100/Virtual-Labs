import {Component, OnDestroy, OnInit} from '@angular/core';
import {Course} from '../models/course.model';
import {Title} from '@angular/platform-browser';
import {MatDialog} from '@angular/material/dialog';
import {AuthService} from '../services/auth.service';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs';
import {TestDialogComponent} from '../dialogs/test-dialog/test-dialog.component';
import {LoginDialogComponent} from '../dialogs/login-dialog/login-dialog.component';

// TODO: remove it later, it was test code
export interface DialogData {
  animal: string;
  name: string;
}

const DB_COURSES: Course[] = [
  {id: 1, label: 'Applicazioni Internet', path: 'applicazioni-internet', fullName: '', minEnrolled: 0, maxEnrolled: 0, enabled: true},
  {
    id: 2,
    label: 'Programmazione di sistema',
    path: 'programmazione-di-sistema',
    fullName: '',
    minEnrolled: 0,
    maxEnrolled: 0,
    enabled: true
  },
  {id: 3, label: 'Mobile development', path: 'mobile-development', fullName: '', minEnrolled: 0, maxEnrolled: 0, enabled: true}
];

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
  dialogRef = undefined;

  animal: string;
  name: string;

  constructor(private titleService: Title, public dialog: MatDialog, private auth: AuthService,
              private router: Router, private route: ActivatedRoute) {
    titleService.setTitle(this.title);
    // console.log('constructor HomeComponent pre ' + this.isLogged);
    this.subscription = this.auth.getSub().subscribe(x => {
      this.isLogged = x;
      if (x === true) {
        this.loggedUser = localStorage.getItem('user');
      } else {
        localStorage.removeItem('user');
      }
      // console.log('constructor HomeComponent getSub().subscribe ' + this.isLogged);
    });
    // console.log('constructor HomeComponent post ' + this.isLogged);
  }
  ngOnInit(): void {
    // console.log('# HomeController.ngOninit START');
    this.subscriptionRoute = this.route.queryParams.subscribe(params => {
      // console.log('inside_Route', params, params.doLogin, params['doLogin']);
      // this.doLogin = params['doLogin'];
      if (params.doLogin == 'true') {
        // console.log('inside_DoLogin');
        this.openLoginDialogReactive();
      }
    });
  }
  openLoginDialogTemplate(): void {
    const dialogRef = this.dialog.open(TestDialogComponent, {
      maxWidth: '600px', hasBackdrop: true,
      data: {name: this.name, animal: this.animal}
    });
    dialogRef.afterClosed().subscribe(result => {
      // console.log('openLoginDialogTemplate afterClosed().subscribe');
    });
  }
  ngOnDestroy(): void {
    this.subscription.unsubscribe();
    this.subscriptionRoute.unsubscribe();
  }
  openLoginDialogReactive(): void {
    if (this.dialogRef) { // if dialog exists
      return;
    }
    this.dialogRef = this.dialog.open(LoginDialogComponent, {
      maxWidth: '600px',
      autoFocus: true,
      disableClose: false, // Esc key will close it
      hasBackdrop: false, // clicking outside wont close it
    });
    // Settings what to do when dialog is closed
    this.dialogRef.afterClosed().subscribe(result => {
        this.dialogRef = undefined;
      // console.log('openLoginDialogReactive afterClosed().subscribe', result);
      }
    );
  }
  clickLoginLogout() {
    if (this.isLogged) {
      console.log('logout');
      this.auth.logout();
      this.router.navigateByUrl('/home');
    } else {
      console.log('login');
      // navigando su doLogin apre in automatico la dialog in ngOnInit
      this.router.navigateByUrl('/home?doLogin=true');
      console.log(this.dialogRef);
      this.openLoginDialogReactive();
    }
  }
}

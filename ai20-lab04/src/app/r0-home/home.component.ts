import {Component, OnDestroy, OnInit} from '@angular/core';
import {Course} from '../models/course.model';
import {Title} from '@angular/platform-browser';
import {MatDialog} from '@angular/material/dialog';
import {AuthService} from '../services/auth.service';
import {ActivatedRoute, NavigationEnd, Router} from '@angular/router';
import {Subscription} from 'rxjs';
import {TestDialogComponent} from '../dialogs/test-dialog/test-dialog.component';
import {LoginComponent} from '../dialogs/login/login.component';
import {CourseService} from '../services/course-service';
import {filter, map, mergeMap, tap} from 'rxjs/operators';

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
  courses: Course[] = null;
  isLogged = false;
  loggedUser = '';
  subscription: Subscription;
  subscriptionRoute: Subscription;
  dialogRef = undefined;

  animal: string;
  name: string;
  nameActiveCourse: any;

  constructor(private titleService: Title,
              private courseService: CourseService,
              public dialog: MatDialog,
              private auth: AuthService,
              private router: Router,
              private route: ActivatedRoute) {
    titleService.setTitle(this.title);
    // // Debug
    // this.router.events.subscribe((event => console.log('Event:', event)));
    this.router.events
      .pipe(
        filter((event) => event instanceof NavigationEnd),
        tap(paramMap => console.log('NavEnd')),
        map(() => this.route),
        tap(r => console.log(r)),
        tap(r => console.log('test', r.firstChild.firstChild.firstChild.firstChild)),
        // tap(r => console.log(r.firstChild.snapshot.params)),
        // tap(r => console.log(r.firstChild.firstChild.snapshot.params)),
        // tap(r => console.log('working?', r.firstChild.firstChild.firstChild.snapshot.params)),
        // tap(r => console.log('test', r.firstChild.firstChild.firstChild.firstChild.snapshot.params)),
        map((rout) => {
          return rout.firstChild.firstChild.firstChild.firstChild;
        }),
        mergeMap((rout) => rout.params),
        tap(paramMap => console.log('params', paramMap))
      ).subscribe((paramMap) => {
      // this.courses = courseService.getCoursesSnapshot();
      courseService.getCourses().subscribe(x => {
        console.log('CoursesLoaded');
        this.courses = x;
        console.log(paramMap);
        // const idActiveCourse = +paramAsMap.get('id');
        const idActiveCourse = +paramMap.id;
        console.log(idActiveCourse);
        for (const course of this.courses) {
          if (course.id == idActiveCourse) {
            this.nameActiveCourse = course.fullName;
          }
        }

      });
    }); // Get the params (paramAsMap.params) and use them to highlight or everything that meet your need

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
      if (params.doLogin === 'true') {
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
    this.dialogRef = this.dialog.open(LoginComponent, {
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
  editCourse() {

  }
}

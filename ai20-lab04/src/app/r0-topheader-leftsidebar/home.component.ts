import {Component, OnDestroy, OnInit} from '@angular/core';
import {Course} from '../models/course.model';
import {Title} from '@angular/platform-browser';
import {MatDialog} from '@angular/material/dialog';
import {AuthService} from '../services/auth.service';
import {ActivatedRoute, NavigationEnd, Router} from '@angular/router';
import {of, Subscription} from 'rxjs';
import {LoginComponent} from '../dialogs/login/login.component';
import {CourseService} from '../services/course.service';
import {filter, map, mergeMap} from 'rxjs/operators';
import {CourseEditComponent} from '../dialogs/course-edit/course-edit.component';
import {Alert, AlertsService} from '../services/alerts.service';
import {AppSettings} from '../app-settings';
import {CourseDeleteComponent} from '../dialogs/course-delete/course-delete.component';

export interface dialogCourseData {
  courseId: string,
  courseName: string,
}
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit, OnDestroy {
  title = 'VirtualLabs';
  courses: Course[] = null;
  nameActiveCourse: string; // used for toolbar, dialog
  idActiveCourse: string; // dialog
  dialogRef = undefined;
  panelOpenState = [];
  authSubscription: Subscription;
  loggedUser = '';
  isLogged = false;
  routeSubscription: Subscription;
  alertsSubscription: Subscription;
  alertMessage: Alert; // ngb-alert
  testingPageEnabled = AppSettings.devShowTestingComponents;
  forseCoursesUpdate = false;

  dontExpandPanelOnNameClick(i: number) {
    this.panelOpenState[i] = !this.panelOpenState[i];
  }
  constructor(private titleService: Title,
              private courseService: CourseService,
              public dialog: MatDialog,
              private authService: AuthService,
              private router: Router,
              private route: ActivatedRoute,
              private alertsService: AlertsService
  ) {
    titleService.setTitle(this.title);
    courseService.getCourses().subscribe(x => this.courses = x);

    // At every routing change, update nameActiveCourse (top toolbar) and idActiveCourse, plus some resets/refresh/checks
    this.router.events
      .pipe(
        filter((event) => event instanceof NavigationEnd),
        map(() => this.route),
        map((rout) => {
          // Moving to params child route (StudentsContComponent)
          // return rout?.firstChild?.firstChild?.firstChild?.firstChild; // --> without "paramsInheritanceStrategy: 'always'"
          let lastchild: ActivatedRoute = rout;
          while (lastchild.firstChild != null) {
            lastchild = lastchild.firstChild;
          }
          return lastchild; // -> last child will have all the params inherited
        }),
        mergeMap((rout) => (rout != null) ? rout?.paramMap : of(null))
      ).subscribe((paramMap) => {
        if (paramMap == null || this.courses == null) {
          this.nameActiveCourse = '';
        } else {
          const oldCourseId = this.idActiveCourse;
          this.idActiveCourse = paramMap.get('id');
          for (const course of this.courses) {
            // tslint:disable-next-line:triple-equals
            if (course.id == this.idActiveCourse) { // dont use === here
              this.nameActiveCourse = course.fullName;
            }
          }
          if (oldCourseId != this.idActiveCourse && this.idActiveCourse != null) { // reset alert on course change
            this.alertsService.setAlert(null);
          }
          if (this.forseCoursesUpdate === true) { // called after course delete
            this.forseCoursesUpdate = false;
            courseService.getCourses().subscribe(x => this.courses = x);
          }
          if (this.idActiveCourse != oldCourseId) { // closing all panels
            for (let i = 0; i < this.panelOpenState.length; i++) {
              this.panelOpenState[i] = false;
            }
          }
        }
      }
    );
    this.authSubscription = this.authService.getIsLoggedSubject().subscribe(x => {
      this.isLogged = x;
      if (x === true) {
        this.loggedUser = localStorage.getItem('user');
      } else {
        localStorage.removeItem('user');
      }
    });
    this.alertsSubscription = this.alertsService.getAlertSubject().subscribe(x => this.alertMessage = x);
  }
  ngOnInit(): void {
    this.routeSubscription = this.route.queryParams.subscribe(params => {
      if (params.doLogin === 'true') {
        this.openLoginDialogReactive();
      }
    });
  }
  openEditCourseDialog(): void {
    const dialogRef = this.dialog.open(CourseEditComponent, {
      maxWidth: '600px', autoFocus: true, hasBackdrop: false, disableClose: false, closeOnNavigation: true,
      data: {courseName: this.nameActiveCourse, courseId: this.idActiveCourse}
    });
    dialogRef.afterClosed().subscribe((res: string) => {
        this.dialogRef = null;
        if (res == 'success') {
          this.alertsService.setAlert({type: 'success', message: 'Course update successful!'});
          this.router.navigateByUrl('/'); // refreshing data
        } else if (res != undefined) {
          this.alertsService.setAlert({type: 'danger', message: 'Couldn\'t update course! ' + res});
        }
      }, error => this.alertsService.setAlert({type: 'danger', message: 'Dialog Error!'})
    );
  }
  openDeleteCourseDialog(): void {
    const dialogRef = this.dialog.open(CourseDeleteComponent, {
      maxWidth: '600px', autoFocus: true, hasBackdrop: false, disableClose: false, closeOnNavigation: true,
      data: {courseName: this.nameActiveCourse, courseId: this.idActiveCourse}
    });
    dialogRef.afterClosed().subscribe((res: string) => {
        this.dialogRef = null;
        if (res == 'success') {
          this.alertsService.setAlert({type: 'success', message: 'Course delete successful!'});
          this.forseCoursesUpdate = true;
          this.router.navigateByUrl('/'); // refreshing data
        } else if (res != undefined) {
          this.alertsService.setAlert({type: 'danger', message: 'Couldn\'t delete course! ' + res});
        }
      }, error => this.alertsService.setAlert({type: 'danger', message: 'Dialog Error!'})
    );
  }
  openLoginDialogReactive(): void {
    if (this.dialogRef) { // if dialog exists
      return;
    }
    this.dialogRef = this.dialog.open(LoginComponent, {
      maxWidth: '600px', autoFocus: true,
      disableClose: false, // Esc key will close it
      hasBackdrop: false, // clicking outside wont close it
    });
    // Settings what to do when dialog is closed
    this.dialogRef.afterClosed().subscribe(() => {
      this.dialogRef = null;
      }
    );
  }
  clickLoginLogout() {
    if (this.isLogged) {
      console.log('logout');
      this.authService.logout();
      this.router.navigateByUrl('/home');
    } else {
      console.log('login');
      // navigando su doLogin apre in automatico la dialog in ngOnInit
      this.router.navigateByUrl('/home?doLogin=true');
      console.log(this.dialogRef);
      this.openLoginDialogReactive();
    }
  }
  closeAlert() {
    this.alertMessage = null;
  }
  ngOnDestroy(): void {
    this.authSubscription.unsubscribe();
    this.routeSubscription.unsubscribe();
  }
}

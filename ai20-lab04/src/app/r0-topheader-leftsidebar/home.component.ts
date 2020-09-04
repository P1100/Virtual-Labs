import {ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import {Course} from '../models/course.model';
import {Title} from '@angular/platform-browser';
import {MatDialog, MatDialogRef, MatDialogState} from '@angular/material/dialog';
import {AuthService} from '../services/auth.service';
import {ActivatedRoute, NavigationEnd, Router} from '@angular/router';
import {Observable, Subscription} from 'rxjs';
import {LoginComponent} from '../dialogs/login/login.component';
import {CourseService} from '../services/course.service';
import {filter, map, startWith, take, tap} from 'rxjs/operators';
import {CourseEditComponent} from '../dialogs/course-edit/course-edit.component';
import {Alert, AlertsService} from '../services/alerts.service';
import {AppSettings} from '../app-settings';
import {CourseDeleteComponent} from '../dialogs/course-delete/course-delete.component';
import {CourseAddComponent} from '../dialogs/course-add/course-add.component';
import {RegisterComponent} from '../dialogs/register/register.component';
import {ImageService} from '../services/image.service';

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
  courses: Course[] = [];
  nameActiveCourse: string; // used for toolbar, dialog
  idActiveCourse: string; // dialog
  loggedUserName = undefined;
  isLogged = undefined;
  role = 'anonymous';
  dialogRef: MatDialogRef<any>;
  panelOpenState = [];
  authSubscription: Subscription;
  routeSubscription: Subscription;
  routeQueryMapSubscription: Subscription;
  alertsSubscription: Subscription;
  alertNgb: Alert; // ngb-alert
  devShowTestingComponents = AppSettings.devtest;
  coursesObservable: Observable<Course[]>;
  retrievedImage: string;

  private obsUpdateCourses = this.courseService.getCourses(); // no parameters -> reusable
  private coursesLoadedOrUpdated: Promise<unknown>;
  private authBootstrapInitPromise: Promise<unknown>;

  constructor(private titleService: Title,
              private courseService: CourseService,
              public dialog: MatDialog,
              private authService: AuthService,
              private router: Router,
              private route: ActivatedRoute,
              private alertsService: AlertsService,
              private imageService: ImageService,
              private changeDetectorRef: ChangeDetectorRef
  ) {
    titleService.setTitle(this.title);
    for (let i = 0; i < this.panelOpenState.length; i++) {
      this.panelOpenState[i] = false;
    }

    this.authBootstrapInitPromise = new Promise((resolve, reject) => {
      // Updates tool welcome message, post login refresh
      this.authService.getIsLoggedSubject().pipe(take(1)).subscribe(initStatus => {
        console.log('INIT', initStatus, this.isLogged);
        if (initStatus == true) {
          this.role = localStorage.getItem('role');
          this.loggedUserName = this.role + ' ' + localStorage.getItem('username');
          this.obsUpdateCourses.subscribe(courses => {
              this.courses = courses;
              resolve('Bootstrap: Logged in, courses initialized!');
            },
            e => {
              this.alertsService.setAlert('danger', 'Couldn\'t init courses. ' + e);
            });
        } else {
          resolve('Bootstrap: not logged in, courses not initialized');
        }
      });
    });
    // Necessary to have courses loaded on routing update, after bootstrap
    this.authBootstrapInitPromise.then(value => {
      this.setupCourseIdRoutingUpdateLogic();
      this.setupAuthUpdateLogic();
    });
  }
  private setupAuthUpdateLogic() {
    console.log('AUTO SUB OUT', this.isLogged);
    this.authSubscription = this.authService.getIsLoggedSubject().subscribe(newLogStatus => {
      console.log('AUTO SUB IN', this.isLogged, newLogStatus);
      const previousLoggedStatus = this.isLogged;
      if (previousLoggedStatus === false && newLogStatus === true) { // login
        console.log('AUTH LOGIN');
        this.role = localStorage.getItem('role');
        this.loggedUserName = this.role + ' ' + localStorage.getItem('username');
        this.obsUpdateCourses.subscribe(x => {
            this.courses = x;
            this.router.navigateByUrl('/loggedin');
          },
          error => {
            this.alertsService.setAlert('danger', 'Couldn\'t update courses after login! ' + error);
          });
      } else if (previousLoggedStatus === true && newLogStatus === false) { // logout
        console.log('AUTH LOGOUT');
        this.idActiveCourse = null;
        this.courses = [];
        this.nameActiveCourse = null;
        this.loggedUserName = '';
        this.role = 'anonymous';
        this.router.navigateByUrl('/loggedout');
      }
      this.isLogged = newLogStatus;
    });
  }
  ngOnInit(): void {
    this.alertsSubscription = this.alertsService.getAlertSubject().subscribe(x => this.alertNgb = x);
    this.routeQueryMapSubscription = this.route.queryParams.subscribe(params => {
      if (params.doLogin === 'true') {
        this.openLoginDialogReactive();
      }
    });
  }
  private setupCourseIdRoutingUpdateLogic() {
    console.log('INIT ROUTING', this.isLogged);
    // At every routing change, update nameActiveCourse (top toolbar) and idActiveCourse, plus some resets/refresh/checks
    const routeSubscription = this.router.events.pipe(
      startWith('firstTimeRun'),
      tap(x => console.log('INSIDE FIRST TIME')),
      filter((event) => event instanceof NavigationEnd || event == 'firstTimeRun'),
      tap(event => console.log('ROUTING: INSIDE NAVEND OR FIRST TIME', event)),
      map(() => {
        let lastchild: ActivatedRoute = this.route;
        while (lastchild.firstChild != null) {
          lastchild = lastchild.firstChild;
        }
        return lastchild.snapshot.paramMap; // -> last child will have all the params inherited (paramsInheritanceStrategy: 'always'")
      }),
    ).subscribe((paramMap) => {
        const oldCourseId = this.idActiveCourse;
        this.idActiveCourse = paramMap.get('id');
        this.nameActiveCourse = null;
        console.log('UPDATE ROUTING', this.courses.length, this.idActiveCourse, this.nameActiveCourse);
        this.updateToolbarCourseName();
        /* Closing all panels */
        // if (this.idActiveCourse != oldCourseId) {
        //   for (let i = 0; i < this.panelOpenState.length; i++) {
        //     this.panelOpenState[i] = false;
        //   }
        // }
        /* Reset alerts */
        if (this.idActiveCourse != oldCourseId && this.isLogged && this.idActiveCourse != null) {
          this.alertsService.closeAlert();
        }
      }
    );
  }
  private updateToolbarCourseName() {
    if (this.idActiveCourse != null) {
      for (const course of this.courses) {
        if (course.id == this.idActiveCourse) { // dont use === here
          this.nameActiveCourse = course.fullName;
        }
      }
    }
  }
  dontExpandPanelOnNameClick(i: number) {
    this.panelOpenState[i] = !this.panelOpenState[i];
    this.changeDetectorRef.detectChanges();
  }
  openAddCourseDialog(): void {
    if (this.dialogRef?.getState() == MatDialogState.OPEN) {
      throw new Error('Dialog stil open while opening a new one');
    }
    this.dialogRef = this.dialog.open(CourseAddComponent, {
      maxWidth: '400px', autoFocus: true, hasBackdrop: true, disableClose: true, closeOnNavigation: true
    });
    this.dialogRef.afterClosed().subscribe((res: string) => {
        if (res != undefined) {
          this.obsUpdateCourses.subscribe(x => this.courses = x);
        }
      }, () => this.alertsService.setAlert('danger', 'Add Course Dialog Error')
    );
  }
  openEditCourseDialog(): void {
    if (this.dialogRef?.getState() == MatDialogState.OPEN) {
      throw new Error('Dialog stil open while opening a new one');
    }
    this.dialogRef = this.dialog.open(CourseEditComponent, {
      maxWidth: '400px', autoFocus: true, hasBackdrop: true, disableClose: true, closeOnNavigation: true,
      data: {courseName: this.nameActiveCourse, courseId: this.idActiveCourse}
    });
    this.dialogRef.afterClosed().subscribe((res: string) => {
      }, error => this.alertsService.setAlert('danger', 'Edit Course Dialog Error!')
    );
  }
  openDeleteCourseDialog(): void {
    if (this.dialogRef?.getState() == MatDialogState.OPEN) {
      throw new Error('Dialog stil open while opening a new one');
    }
    this.dialogRef = this.dialog.open(CourseDeleteComponent, {
      maxWidth: '400px', autoFocus: false, hasBackdrop: true, disableClose: false, closeOnNavigation: true,
      data: {courseName: this.nameActiveCourse, courseId: this.idActiveCourse}
    });
    this.dialogRef.afterClosed().subscribe((res: string) => {
        if (res != undefined) {
          this.obsUpdateCourses.subscribe(x => this.courses = x);
        }
      }, error => this.alertsService.setAlert('danger', 'Delete Course Dialog Error!')
    );
  }
  openLoginDialogReactive(): void {
    if (this.dialogRef?.getState() == MatDialogState.OPEN) {
      throw new Error('Dialog stil open while opening a new one');
    }
    this.dialogRef = this.dialog.open(LoginComponent, {
      maxWidth: '400px', autoFocus: true, hasBackdrop: true, disableClose: true, closeOnNavigation: false
    });
    // Settings what to do when dialog is closed
    this.dialogRef.afterClosed().subscribe((res: string) => {
      }, error => this.alertsService.setAlert('danger', 'Login Dialog Error!')
    );
  }
  openRegisterDialog() {
    if (this.dialogRef?.getState() == MatDialogState.OPEN) {
      throw new Error('Dialog stil open while opening a new one');
    }
    this.dialogRef = this.dialog.open(RegisterComponent, {
      maxWidth: '400px', autoFocus: true, hasBackdrop: true, disableClose: true, closeOnNavigation: true
    });
    this.dialogRef.afterClosed().subscribe((idImage: number) => {
        if (idImage > 0) {
          this.imageService.getImage(idImage).subscribe(imageDto => this.retrievedImage = imageDto.imageStringBase64);
        }
      }, () => this.alertsService.setAlert('danger', 'Registration Dialog Error')
    );
  }
  login() {
    this.router.navigateByUrl('/home?doLogin=true');
  }
  logout() {
    console.log('LOGOUT');
    this.authService.logout();
  }
  closeAlert() {
    this.alertNgb = null;
  }
  ngOnDestroy(): void {
    this.authSubscription?.unsubscribe();
    this.routeSubscription?.unsubscribe();
    this.routeQueryMapSubscription?.unsubscribe();
    this.alertsSubscription?.unsubscribe();
  }
}

import {ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import {Course} from '../models/course.model';
import {Title} from '@angular/platform-browser';
import {MatDialog, MatDialogRef, MatDialogState} from '@angular/material/dialog';
import {AuthService} from '../services/auth.service';
import {ActivatedRoute, NavigationEnd, Router} from '@angular/router';
import {Observable, Subscription} from 'rxjs';
import {LoginComponent} from '../dialogs/login/login.component';
import {CourseService} from '../services/course.service';
import {filter, map} from 'rxjs/operators';
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
  courses: Course[] = null;
  nameActiveCourse: string; // used for toolbar, dialog
  idActiveCourse: string; // dialog
  dialogRef: MatDialogRef<any>;
  panelOpenState = [];
  authSubscription: Subscription;
  loggedUserName = '';
  isLogged = false;
  routeSubscription: Subscription;
  alertsSubscription: Subscription;
  alertNgb: Alert; // ngb-alert
  devShowTestingComponents = AppSettings.devtest;
  coursesObservable: Observable<Course[]>;
  retrievedImage: string;
  role = 'anonymous';

  private obsUpdateCourses = this.courseService.getCourses(); // no parameters -> reusable
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
    this.obsUpdateCourses.subscribe(courses => {
      this.courses = courses;
    });
    const promise = new Promise((resolve, reject) => {
      if (this.isLogged) {
        this.obsUpdateCourses.subscribe(courses => {
            this.courses = courses;
            resolve('Courses initialized!');
          },
          e => {
            this.alertsService.setAlert('danger', 'Couldn\'t init courses. ' + e);
          });
      } else {
        resolve('User not logged');
      }
    });
    // At every routing change, update nameActiveCourse (top toolbar) and idActiveCourse, plus some resets/refresh/checks
    this.router.events
      .pipe(
        filter(() => this.isLogged),
        filter((event) => event instanceof NavigationEnd),
        map(() => this.route),
        map((rout) => {
          let lastchild: ActivatedRoute = rout;
          while (lastchild.firstChild != null) {
            lastchild = lastchild.firstChild;
          }
          return lastchild; // -> last child will have all the params inherited (paramsInheritanceStrategy: 'always'")
        }),
        map(rout => rout.snapshot.paramMap)
      ).subscribe((paramMap) => {
      // Wait for courses to be initialized (init)
      promise.then(() => {
        const oldCourseId = this.idActiveCourse;
        if (this.courses == null || paramMap?.get('id') == null) {
          this.nameActiveCourse = null;
          this.idActiveCourse = null;
        } else {
          this.idActiveCourse = paramMap.get('id');
          for (const course of this.courses) {
            // tslint:disable-next-line:triple-equals
            if (course.id == this.idActiveCourse) { // dont use === here
              this.nameActiveCourse = course.fullName;
            }
          }
        }
        if (this.idActiveCourse != oldCourseId && this.idActiveCourse != null) { // reset alerts
          this.alertsService.closeAlert();
        }
        if (this.idActiveCourse != oldCourseId) { // closing all panels,
          for (let i = 0; i < this.panelOpenState.length; i++) {
            this.panelOpenState[i] = false;
          }
        }
      });
    });
    this.authSubscription = this.authService.getIsLoggedSubject().subscribe(newLogStatus => {
      const previousLoggedStatus = this.isLogged;
      this.isLogged = newLogStatus;
      if (newLogStatus == true) {
        this.role = localStorage.getItem('role');
        this.loggedUserName = this.role + ' ' + localStorage.getItem('username');
        if (previousLoggedStatus == false && newLogStatus == true) {
          this.obsUpdateCourses.subscribe(x => this.courses = x,
            e => {
              this.alertsService.setAlert('danger', 'Couldn\'t update courses after login! ' + e);
              localStorage.clear();
            });
        }
      } else {
        this.loggedUserName = null;
        this.role = 'anonymous';
      }
    });
    this.alertsSubscription = this.alertsService.getAlertSubject().subscribe(x => this.alertNgb = x);
  }
  ngOnInit(): void {
    this.routeSubscription = this.route.queryParams.subscribe(params => {
      if (params.doLogin === 'true') {
        this.openLoginDialogReactive();
      }
    });
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
  clickLoginLogout() {
    if (this.isLogged) {
      this.authService.logout();
      // this.router.navigateByUrl('/home');
    } else {
      // navigando su doLogin apre in automatico la dialog in ngOnInit
      // this.router.navigateByUrl('/home?doLogin=true');
      this.openLoginDialogReactive();
    }
  }
  closeAlert() {
    this.alertNgb = null;
  }
  ngOnDestroy(): void {
    this.authSubscription.unsubscribe();
    this.routeSubscription.unsubscribe();
    this.alertsSubscription.unsubscribe();
  }
}

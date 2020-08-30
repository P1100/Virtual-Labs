import {ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import {Course} from '../models/course.model';
import {Title} from '@angular/platform-browser';
import {MatDialog} from '@angular/material/dialog';
import {AuthService} from '../services/auth.service';
import {ActivatedRoute, NavigationEnd, Router} from '@angular/router';
import {Observable, of, Subscription} from 'rxjs';
import {LoginComponent} from '../dialogs/login/login.component';
import {CourseService} from '../services/course.service';
import {filter, map, switchMap} from 'rxjs/operators';
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
  dialogRef = undefined;
  panelOpenState = [];
  authSubscription: Subscription;
  loggedUser = '';
  isLogged = false;
  routeSubscription: Subscription;
  alertsSubscription: Subscription;
  alertNgb: Alert; // ngb-alert
  devShowTestingComponents = AppSettings.devShowTestingComponents;
  forseCoursesUpdate = false;
  coursesObservable: Observable<Course[]>;
  retrievedImage: string;

  dontExpandPanelOnNameClick(i: number) {
    this.panelOpenState[i] = !this.panelOpenState[i];
    this.changeDetectorRef.detectChanges();
  }
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
    const coursesObservable = courseService.getCourses();
    coursesObservable.subscribe(x => this.courses = x); // for toPromise
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
        switchMap((rout) => (rout != null) ? rout?.paramMap : of(null)) // debug_note: it was mergeMap
      ).subscribe((paramMap) => {
      // Wait for courses to be updated
      coursesObservable.toPromise().then(() => {
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
    this.authSubscription = this.authService.getIsLoggedSubject().subscribe(x => {
      this.isLogged = x;
      if (x === true) {
        this.loggedUser = localStorage.getItem('user');
      } else {
        localStorage.removeItem('user');
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
  openRegisterDialog() {
    if (this.dialogRef) {
      return;
    }
    const dialogRef = this.dialog.open(RegisterComponent, {
      maxWidth: '800px', autoFocus: true, hasBackdrop: true, disableClose: true, closeOnNavigation: true
    });
    dialogRef.afterClosed().subscribe((idImage: string) => {
        this.dialogRef = null;
        this.imageService.getImage(idImage).subscribe(imageDto => this.retrievedImage = imageDto.imageStringBase64);
      }, () => this.alertsService.setAlert('danger', 'Dialog Error')
    );
  }
  openAddCourseDialog(): void {
    if (this.dialogRef) {
      return;
    }
    const dialogRef = this.dialog.open(CourseAddComponent, {
      maxWidth: '800px', autoFocus: true, hasBackdrop: true, disableClose: true, closeOnNavigation: true
    });
    dialogRef.afterClosed().subscribe((res: string) => {
      this.dialogRef = null;
      if (res != undefined) {
          this.courseService.getCourses().subscribe(x => this.courses = x);
        }
      }, () => this.alertsService.setAlert('danger', 'Dialog Error')
    );
  }
  openEditCourseDialog(): void {
    if (this.dialogRef) {
      return;
    }
    const dialogRef = this.dialog.open(CourseEditComponent, {
      maxWidth: '800px', autoFocus: true, hasBackdrop: true, disableClose: true, closeOnNavigation: true,
      data: {courseName: this.nameActiveCourse, courseId: this.idActiveCourse}
    });
    dialogRef.afterClosed().subscribe((res: string) => {
        this.dialogRef = null;
      }, error => this.alertsService.setAlert('danger', 'Dialog Error!')
    );
  }
  openDeleteCourseDialog(): void {
    if (this.dialogRef) {
      return;
    }
    const dialogRef = this.dialog.open(CourseDeleteComponent, {
      maxWidth: '800px', autoFocus: false, hasBackdrop: true, disableClose: false, closeOnNavigation: true,
      data: {courseName: this.nameActiveCourse, courseId: this.idActiveCourse}
    });
    dialogRef.afterClosed().subscribe((res: string) => {
        this.dialogRef = null;
        if (res != undefined) {
          this.courseService.getCourses().subscribe(x => this.courses = x);
        }
      }, error => this.alertsService.setAlert('danger', 'Dialog Error!')
    );
  }
  openLoginDialogReactive(): void {
    if (this.dialogRef) {
      return;
    }
    this.dialogRef = this.dialog.open(LoginComponent, {
      maxWidth: '800px', autoFocus: true, hasBackdrop: true, disableClose: true, closeOnNavigation: false
    });
    // Settings what to do when dialog is closed
    this.dialogRef.afterClosed().subscribe(() => this.dialogRef = null);
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
    this.alertNgb = null;
  }
  ngOnDestroy(): void {
    this.authSubscription.unsubscribe();
    this.routeSubscription.unsubscribe();
    this.alertsSubscription.unsubscribe();
  }
}

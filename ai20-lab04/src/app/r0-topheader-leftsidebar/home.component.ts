import {ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
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
import {DeletetestComponent} from '../dialogs/deletetest/deletetest.component';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit, OnDestroy {
  title = 'VirtualLabs';
  courses: Course[] = null;
  nameActiveCourse: string;
  idActiveCourse: string;
  isLogged = false;
  loggedUser = '';
  subscription: Subscription;
  subscriptionRoute: Subscription;
  dialogRef = undefined;

  panelOpenState = [];

  dontExpandPanelOnNameClick(i: number) {
    this.panelOpenState[i] = !this.panelOpenState[i];
    this.cdref.detectChanges(); // Needed to avoid ExpressionChangedAfterItHasBeenCheckedError
  }

  constructor(private titleService: Title,
              private courseService: CourseService,
              public dialog: MatDialog,
              private auth: AuthService,
              private router: Router,
              private route: ActivatedRoute,
              private cdref: ChangeDetectorRef
  ) {
    titleService.setTitle(this.title);
    courseService.getCourses().subscribe(x => this.courses = x);

    // At every routing change, update nameActiveCourse (top toolbar)
    this.router.events
      .pipe(
        filter((event) => event instanceof NavigationEnd),
        map(() => this.route),
        map((rout) => {
          // Moving to params child route (StudentsContComponent) --> without "paramsInheritanceStrategy: 'always'"
          // return rout?.firstChild?.firstChild?.firstChild?.firstChild;
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
          const oldId = this.idActiveCourse;
          this.idActiveCourse = paramMap.get('id');
          for (const course of this.courses) {
            // tslint:disable-next-line:triple-equals
            if (course.id == this.idActiveCourse) { // dont use === here
              this.nameActiveCourse = course.fullName;
            }
          }
        }
      }
    );

    this.subscription = this.auth.getSubscriptionSubject().subscribe(x => {
      this.isLogged = x;
      if (x === true) {
        this.loggedUser = localStorage.getItem('user');
      } else {
        localStorage.removeItem('user');
      }
    });
  }
  ngOnInit(): void {
    this.subscriptionRoute = this.route.queryParams.subscribe(params => {
      if (params.doLogin === 'true') {
        this.openLoginDialogReactive();
      }
    });
  }
  openEditCourseDialog(): void {
    const dialogRef = this.dialog.open(CourseEditComponent, {
      maxWidth: '900px', autoFocus: true, hasBackdrop: false, disableClose: false, closeOnNavigation: true,
      data: {courseName: this.nameActiveCourse, id: this.idActiveCourse}
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log(result);
      this.dialogRef = null;
      if (result === 'refresh') {
        this.router.navigateByUrl('/'); // refreshing data
      }
    });
  }
  // TODO: remove later
  openTestDialog(): void {
    const dialogRef = this.dialog.open(DeletetestComponent, {
      maxWidth: '900px', autoFocus: true, hasBackdrop: false, disableClose: false, closeOnNavigation: true
    });
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
  ngOnDestroy(): void {
    this.subscription.unsubscribe();
    this.subscriptionRoute.unsubscribe();
  }
}

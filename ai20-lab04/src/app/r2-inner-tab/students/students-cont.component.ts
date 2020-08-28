import {Component, ElementRef, OnDestroy, ViewChild} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {concatMap, toArray} from 'rxjs/operators';
import {StudentService} from '../../services/student.service';
import {from, Observable, Subscription} from 'rxjs';
import {Student} from '../../models/student.model';
import {getSafeDeepCopyArray} from '../../app-settings';
import {AlertsService} from '../../services/alerts.service';
import {CourseService} from '../../services/course.service';

/* API:
* - Data taken from backend service: one new request each time active route changes.
* --- Then passed to student component by html-template property binding
* - Angular events are used to receive commands from the child student component
*/
@Component({
  selector: 'app-students-cont',
  template: `
    <app-students [enrolled]="enrolledStudents"
                  [students]="allStudents"
                  (enrolledEvent)="onStudentsToEnroll($event)"
                  (disenrolledEvent)="onStudentsToDisenroll($event)"
                  (uploadCsvEvent)="onCsvUpload($event)"
    ></app-students>
  `,
  styleUrls: []
})
export class StudentsContComponent implements OnDestroy {
  // updated after every route change, inside constructor
  allStudents: Student[] = [];
  enrolledStudents: Student[] = [];
  courseId = '0';
  // constructor subscriptions to paramMap
  subAllStudents: Subscription = null;
  subEnrolledStudentsCourse: Subscription = null;
  subRouteParam: Subscription = null;

  @ViewChild('labelFileCsv')
  labelFileName: ElementRef;

  // Routing change update (e.g. when changing course)
  constructor(private backendService: StudentService, private activatedRoute: ActivatedRoute, private alertsService: AlertsService
    , private courseService: CourseService) {
    this.subRouteParam = this.activatedRoute.paramMap.subscribe(() => {
      this.courseId = this.activatedRoute.parent.snapshot.paramMap.get('id');
      console.log('activeCourse: ' + this.courseId);
      this.subEnrolledStudentsCourse = this.backendService.getEnrolledStudents(this.courseId)
        .subscribe((students: Student[]) => {
            this.enrolledStudents = Array.isArray(students) ? [...students] : [];
          }, error => this.alertsService.setAlert({type: 'danger', message: 'Error: couldn\'t get enrolled students!'})
        );
      this.subAllStudents = this.backendService.getAllStudents()
        .subscribe((students: Student[]) => {
              this.allStudents = Array.isArray(students) ? [...students] : [];
            }, error => this.alertsService.setAlert({type: 'danger', message: 'Error: couldn\'t get students list!'})
          );
      }
    );
  }
  ngOnDestroy(): void {
    this.subRouteParam.unsubscribe();
    this.subAllStudents.unsubscribe();
    this.subEnrolledStudentsCourse.unsubscribe();
  }

  onStudentsToEnroll(studentsToEnroll: Student[]) {
    console.log('enroll', studentsToEnroll);
    if (studentsToEnroll === null || studentsToEnroll.length === 0) {
      this.alertsService.setAlert({type: 'danger', message: 'Couldn\'t enroll! Null parameters'});
      return;
    }
    const observable: Observable<Student[]> = from([...studentsToEnroll])
      .pipe(
        concatMap((student: Student) =>
          this.backendService.enroll(student, this.courseId) as Observable<any>
        ),
        toArray()  // so it waits for all inner observables to collect
      ) as Observable<any>;
    this.updateEnrolledStudents(observable, 'enroll');
  }
  onStudentsToDisenroll(studentsToDisenroll: Student[]) {
    if (studentsToDisenroll === null || studentsToDisenroll.length === 0) {
      this.alertsService.setAlert({type: 'danger', message: 'Couldn\'t disenroll! Null parameters'});
      return;
    }
    const observable: Observable<Student[]> = from(studentsToDisenroll).pipe(
      concatMap((student: Student) =>
        this.backendService.disenroll(student, this.courseId) as Observable<any>
      ),
      toArray()  // so it waits for all inner observables to collect
    ) as Observable<any>;
    this.updateEnrolledStudents(observable, 'disenroll');
  }
  // Updating table data, basically
  private updateEnrolledStudents(o: any, message: string) {
    o.subscribe(() => {
      this.backendService.getEnrolledStudents(this.courseId).subscribe(
        (ss: Student[]) => {
          this.enrolledStudents = getSafeDeepCopyArray(ss);
          this.alertsService.setAlert({type: 'success', message: `Student ${message}ed!`});
        }, error => this.alertsService.setAlert({type: 'danger', message: `Couldn\'t update enrolled students! ${error}`}));
    }, error => this.alertsService.setAlert({type: 'danger', message: `Couldn\'t ${message}! ${error}`}));
  }
  onCsvUpload(selectedFile: File) {
    const uploadCSVData = new FormData();
    uploadCSVData.append('file', selectedFile);
    this.courseService.enrollStudentsCSV(this.courseId, uploadCSVData)
      .subscribe(() => {
          this.backendService.getEnrolledStudents(this.courseId).subscribe(
            (ss: Student[]) => {
              this.enrolledStudents = getSafeDeepCopyArray(ss);
              this.alertsService.setAlert({type: 'success', message: `CSV students enrolled!`});
            }, error => this.alertsService.setAlert({type: 'danger', message: `Couldn\'t update enrolled students! ${error}`}));
        },
        error => this.alertsService.setAlert({type: 'danger', message: `Couldn\'t upload CSV! ${error}`})
      );
  }
}

import {Injectable} from '@angular/core';
import {Student} from '../models/student.model';
import {forkJoin, Observable, throwError} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {catchError, map, retry, tap} from 'rxjs/operators';
import {environment} from '../../environments/environment';
import {AppSettings} from '../app-settings';

@Injectable({
  providedIn: 'root'
})
export class BackendService {
  private baseUrl = environment.baseUrl;

  constructor(private http: HttpClient) {
  }
  private formatErrors(error: any) {
    console.error(error);
    return throwError(error.error);
  }

  // Pipe functions to be pure. No pipe function should create any side effects that persist outside the execution of that function
  getAllStudents(): Observable<Student[]> {
    return this.http.get<any>(`${this.baseUrl}/students`, AppSettings.JSON_HTTP_OPTIONS)
      .pipe(
        map(response => response?._embedded?.studentDTOList),
        map(s => s?.map(ss => {
          const copy = {...ss};
          delete copy?._links;
          delete copy?.links; // only '_links' should show up
          return copy;
        })),
        retry(AppSettings.RETRIES), catchError(this.formatErrors)
        // ,tap(res => console.log('getAllStudents._embedded.studentDTOList', res))
      );
  }
  getEnrolledStudents(courseId: number): Observable<Student[]> {
    return this.http.get<any>(`${this.baseUrl}/courses/${courseId}/enrolled`, AppSettings.JSON_HTTP_OPTIONS)
      .pipe(
        retry(AppSettings.RETRIES), catchError(this.formatErrors),
        map(response => response?._embedded?.studentDTOList),
        map(s => s?.map(ss => {
          const copy = {...ss};
          delete copy?._links;
          delete copy?.links; // only '_links' should show up
          return copy;
        }))
        // ,tap(res => console.log('getEnrolledStudents._embedded.studentDTOList', res))
      );
  }
  enroll(student: Student, courseId: number) {
    console.log('enroll(student: Student, courseId: number)', courseId, student, JSON.stringify(student));
    return this.http.put(
      `${this.baseUrl}/courses/${courseId}/enroll`,
      JSON.stringify(student),
      AppSettings.JSON_HTTP_OPTIONS
    ).pipe(
      tap(res => console.log('enroll(student: Student, courseId: number)', res)),
      retry(AppSettings.RETRIES), catchError(this.formatErrors));
  }
  disenroll(student: Student, courseId: number): Observable<any> {
    console.log('disenroll(student: Student, courseId: number)', courseId, student, JSON.stringify(student));
    return this.http.put(
      `${this.baseUrl}/courses/${courseId}/disenroll/${student.id}`,
      null
    ).pipe(
      tap(s => console.log('disenroll http.put:', s)),
      retry(AppSettings.RETRIES), catchError(this.formatErrors));
  }

  queryAllStudents(queryTitle: string): Observable<Student[]> {
    return this.http.get<Student[]>(`${this.baseUrl}/students?q=${queryTitle}`)
      .pipe(retry(AppSettings.RETRIES), catchError(this.formatErrors));
  }
  deleteStudent(student: Student, courseId: number): Observable<any> {
    return this.http.delete(
      `${this.baseUrl}/students/${student.id}`
    ).pipe(retry(AppSettings.RETRIES), catchError(this.formatErrors));
  }

  enrollStudents(students: Student[], courseId: number) {
    const request$ = new Array<Observable<Student>>();
    students.forEach((student: Student) => request$.push(this.updateStudent(student)));
    return forkJoin(request$);

    // return this.http.post(
    //   `${this.baseUrl}`,
    //   JSON.stringify(students)
    // ).pipe(retry(AppSettings.RETRIES), catchError(this.formatErrors));
  }
  deleteStudents(studentsToRemove: Student[]) {
  }
  queryEnrolledStudents(courseId: number, queryTitle: string): Observable<Student[]> {
    return this.http.get<Student[]>(`${this.baseUrl}/courses/${courseId}/students?q=${queryTitle}`)
      .pipe(retry(AppSettings.RETRIES), catchError(this.formatErrors));
  }
  updateStudent(student: Student, body: object = {}): Observable<any> {
    return this.http.put(
      `${this.baseUrl}`,
      JSON.stringify(body),
      AppSettings.JSON_HTTP_OPTIONS
    ).pipe(retry(AppSettings.RETRIES), catchError(this.formatErrors));
  }
  createStudent(path: string, body: object = {}): Observable<any> {
    return this.http.post(
      `${this.baseUrl}${path}`,
      JSON.stringify(body),
      AppSettings.JSON_HTTP_OPTIONS
    ).pipe(retry(AppSettings.RETRIES), catchError(this.formatErrors));
  }
  // create() {}
  // find() {}
  // updateStudent(student: Student) {
  // }
  // delete(){}
}

import {Injectable} from '@angular/core';
import {Student} from '../models/student.model';
import {forkJoin, Observable, throwError} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {catchError, map, retry, tap} from 'rxjs/operators';
import {environment} from '../../environments/environment';
import {AppSettings, removeHATEOAS} from '../app-settings';
import {HateoasModel} from '../models/hateoas.model';

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

  getAllStudents(): Observable<Student[]> {
    return this.http.get<HateoasModel>(`${this.baseUrl}/students`, AppSettings.JSON_HTTP_OPTIONS)
      .pipe(
        map(object => removeHATEOAS(object)),
        retry(AppSettings.RETRIES), catchError(this.formatErrors),
        tap(res => console.log('--getAllStudents:', res))
      );
  }
  getEnrolledStudents(courseId: string): Observable<Student[]> {
    return this.http.get<HateoasModel>(`${this.baseUrl}/courses/${courseId}/enrolled`, AppSettings.JSON_HTTP_OPTIONS)
      .pipe(
        map(object => removeHATEOAS(object)),
        retry(AppSettings.RETRIES), catchError(this.formatErrors),
        tap(res => console.log('--getEnrolledStudents:', res))
      );
  }
  enroll(student: Student, courseId: string) {
    console.log('enroll(student: Student, courseId: number)', courseId, student, JSON.stringify(student));
    return this.http.put(
      `${this.baseUrl}/courses/${courseId}/enroll`,
      JSON.stringify(student),
      AppSettings.JSON_HTTP_OPTIONS
    ).pipe(
      tap(res => console.log('--enroll:', res)),
      retry(AppSettings.RETRIES), catchError(this.formatErrors));
  }
  disenroll(student: Student, courseId: string): Observable<any> {
    console.log('disenroll(student: Student, courseId: number)', courseId, student, JSON.stringify(student));
    return this.http.put(
      `${this.baseUrl}/courses/${courseId}/disenroll/${student.id}`,
      null
    ).pipe(
      tap(s => console.log('--disenroll:', s)),
      retry(AppSettings.RETRIES), catchError(this.formatErrors));
  }

  queryAllStudents(queryTitle: string): Observable<Student[]> {
    return this.http.get<Student[]>(`${this.baseUrl}/students?q=${queryTitle}`)
      .pipe(retry(AppSettings.RETRIES), catchError(this.formatErrors));
  }
  deleteStudent(student: Student, courseId: string): Observable<any> {
    return this.http.delete(
      `${this.baseUrl}/students/${student.id}`
    ).pipe(retry(AppSettings.RETRIES), catchError(this.formatErrors));
  }

  enrollStudents(students: Student[], courseId: string) {
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
  queryEnrolledStudents(courseId: string, queryTitle: string): Observable<Student[]> {
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

import {Injectable} from '@angular/core';
import {Student} from '../models/student.model';
import {forkJoin, Observable, throwError} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {catchError, map, retry, tap} from 'rxjs/operators';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BackendService {
  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'}), // ,  'Access-Control-Allow-Origin': '*'
// observe?: 'body' | 'events' | 'response',
//   params?: HttpParams|{[param: string]: string | string[]},
//     reportProgress: true,
    // responseType: 'json'|'text',
    // withCredentials: true  // Whether this request should be sent with outgoing credentials (cookies).
  };
  private apiJsonServerProxyPath = environment.HttpOrHttpsPrefix + '://localhost:8080/API'; // URL to web api

  constructor(private http: HttpClient) {
    // console.log('@@ BackendService.constructor - new service instance (http or htpps?)=' + environment.HttpOrHttpsPrefix);
  }
  private formatErrors(error: any) {
    console.error(error);
    return throwError(error.error);
  }

  // Pipe functions to be pure. No pipe function should create any side effects that persist outside the execution of that function
  getAllStudents(): Observable<Student[]> {
    return this.http.get<any>(`${this.apiJsonServerProxyPath}/students`, this.httpOptions)
      .pipe(
        map(response => response._embedded.studentDTOList),
        map(s => s.map(ss => {
          const copy = {...ss};
          delete copy._links;
          delete copy.links; // only '_links' should show up
          return copy;
        })),
        retry(0), catchError(this.formatErrors),
        tap(res => console.log('getAllStudents._embedded.studentDTOList', res))
      );
  }
  getEnrolledStudents(courseId: number): Observable<Student[]> {
    return this.http.get<any>(`${this.apiJsonServerProxyPath}/courses/${courseId}/enrolled`, this.httpOptions)
      .pipe(
        retry(0), catchError(this.formatErrors),
        map(response => response._embedded.studentDTOList),
        map(s => s.map(ss => {
          const copy = {...ss};
          delete copy._links;
          delete copy.links; // only '_links' should show up
          return copy;
        })),
        tap(res => console.log('getEnrolledStudents._embedded.studentDTOList', res))
      );
  }
  enroll(student: Student, courseId: number) {
    console.log('enroll(student: Student, courseId: number)', courseId, student, JSON.stringify(student));
    return this.http.put(
      `${this.apiJsonServerProxyPath}/courses/${courseId}/enroll`,
      JSON.stringify(student),
      this.httpOptions
    ).pipe(
      tap(res => console.log('enroll(student: Student, courseId: number)', res)),
      retry(0), catchError(this.formatErrors));
  }
  disenroll(student: Student, courseId: number): Observable<any> {
    console.log('disenroll(student: Student, courseId: number)', courseId, student, JSON.stringify(student));
    return this.http.put(
      `${this.apiJsonServerProxyPath}/courses/${courseId}/disenroll/${student.id}`,
      null
    ).pipe(
      tap(s => console.log('disenroll http.put:', s)),
      retry(0), catchError(this.formatErrors));
  }
  queryAllStudents(queryTitle: string): Observable<Student[]> {
    return this.http.get<Student[]>(`${this.apiJsonServerProxyPath}/students?q=${queryTitle}`)
      .pipe(retry(0), catchError(this.formatErrors));
  }
  deleteStudent(student: Student, courseId: number): Observable<any> {
    return this.http.delete(
      `${this.apiJsonServerProxyPath}/students/${student.id}`
    ).pipe(retry(0), catchError(this.formatErrors));
  }

  enrollStudents(students: Student[], courseId: number) {
    const request$ = new Array<Observable<Student>>();
    students.forEach((student: Student) => request$.push(this.updateStudent(student)));
    return forkJoin(request$);

    // return this.http.post(
    //   `${this.apiJsonServerProxyPath}`,
    //   JSON.stringify(students)
    // ).pipe(retry(0), catchError(this.formatErrors));
  }
  deleteStudents(studentsToRemove: Student[]) {
  }
  queryEnrolledStudents(courseId: number, queryTitle: string): Observable<Student[]> {
    return this.http.get<Student[]>(`${this.apiJsonServerProxyPath}/courses/${courseId}/students?q=${queryTitle}`)
      .pipe(retry(0), catchError(this.formatErrors));
  }
  updateStudent(student: Student, body: object = {}): Observable<any> {
    return this.http.put(
      `${this.apiJsonServerProxyPath}`,
      JSON.stringify(body),
      this.httpOptions
    ).pipe(retry(0), catchError(this.formatErrors));
  }
  createStudent(path: string, body: object = {}): Observable<any> {
    return this.http.post(
      `${this.apiJsonServerProxyPath}${path}`,
      JSON.stringify(body),
      this.httpOptions
    ).pipe(retry(0), catchError(this.formatErrors));
  }

  // create() {}
  // find() {}
  // updateStudent(student: Student) {
  // }
  // delete(){}
}

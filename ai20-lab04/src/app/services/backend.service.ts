import {Injectable} from '@angular/core';
import {Student} from '../models/student.model';
import {forkJoin, Observable, throwError} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {catchError, map, retry, tap} from 'rxjs/operators';
import {environment} from '../../environments/environment';
import {StudentDto} from '../models/studentdto.model';

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
  private apiJsonServerProxyPath = environment.urlHttpOrHttpsPrefix + '://localhost:8080/API'; // URL to web api

  constructor(private http: HttpClient) {
    // console.log('@@ BackendService.constructor - new service instance (http or htpps?)=' + environment.urlHttpOrHttpsPrefix);
  }
  private formatErrors(error: any) {
    console.error(error);
    return throwError(error.error);
  }

  getAllStudents(): Observable<Student[]> {
    return this.http.get<StudentDto>(`${this.apiJsonServerProxyPath}/students`, this.httpOptions)
      .pipe(
        tap(res => console.log('getAllStudents', res)),
        retry(0), catchError(this.formatErrors),
        map(response => response._embedded.studentDTOList),
        tap(res => console.log('getAllStudents._embedded.studentDTOList', res))
      );
  }
  getEnrolledStudents(courseId: number): Observable<Student[]> {
    return this.http.get<Student[]>(`${this.apiJsonServerProxyPath}/courses/${courseId}/enrolled`, this.httpOptions)
      .pipe(
        tap(res => console.log('getEnrolledStudents', res)),
        retry(0), catchError(this.formatErrors));
  }
  enroll(student: Student, courseId: number) {
    // student.courseId = courseId;
    return this.http.put(
      `${this.apiJsonServerProxyPath}/students/${student.id}`,
      JSON.stringify(student),
      this.httpOptions
    ).pipe(retry(0), catchError(this.formatErrors));
  }
  disenroll(student: Student, courseId: number): Observable<any> {
    // student.courseId = 0; // testing courseId === 2 ? 1 : 2;
    return this.http.put(
      `${this.apiJsonServerProxyPath}/students/${student.id}`,
      JSON.stringify(student),
      this.httpOptions
    ).pipe(tap(s => console.log('disenroll http.put:')),
      tap(s => console.log(s)),
      retry(0),
      catchError(this.formatErrors));
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

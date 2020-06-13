import {Injectable} from '@angular/core';
import {Student} from '../model/student.model';
import {forkJoin, Observable, throwError} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {catchError, retry, tap} from 'rxjs/operators';
import {environment} from '../../environments/environment';

// TODO: StudentsContComponent che conterrà le informazioni sul DB studenti e sugli studenti iscritti, informazioni che passerà al componente
// StudentsComponent attraverso il property binding
// TODO: confrontare dati client (struttura) e dati REST dell'esercitazione server
// TODO: per progetto, questo é file DTO. Non c'é id, come id viene usata matricola/serial

@Injectable({
  providedIn: 'root'
})
export class StudentService {
  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'}),
// observe?: 'body' | 'events' | 'response',
//   params?: HttpParams|{[param: string]: string | string[]},
//     reportProgress: true,
    // responseType: 'json'|'text',
    // withCredentials: true  // Whether this request should be sent with outgoing credentials (cookies).
  };
  private apiJsonServerProxyPath = environment.urlHttpOrHttpsPrefix + '://localhost:4200/api'; // URL to web api

  constructor(private http: HttpClient) {
    console.log('@@ StudentService.constructor - new service istance (http or htpps?)=' + environment.urlHttpOrHttpsPrefix);
  }
  private formatErrors(error: any) {
    console.error(error);
    return throwError(error.error);
  }
  getAllStudents(): Observable<Student[]> {
    return this.http.get<Student[]>(`${this.apiJsonServerProxyPath}/students`)
      .pipe(retry(0), catchError(this.formatErrors));
    // return of(this.DB_STUDENT);
  }
  getEnrolledStudents(courseId: number): Observable<Student[]> {
    return this.http.get<Student[]>(`${this.apiJsonServerProxyPath}/courses/${courseId}/students`)
      .pipe(retry(0), catchError(this.formatErrors));
  }
  enroll(student: Student, courseId: number) {
    student.courseId = courseId;
    return this.http.put(
      `${this.apiJsonServerProxyPath}/students/${student.id}`,
      JSON.stringify(student),
      this.httpOptions
    ).pipe(retry(0), catchError(this.formatErrors));
  }
  disenroll(student: Student, courseId: number): Observable<any> {
    student.courseId = 0; // testing courseId === 2 ? 1 : 2;
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

// TODO: codice sotto da rivedere per progetto
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

1;

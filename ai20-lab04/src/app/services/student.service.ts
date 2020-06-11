import {Injectable} from '@angular/core';
import {Student} from '../model/student.model';
import {Observable, throwError} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {catchError, retry} from 'rxjs/operators';

// TODO: aggiungere gruppi! ... e verificarne funzionalità (tabella, visualizzazione, inserimento)
// TODO: StudentsContComponent che conterrà le informazioni sul DB studenti e sugli studenti iscritti, informazioni che passerà al componente
// StudentsComponent attraverso il property binding
// TODO: aggiornare struttura DB a es5 formato
// TODO: usare observables nel progetto
// TODO: confrontare dati client (struttura) e dati REST dell'esercitazione server
// TODO: per progetto, questo é file DTO. Non c'é id, come id viene usata matricola/serial

//   {id: '1', firstName: 'Pietro', firstName: 'Giasone', group: 'none'},
//   {id: '2', firstName: 'Paolo', firstName: 'Ferri', group: 'Cavalry'},
//   {id: '3', firstName: 'Gino', firstName: 'Rossi', group: 'Cavalry'},
//   {id: '4', firstName: 'Giovanni', firstName: 'Bianchi', group: 'Cavalry2'},
//   {id: '5', firstName: 'Tizio', firstName: 'Tizio'},
//   {id: '6', firstName: 'Caio', firstName: 'Caio'},
//   {id: '7', firstName: 'Alberto', firstName: 'Terri'},
//   {id: '8', firstName: 'Luca', firstName: 'Manni'},
//   {id: '9', firstName: 'Valentina', firstName: 'Guano'},
// ].map((el) => new Student(el.id, el.firstName, el.firstName, el.group));

@Injectable({
  providedIn: 'root'
})
export class StudentService {
  DB_STUDENT: Student[] = [];
  enrolledStudentsCourse: Student[] = []; // TODO: this should be course dependent

  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'}),
// observe?: 'body' | 'events' | 'response',
//   params?: HttpParams|{[param: string]: string | string[]},
    reportProgress: true,
    // responseType: 'json'|'text',
    // withCredentials: true  // Whether this request should be sent with outgoing credentials (cookies).
  };
  // private apiCourseUrl = 'http://localhost:3000/courses/1/students';
  private ApiJsonServerProxyPath = 'http://localhost:4200/api'; // URL to web api

  constructor(private http: HttpClient) {
    // this.DB_STUDENT = [
    //   {id: 1, serial: '123451', firstName: 'Pietro', lastName: 'Giasone', courseId: 1},
    //   {id: 2, serial: '123452', firstName: 'Paolo', lastName: 'Ferri', courseId: 1, groupId: 1},
    //   {id: 3, serial: '123453', firstName: 'Gino', lastName: 'Rossi', courseId: 1, groupId: 1},
    //   {id: 4, serial: '123454', firstName: 'Giovanni', lastName: 'Bianchi', courseId: 1, groupId: 2},
    //   {id: 5, serial: '123455', firstName: 'Tizio', lastName: 'Tizio', courseId: 1, groupId: 2},
    //   {id: 6, serial: '123456', firstName: 'Caio', lastName: 'Caio', courseId: 1, groupId: 2},
    //   {id: 7, serial: '123457', firstName: 'Alberto', lastName: 'Terri', courseId: 1, groupId: 0},
    //   {id: 8, serial: '123458', firstName: 'Luca', lastName: 'Manni', courseId: 1, groupId: 0},
    //   {
    //     id: 9, serial: '123459', firstName: 'Valentina', lastName: 'Guano', courseId: 2, groupId: 0
    //   }
    // ].map((el) => new Student(el.id, el.serial, el.firstName, el.lastName, el.groupId, el.courseId));
    // this.enrolledStudentsCourse = [...this.DB_STUDENT.slice(1, 6)];
    console.log('@ StudentService.constructor - new service istance @');
  }
  getAllStudents(): Observable<Student[]> {
    return this.http.get<Student[]>(`${this.ApiJsonServerProxyPath}/students`)
      .pipe(retry(2), catchError(this.formatErrors));
    // return of(this.DB_STUDENT);
  }
  getEnrolledStudents(courseId: number): Observable<Student[]> {
    return this.http.get<Student[]>(`${this.ApiJsonServerProxyPath}/courses/${courseId}/students`)
      .pipe(retry(2), catchError(this.formatErrors));
    // return of(this.enrolledStudents);
  }
  queryAllStudents(queryTitle: string): Observable<Student[]> {
    return this.http.get<Student[]>(`${this.ApiJsonServerProxyPath}/students?q=${queryTitle}`)
      .pipe(retry(2), catchError(this.formatErrors));
  }
  queryEnrolledStudents(courseId: number, queryTitle: string): Observable<Student[]> {
    return this.http.get<Student[]>(`${this.ApiJsonServerProxyPath}/courses/${courseId}/students?q=${queryTitle}`)
      .pipe(retry(2), catchError(this.formatErrors));
  }
  updateStudent(path: string, body: Object = {}): Observable<any> {
    return this.http.put(
      `${this.ApiJsonServerProxyPath}${path}`,
      JSON.stringify(body),
      this.httpOptions
    ).pipe(retry(2), catchError(this.formatErrors));
  }
  createStudent(path: string, body: Object = {}): Observable<any> {
    return this.http.post(
      `${this.ApiJsonServerProxyPath}${path}`,
      JSON.stringify(body),
      this.httpOptions
    ).pipe(retry(2), catchError(this.formatErrors));
  }
  deleteStudent(path): Observable<any> {
    return this.http.delete(
      `${this.ApiJsonServerProxyPath}${path}`
    ).pipe(retry(2), catchError(this.formatErrors));
  }
  enrollStudents(students: Student[], courseId: number) {
    return this.http.post(
      `${this.ApiJsonServerProxyPath}`,
      JSON.stringify(students)
    ).pipe(retry(2), catchError(this.formatErrors));
  }
  disenrollStudents(students: Student[], courseId: number) {
    return this.http.delete(
      `${this.ApiJsonServerProxyPath}`
    ).pipe(retry(2), catchError(this.formatErrors));
  }
  testDeleteDB() {
    console.log(this.DB_STUDENT);
    this.DB_STUDENT = [];
    console.log(this.DB_STUDENT);
  }

  // create() {}
  // find() {}
  // updateStudent(student: Student) {
  // }
  // delete(){}
  private formatErrors(error: any) {
    console.error(error);
    return throwError(error.error);
  }
}

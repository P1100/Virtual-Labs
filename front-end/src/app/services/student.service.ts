import {Injectable} from '@angular/core';
import {Student} from '../models/student.model';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {catchError, map, retry, tap} from 'rxjs/operators';
import {AppSettings, formatErrors, removeHATEOAS} from '../app-settings';
import {HateoasModel} from '../models/hateoas.model';

@Injectable({
  providedIn: 'root'
})
export class StudentService {
  private baseUrlApi = AppSettings.baseUrl + '/api';

  constructor(private http: HttpClient) {
  }

  getAllStudents(): Observable<Student[]> {
    return this.http.get<HateoasModel>(`${this.baseUrlApi}/students`, AppSettings.JSON_HTTP_OPTIONS)
      .pipe(
        map(object => removeHATEOAS(object)),
        retry(AppSettings.RETRIES), catchError(formatErrors),
        tap(res => console.log('--getAllStudents:', res))
      );
  }
  getEnrolledStudents(courseId: string): Observable<Student[]> {
    return this.http.get<HateoasModel>(`${this.baseUrlApi}/courses/${courseId}/enrolled`, AppSettings.JSON_HTTP_OPTIONS)
      .pipe(map(object => removeHATEOAS(object)),
        retry(AppSettings.RETRIES), catchError(formatErrors),
        tap(res => console.log('--getEnrolledStudents:', res)));
  }
  enroll(student: Student, courseId: string): Observable<any> {
    return this.http.put(`${this.baseUrlApi}/courses/${courseId}/enroll`, JSON.stringify(student), AppSettings.JSON_HTTP_OPTIONS
    ).pipe(catchError(formatErrors));
  }
  disenroll(student: Student, courseId: string): Observable<any> {
    return this.http.put(`${this.baseUrlApi}/courses/${courseId}/disenroll/${student.id}`, null
    ).pipe(catchError(formatErrors));
  }
}

import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {Observable, throwError} from 'rxjs';
import {Course} from '../models/course.model';
import {catchError, map, retry, tap} from 'rxjs/operators';
import {AppSettings, removeHATEOAS} from '../app-settings';
import {Student} from '../models/student.model';
import {HateoasModel} from '../models/hateoas.model';

@Injectable({
  providedIn: 'root'
})
export class CourseService {
  private baseUrl = environment.baseUrl;

  constructor(private http: HttpClient) {
  }
  private formatErrors(error: any) {
    console.error(error?.name + ': ' + error?.statusText);
    return throwError(error);
  }

  getCourses(): Observable<Course[]> {
    return this.http.get<HateoasModel>(`${this.baseUrl}/courses`, AppSettings.JSON_HTTP_OPTIONS)
      .pipe(
        map(object => removeHATEOAS(object)),
        retry(AppSettings.RETRIES), catchError(this.formatErrors),
        tap(res => console.log('--getCourses:', res))
      );
  }
  getCourse(s: string): Observable<Course[]> {
    return this.http.get<HateoasModel>(`${this.baseUrl}/courses/${s}`, AppSettings.JSON_HTTP_OPTIONS)
      .pipe(
        map(object => removeHATEOAS(object)),
        retry(AppSettings.RETRIES), catchError(this.formatErrors),
        tap(res => console.log('--getCourse:', res))
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
  enableCourse(courseId: string) {
    return this.http.put(
      `${this.baseUrl}/courses/${courseId}/enable`,
      null,
      AppSettings.JSON_HTTP_OPTIONS
    );
  }
  disableCourse(courseId: string) {
    return this.http.put(
      `${this.baseUrl}/courses/${courseId}/disable`,
      null,
      AppSettings.JSON_HTTP_OPTIONS
    );
  }
  addCourse(course: Course) {
    return this.http.post(
      `${this.baseUrl}/courses`,
      JSON.stringify(course),
      AppSettings.JSON_HTTP_OPTIONS
    );
  }
  updateCourse(course: Course) {
    return this.http.put(
      `${this.baseUrl}/courses`,
      JSON.stringify(course),
      AppSettings.JSON_HTTP_OPTIONS
    );
  }
  deleteCourse(courseId: string) {
    return this.http.delete(
      `${this.baseUrl}/courses/${courseId}`,
      AppSettings.JSON_HTTP_OPTIONS
    );
  }
}

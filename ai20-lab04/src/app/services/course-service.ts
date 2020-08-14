import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {Observable, throwError} from 'rxjs';
import {Course} from '../models/course.model';
import {catchError, map, retry, tap} from 'rxjs/operators';
import {AppSettings} from '../app-settings';
import {Student} from '../models/student.model';
import {HateoasModel} from '../models/hateoas.model';

@Injectable({
  providedIn: 'root'
})
export class CourseService {
  private baseUrl = environment.baseUrl;
  // TODO: rivedere uso variabile
  courses: Course[] = null;

  constructor(private http: HttpClient) {
    // Primo e unico aggiornamento corsi. Altri updates solo nelle funzioni di modifica //TODO: rivedere, nell'home chiamo getCourses
    // this.getCourses().subscribe(x => this.courses = [...x]);
  }
  private formatErrors(error: any) {
    console.error(error);
    return throwError(error.error);
  }
  //
  // getCoursesSnapshot(): Course[] {
  //   return this.courses;
  // }
  // getCourses(): Observable<Course[]> {
  //   return this.http.get<any>(`${this.baseUrl}/courses`, AppSettings.JSON_HTTP_OPTIONS)
  //     .pipe(
  //       tap(res => console.log('getCoursesPRE:', res)),
  //       map(response => response?._embedded?.courseDTOList),
  //       map(s => s?.map(ss => {
  //         const copy = {...ss};
  //         delete copy._links;
  //         delete copy.links; // only '_links' should show up
  //         return copy;
  //       })),
  //       retry(5), catchError(this.formatErrors),
  //       tap(res => console.log('getCourses:', res)),
  //       map(r => Array.of(new Course(1, '', 2, 3, true)))
  //     );
  // }
  getCourses(): Observable<Course[]> {
    return this.http.get<HateoasModel>(`${this.baseUrl}/courses`, AppSettings.JSON_HTTP_OPTIONS)
      .pipe(
        tap(res => console.log('getCoursesT pre:', res, typeof res)),
        map(object => this.removeHATEOAS(object)),
        retry(5), catchError(this.formatErrors),
        tap(res => console.log('getCoursesT post:', res, typeof res))
      );
  }
  getCourse(s: string): Observable<Course[]> {
    return this.http.get<HateoasModel>(`${this.baseUrl}/courses/${s}`, AppSettings.JSON_HTTP_OPTIONS)
      .pipe(
        tap(res => console.log('getCourses:', res, typeof res)),
        map(object => this.removeHATEOAS(object)),
        retry(5), catchError(this.formatErrors),
        tap(res => console.log('getCourses:', res, typeof res))
      );
  }
  getEnrolledStudents(courseId: string): Observable<Student[]> {
    return this.http.get<Student[]>(`${this.baseUrl}/courses/${{courseId}}/enrolled`, AppSettings.JSON_HTTP_OPTIONS);
  }

  private removeHATEOAS(i: HateoasModel): any {
    let x: any = {...i};
    x = x?._embedded;
    if (x?.courseDTOList != null) {
      x = x.courseDTOList;
    }
    if (x?.studentDTOList != null) {
      x = x.studentDTOList;
    }
    delete x?._links;
    delete x?.links; // only '_links' should show up
    return x;
  }
}

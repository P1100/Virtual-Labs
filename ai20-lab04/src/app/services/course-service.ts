import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {Observable, throwError} from 'rxjs';
import {Course} from '../models/course.model';
import {catchError, map, retry, tap} from 'rxjs/operators';
import {AppSettings} from '../app-settings';

@Injectable({
  providedIn: 'root'
})
export class CourseService {
  private baseUrl = environment.baseUrl;
  courses: Course[] = null;

  constructor(private http: HttpClient) {
    // Primo e unico aggiornamento corsi. Altri updates solo nelle funzioni di modifica //TODO: rivedere, nell'home chiamo getCourses
    this.getCourses().subscribe(x => this.courses = [...x]);
  }
  private formatErrors(error: any) {
    console.error(error);
    return throwError(error.error);
  }

  getCourses(): Observable<Course[]> {
    return this.http.get<any>(`${this.baseUrl}/courses`, AppSettings.JSON_HTTP_OPTIONS)
      .pipe(
        map(response => response._embedded.courseDTOList),
        map(s => s.map(ss => {
          const copy = {...ss};
          delete copy._links;
          delete copy.links; // only '_links' should show up
          return copy;
        })),
        retry(5), catchError(this.formatErrors),
        tap(res => console.log('getCourses:', res))
      );
  }
  // getCourses(): Observable<Course[]> {
  //   return this.http.get<any>(`${this.baseUrl}/courses`, AppSettings.JSON_HTTP_OPTIONS)
  //     .pipe(
  //       map(response => response._embedded.courseDTOList),
  //       map(s => s.map(ss => this.removeHATEOAS(ss))),
  //       retry(5), catchError(this.formatErrors),
  //       tap(res => console.log('getCourses:', res))
  //     );
  // }

  getCoursesSnapshot(): Course[] {
    return this.courses;
  }
  getCourse(s: string): Observable<Course> {
    return null;
  }

  private removeHATEOAS(o: any): any {
    let x = {...o};
    if (x?._embedded != null) {
      x = x._embedded;
    }
    if (x?.courseDTOList != null) {
      x = x.courseDTOList;
    }
    if (x?.studentDTOList != null) {
      x = x.studentDTOList;
    }
    if (x?.studentDTOList != null) {
      x = x.studentDTOList;
    }
    delete x._links;
    delete x.links; // only '_links' should show up
    console.log(x);
    return x;
  }
}

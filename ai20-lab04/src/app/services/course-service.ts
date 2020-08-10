import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {Observable, throwError} from 'rxjs';
import {Course} from '../models/course.model';
import {catchError, map, retry, tap} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class CourseService {
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
      // ,
      // responseType: 'json'
    })
  };
  private apiJsonServerProxyPath = environment.prefixUrl;
  courses: Course[] = null;

  constructor(private http: HttpClient) {
    console.log('CourseService costr');
    // Primo e unico aggiornamento corsi. Altri updates solo nelle funzioni di modifica
    this.getCourses().subscribe(x => this.courses = [...x]);
  }
  private formatErrors(error: any) {
    console.error(error);
    return throwError(error.error);
  }

  getCourses(): Observable<Course[]> {
    return this.http.get<any>(`${this.apiJsonServerProxyPath}/courses`, this.httpOptions)
      .pipe(
        tap(res => console.log('getCourses response._embedded.CourseDTOList', res)),
        map(response => response._embedded.courseDTOList),
        tap(res => console.log('getCourses v2', res)),
        map(s => s.map(ss => {
          const copy = {...ss};
          delete copy._links;
          delete copy.links; // only '_links' should show up
          return copy;
        })),
        retry(0), catchError(this.formatErrors),
        tap(res => console.log('getCourses post', res))
      );
  }

  getCoursesSnapshot(): Course[] {
    console.log('snapshot courses', this.courses);
    return this.courses;
  }
}

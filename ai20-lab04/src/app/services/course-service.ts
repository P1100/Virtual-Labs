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
    return this.http.get<any>(`${this.baseUrl}/courses`, {
      headers: new HttpHeaders({'Content-Type': 'application/json'}),
      responseType: 'json'
    })
      .pipe(
        map(response => response._embedded.courseDTOList),
        map(s => s.map(ss => {
          const copy = {...ss};
          delete copy._links;
          delete copy.links; // only '_links' should show up
          return copy;
        })),
        retry(5), catchError(this.formatErrors),
        tap(res => console.log('http.get getCourses:', res))
      );
  }

  getCoursesSnapshot(): Course[] {
    return this.courses;
  }
}

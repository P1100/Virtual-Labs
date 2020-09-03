import {Injectable} from '@angular/core';
import {AppSettings, formatErrors} from '../app-settings';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {catchError} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class VlServiceService {
  private baseUrlApi = AppSettings.baseUrl + '/teams';

  constructor(private http: HttpClient) {
  }

  proposeTeam(courseId: string, teamName: string, hoursTimeout: number, memberIds: number[]): Observable<any> {
    return this.http.post(`${this.baseUrlApi}/propose/${courseId}/${teamName}/${hoursTimeout}/${memberIds}`, null, AppSettings.JSON_HTTP_OPTIONS)
      .pipe(catchError(formatErrors));
  }
}

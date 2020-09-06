import {Injectable} from '@angular/core';
import {AppSettings, formatErrors, removeHATEOAS} from '../app-settings';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {catchError, map, retry, tap} from 'rxjs/operators';
import {HateoasModel} from '../models/hateoas.model';
import {Team} from '../models/team.model';

@Injectable({
  providedIn: 'root'
})
export class VlServiceService {
  private baseUrlApi = AppSettings.baseUrl + '/api/teams';

  constructor(private http: HttpClient) {
  }

  proposeTeam(courseId: string, teamName: string, hoursTimeout: number, memberIds: number[]): Observable<any> {
    return this.http.post(`${this.baseUrlApi}/propose/${courseId}/${teamName}/${hoursTimeout}/${memberIds}`, null, AppSettings.JSON_HTTP_OPTIONS)
      .pipe(catchError(formatErrors));
  }
  getTeamsUser(student_id: number, courseId: string): Observable<Team[]> {
    return this.http.get<HateoasModel>(`${this.baseUrlApi}/${student_id}/teams/${courseId}`, AppSettings.JSON_HTTP_OPTIONS)
      .pipe(
        map(object => removeHATEOAS(object)),
        retry(AppSettings.RETRIES), catchError(formatErrors),
        tap(res => console.log('--getTeamsUser:', res))
      );
  }
}

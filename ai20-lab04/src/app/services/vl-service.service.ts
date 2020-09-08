import {Injectable} from '@angular/core';
import {AppSettings, formatErrors, removeHATEOAS} from '../app-settings';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {catchError, map, retry, tap} from 'rxjs/operators';
import {HateoasModel} from '../models/hateoas.model';
import {Team} from '../models/team.model';
import {Vm} from '../models/vm.model';

@Injectable({
  providedIn: 'root'
})
export class VlServiceService {
  private baseUrlApi = AppSettings.baseUrl + '/api';

  constructor(private http: HttpClient) {
  }

  proposeTeam(courseId: string, teamName: string, hoursTimeout: number, memberIds: number[]): Observable<any> {
    return this.http.post(`${this.baseUrlApi}/teams/propose/${courseId}/${teamName}/${hoursTimeout}/${memberIds}`, null, AppSettings.JSON_HTTP_OPTIONS)
      .pipe(catchError(formatErrors));
  }
  cleanUpTeams(courseId: string): Observable<any> {
    return this.http.delete(`${this.baseUrlApi}/teams/cleanup/${courseId}`, AppSettings.JSON_HTTP_OPTIONS)
      .pipe(catchError(formatErrors));
  }
  getTeamsUser(student_id: number, courseId: string): Observable<Team[]> {
    return this.http.get<HateoasModel>(`${this.baseUrlApi}/teams/${student_id}/teams/${courseId}`, AppSettings.JSON_HTTP_OPTIONS)
      .pipe(
        map(object => removeHATEOAS(object)),
        retry(AppSettings.RETRIES), catchError(formatErrors),
        tap(res => console.log('--getTeamsUser:', res))
      );
  }
  createVm(vm: Vm): Observable<any> {
    return this.http.post(`${this.baseUrlApi}/vms`, JSON.stringify(vm), AppSettings.JSON_HTTP_OPTIONS)
      .pipe(tap(x => console.log('INSIDE CREATE VM', x)),catchError(formatErrors));

  }
  getTeamVm(teamId: number): Observable<Vm[]> {
    console.log('getTeamVm: ', teamId);
    return this.http.get<HateoasModel>(`${this.baseUrlApi}/vms/${teamId}`, AppSettings.JSON_HTTP_OPTIONS)
      .pipe(
        tap(x => console.log('getTeamVm-tap: ', x)),
        map(object => removeHATEOAS(object)),
        retry(AppSettings.RETRIES), catchError(formatErrors),
        tap(res => console.log('--getTeamVm:', res))
      );
  }
  changeStatusVm(vmId: number, status: boolean): Observable<any> {
    if (status) {
      return this.http.put(`${this.baseUrlApi}/vms/vm/${vmId}/enable`, null, AppSettings.JSON_HTTP_OPTIONS).pipe(catchError(formatErrors));
    } else {
      return this.http.put(`${this.baseUrlApi}/vms/vm/${vmId}/disable`, null, AppSettings.JSON_HTTP_OPTIONS).pipe(catchError(formatErrors));
    }
  }
}

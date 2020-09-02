import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {catchError, retry, tap} from 'rxjs/operators';
import * as moment from 'moment';
import {User} from '../models/user.model';
import {BehaviorSubject, Observable} from 'rxjs';
import {AppSettings, formatErrors} from '../app-settings';
import {Student} from '../models/student.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrlApi = AppSettings.baseUrl;
  user: User;
  isLoggedSubject: BehaviorSubject<boolean>;

  constructor(private http: HttpClient) {
    if (this.isLoggedIn()) {
      this.isLoggedSubject = new BehaviorSubject(true);
    } else {
      localStorage.clear();
      this.isLoggedSubject = new BehaviorSubject(false);
    }
  }
  public getIsLoggedSubject(): Observable<any> {
    return this.isLoggedSubject as Observable<any>;
  }
  login(username: string, password: string): Observable<any> {  // returns token object
    return this.http.post<any>(`${this.baseUrlApi}/users/authenticate`, {username, password}).pipe(
      // {"token":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIwMDAwMDAiLCJleHAiOjE1OTk5NzYzNjgsImlhdCI6MTU5ODkzOTU2OH0.RpHPiVshk9ZXDr-ZUA9JEPdqvAfbtNhhEqzTiBQgw3ksXA5nxn0UsiM5bk3hlnWxXTrtqgHFm-UTC6DrV8YoSA"}
      tap(authResult => {
        const token = authResult?.token;
        const payload = JSON.parse(atob(token?.split('.')[1]));
        localStorage.setItem('token', token);
        localStorage.setItem('username', username);
        localStorage.setItem('role', authResult?.role);
        localStorage.setItem('expires_at', payload?.exp);
      }),
      tap(() => this.isLoggedSubject.next(true)),
      retry(AppSettings.RETRIES), catchError(formatErrors)
    );
  }
  logout(): void {
    // No server logout for JWT, just remove token from local storage
    this.isLoggedSubject.next(false);
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    localStorage.removeItem('role');
    localStorage.removeItem('expires_at');
    // localStorage.clear();
  }
  public isLoggedIn(): boolean {
    const exp = localStorage.getItem('expires_at');
    return exp != null && moment().isBefore(moment.unix(+exp));
  }
  public registerStudent(user: Student): Observable<User> {
    return this.http.post<User>(`${this.baseUrlApi}/users/student`, JSON.stringify(user), AppSettings.JSON_HTTP_OPTIONS).pipe(catchError(formatErrors));
  }
  public registerProfessor(user: Student): Observable<User> {
    return this.http.post<User>(`${this.baseUrlApi}/users/professor`, JSON.stringify(user), AppSettings.JSON_HTTP_OPTIONS).pipe(catchError(formatErrors));
  }
}

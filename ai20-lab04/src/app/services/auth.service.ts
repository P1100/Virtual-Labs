import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {catchError, tap} from 'rxjs/operators';
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
      this.isLoggedSubject = new BehaviorSubject(false);
    }
  }
  public getIsLoggedSubject(): Observable<any> {
    return this.isLoggedSubject as Observable<any>;
  }
  login(username: string, password: string): Observable<any> {  // returns object with token
    return this.http.post<any>(`${this.baseUrlApi}/users/authenticate`, {username, password}).pipe(
      tap(authResult => {
        console.log(authResult?.token);
        localStorage.setItem('token', authResult?.token);
        // const expiresAt = moment().add(authResult.expiresIn, 'second');
        // console.log(JSONmoment.unix(tkn.exp));
        localStorage.setItem('username', username);
      }),
      tap(() => this.isLoggedSubject.next(true))
    );
  }
  logout(): void {
    // No server logout for JWT, just remove token from local storage
    this.isLoggedSubject.next(false);
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    localStorage.removeItem('expires_at');
    localStorage.clear();
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

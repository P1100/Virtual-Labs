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
    // console.log('new auth service');
    // localStorage.clear();
    if (this.isLoggedIn()) {
      this.isLoggedSubject = new BehaviorSubject(true);
    } else {
      this.isLoggedSubject = new BehaviorSubject(false);
    }
  }
  public getIsLoggedSubject(): Observable<any> {
    return this.isLoggedSubject as Observable<any>;
  }
  // TODO: static????
  private static setSession(authResult, email) {
    const tkn = JSON.parse(atob(authResult.accessToken.split('.')[1]));
    // const expiresAt = moment().add(authResult.expiresIn, 'second');
    console.log('setSession:', atob(authResult.accessToken.split('.')[1]));
    // console.log(JSONmoment.unix(tkn.exp));
    localStorage.setItem('accessToken', authResult.accessToken);
    localStorage.setItem('user', email);
    // json-server-auth token field exp contains epoch of exportation (last 1 hour)
    localStorage.setItem('expires_at', tkn.exp);
  }

  login(email: string, password: string): Observable<any> {  // returns object with accessToken
    return this.http.post<User>('/api/login', {email, password}).pipe(
      // tap(user => this.isLoggedSubject.next(user)));
      // tap(res => console.log('AuthService.login() post before delay:')),
      // delay(2000), // testing correctness code
      // tap(res => console.log('AuthService.login() post before delay:')),
      tap(res => AuthService.setSession(res, email)),
      tap(() => this.isLoggedSubject.next(true))
    );
  }
  logout(): /* Observable<any> */ void {
    // No server logout for JWT, just remove token from local storage
    this.isLoggedSubject.next(false);
    localStorage.removeItem('accessToken');
    localStorage.removeItem('user');
    localStorage.removeItem('expires_at');
    console.log('AuthService.logout: accessToken removed');
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

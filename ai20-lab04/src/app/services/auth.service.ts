import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {tap} from 'rxjs/operators';
import * as moment from 'moment';
import {User} from '../models/user.model';
import {BehaviorSubject, Observable, Subject} from 'rxjs';

export const ANONYMOUS_USER: User = {
  id: undefined, email: undefined, roles: []
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {
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

  /* shareReplay --> needs Subject
     We are calling shareReplay to prevent the receiver of this Observable
    from accidentally triggering multiple POST requests due to multiple subscriptions.
  */
  login(email: string, password: string): Observable<any> {  // returns object with accessToken
    return this.http.post<User>('/api/login', {email, password}).pipe(
      // tap(user => this.isLoggedSubject.next(user)));
      // tap(res => console.log('AuthService.login() post before delay:')),
      // delay(2000), // testing correctness code
      // tap(res => console.log('AuthService.login() post before delay:')),
      tap(res => AuthService.setSession(res, email)),
      // shareReplay(),
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
  public getSubscriptionSubject(): Subject<any> {
    return this.isLoggedSubject;
  }

  public isLoggedIn(): boolean {
    const exp = localStorage.getItem('expires_at');
    return exp != null && moment().isBefore(moment.unix(+exp));
  }
  public isLoggedOut() {
    return !this.isLoggedIn();
  }
}

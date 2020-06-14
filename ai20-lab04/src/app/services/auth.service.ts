import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {shareReplay, tap} from 'rxjs/operators';
import * as moment from 'moment';
import {User} from '../model/user.model';
import {BehaviorSubject, Observable, Subject} from 'rxjs';

export const ANONYMOUS_USER: User = {
  id: undefined, email: undefined, roles: []
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  user: User;
  isLoggedSubject = new BehaviorSubject(false);

  constructor(private http: HttpClient) {
    console.log('new auth service');
  }

  /* shareReplay
     We are calling shareReplay to prevent the receiver of this Observable
    from accidentally triggering multiple POST requests due to multiple subscriptions.
  */
  login(email: string, password: string): Observable<any> {  // returns object with accessToken
    return this.http.post<User>('/api/login', {email, password}).pipe(
      // tap(user => this.isLoggedSubject.next(user)));
      tap(res => console.log('login post res:', res)),
      tap(res => this.setSession(res, email)),
      shareReplay(),
      tap(res => this.isLoggedSubject.next(true))
    );
  }

  private setSession(authResult, email) {
    const tkn = JSON.parse(atob(authResult.accessToken.split('.')[1]));
    // const expiresAt = moment().add(authResult.expiresIn, 'second');
    console.log('setSession:', atob(authResult.accessToken.split('.')[1]));
    // console.log(JSONmoment.unix(tkn.exp));
    localStorage.setItem('accessToken', authResult.accessToken);
    localStorage.setItem('user', email);
    // json-server-auth token field exp contains epoch of exportation (last 1 hour)
    localStorage.setItem('expires_at', tkn.exp);
  }

  logout(): /* Observable<any> */ void {
    // No server logout for JWT, just remove token from local storage
    this.isLoggedSubject.next(false);
    localStorage.removeItem('accessToken');
    localStorage.removeItem('user');
    console.log('AuthService.logout: accessToken removed');
    /*
    return this.http.post('/api/logout', null).pipe(
      shareReplay(),
      // tap(user => this.isLoggedSubject.next(ANONYMOUS_USER)));
      tap(user => this.user = ANONYMOUS_USER)
    );
    */
  }
  public getSub(): Subject<any> {
    return this.isLoggedSubject;
  }
  public isLoggedIn() {
    return moment().isBefore(moment.unix(+localStorage.getItem('expires_at')));
  }
  public isLoggedOut() {
    return !this.isLoggedIn();
  }

}

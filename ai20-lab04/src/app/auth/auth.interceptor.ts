import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor() {
  }
  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    // const accessToken = localStorage.getItem('accessToken');
    const accessToken = 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTU5MzAzODM4MywiaWF0IjoxNTkzMDIwMzgzfQ.bQUxdDKJWPVOKM2g1LnR5ubFjs2GNhQutsh43ARNTmQ_44G2ZbHyxfcchY8DmoxqSwNpU6jfusewmWuKkMJAUQ';
    if (accessToken) {
      const cloned = request.clone({
        headers: request.headers.set('Authorization', 'Bearer ' + accessToken)
      });
      // console.log('AuthInterceptor accessToken found: ' + JSON.stringify(accessToken));
      return next.handle(cloned);
    } else {
      console.log('AuthInterceptor accessToken not found');
      return next.handle(request);
    }
  }
}


import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, CanActivateChild, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {AuthService} from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthStudentGuard implements CanActivate, CanActivateChild {
  constructor(
    private router: Router,
    private authService: AuthService
  ) {
  }
  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const role = localStorage.getItem('role');
    if (!this.authService.isLoggedIn() || role != 'student') {
      this.router.navigateByUrl('/unauthorized');
    }
    return true;
  }
  canActivateChild(childRoute: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const role = localStorage.getItem('role');
    if (!this.authService.isLoggedIn() || role != 'student') {
      this.router.navigateByUrl('/unauthorized');
    }
    return true;
  }
}

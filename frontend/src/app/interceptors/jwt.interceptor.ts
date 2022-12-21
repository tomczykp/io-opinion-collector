import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor, HttpErrorResponse
} from '@angular/common/http';
import {catchError, Observable, switchMap, throwError} from 'rxjs';
import {environment} from "../../environments/environment";
import {AuthService} from "../services/auth.service";
import { Router } from '@angular/router';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

  constructor(
    private authService: AuthService,
    private router: Router,
  ) {}

  refresh = false;

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    this.refresh = false;
    const token = localStorage.getItem("jwt");

    if (!request.url.startsWith(environment.apiUrl) || !token) {
      return next.handle(request).pipe(catchError((err) => {
        if (err.status === 403) {
          this.logout();
        }
        return throwError(() => err)}));
    }

    const cloned = request.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });

    return next.handle(cloned).pipe(catchError((err) => {
      if (err.status === 403) {
        this.logout();
      }

      if (err.status === 401 && !this.refresh) {
        this.refresh = true;
        return this.handleRefreshToken(request, next)
      }

      this.refresh = false;
      return throwError(() => err)
    }));
  }

  handleRefreshToken(request: HttpRequest<any>, next: HttpHandler) {
    return this.authService.refresh().pipe(catchError(err => {
      console.log("Could not refresh token, need to relogin")
      this.logout();
      return throwError(err);
    })).pipe(switchMap((res) => {
        this.authService.saveUserData(res);
        const token = res.body?.jwt;

        return next.handle(request.clone({
          setHeaders: {
            Authorization: `Bearer ${token}`
          }
        }));
      }));
  }

  logout() {
    this.authService.clearUserData();
    this.authService.authenticated.next(false);
    this.router.navigate(['/login'], {queryParams: {'session-expired': true}});
  }
}

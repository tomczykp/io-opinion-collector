import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable } from 'rxjs';
import {environment} from "../../environments/environment";

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

  constructor() {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const token = localStorage.getItem("jwt");

    if (!request.url.startsWith(environment.apiUrl) || !token) {
      return next.handle(request);
    }

    const cloned = request.clone({
      headers: request.headers.set("Authorization",
        "Bearer " + token)
    });
    return next.handle(cloned);

  }
}

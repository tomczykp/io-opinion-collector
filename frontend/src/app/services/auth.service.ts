import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {LoginResponse} from "../model/LoginResponse";
import {environment} from "../../environments/environment";
import {BehaviorSubject, Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  authenticated = new BehaviorSubject(false);

  constructor(private http: HttpClient) {
    if (localStorage.getItem("email") != null) {
      this.authenticated.next(true);
    }
  }

  login(email: string, password: string) {
    return this.http.post<LoginResponse>(`${environment.apiUrl}/login`, {email, password}, {observe: 'response'})
  }

  register(email: string, username: string, password: string) {
    return this.http.post(`${environment.apiUrl}/register`, {email, username, password}, {observe: 'response'})
  }

  logout() {
    console.log("logging out")
    localStorage.removeItem("role")
    localStorage.removeItem("email")
    localStorage.removeItem("jwt")
    localStorage.removeItem("refreshToken")
    this.authenticated.next(false);
  }
}

import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {LoginResponse} from "../model/LoginResponse";
import {environment} from "../../environments/environment";
import {BehaviorSubject} from "rxjs";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  authenticated = new BehaviorSubject(false);

  constructor(
    private http: HttpClient,
    private router: Router) {
    if (localStorage.getItem("email") != null) {
      this.authenticated.next(true);
    }
  }

  login(email: string, password: string) {
    return this.http.post<LoginResponse>(`${environment.apiUrl}/login`, {email, password}, {observe: 'response'})
  }

  refresh() {
    return this.http.get<LoginResponse>(`${environment.apiUrl}/refresh?token=${this.getRefreshToken()}`, {observe: 'response'})
  }

  saveUserData(result: any) {
    localStorage.setItem("email", result.body.email)
    localStorage.setItem("jwt", result.body.jwt)
    localStorage.setItem("refreshToken", result.body.refreshToken)
    localStorage.setItem("role", result.body.role)
  }

  register(email: string, username: string, password: string) {
    return this.http.post(`${environment.apiUrl}/register`, {email, username, password}, {observe: 'response'})
  }

  logout() {
    this.http.delete(environment.apiUrl + "/signout?token=" + localStorage.getItem("refreshToken"))
      .subscribe((result) => {
        this.clearUserData();
        this.authenticated.next(false);
        this.router.navigate(['/login'], {queryParams: {'logout-success': true}});
    })
  }

  logoutFromAllDevices() {
    this.http.delete(environment.apiUrl + "/signout/force").subscribe((result) => {
      this.clearUserData();
      this.authenticated.next(false);
      this.router.navigate(['/login'], {queryParams: {'logout-success': true}});
    })
  }

  getRole() {
    return localStorage.getItem("role");
  }

  getEmail() {
    return localStorage.getItem("email");
  }

  getRefreshToken() {
    return localStorage.getItem("refreshToken");
  }

  clearUserData() {
    localStorage.removeItem("role")
    localStorage.removeItem("email")
    localStorage.removeItem("jwt")
    localStorage.removeItem("refreshToken")
  }
}

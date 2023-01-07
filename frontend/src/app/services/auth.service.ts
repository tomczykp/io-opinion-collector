import {Injectable} from '@angular/core';
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

    window.addEventListener('storage', (event) => {
      if (event.storageArea == localStorage) {
        let token = localStorage.getItem('jwt');
        if(token == undefined) {
          window.location.href = '/login';
        }
      }
    }, false);
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
    localStorage.setItem("provider", result.body.provider)
    localStorage.setItem("username", result.body.visibleName)
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

  setUsername(username: string) {
    localStorage.setItem("username", username);
  }

  getUsername() {
    return localStorage.getItem("username");
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

  getProvider() {
    return localStorage.getItem("provider");
  }

  clearUserData() {
    localStorage.removeItem("role")
    localStorage.removeItem("email")
    localStorage.removeItem("jwt")
    localStorage.removeItem("refreshToken")
    localStorage.removeItem("provider")
    localStorage.removeItem("username")
  }

  loginWithGoogle() {
    window.location.href = `${environment.apiUrl}/oauth2/authorize/google`
  }

  loginWithFacebook() {
    window.location.href = `${environment.apiUrl}/oauth2/authorize/facebook`
  }

  authByGoogleCode(code: string) {
    return this.http.get(`${environment.apiUrl}/token/google?code=${code}`, {observe: "response"})
  }

  authByFacebookCode(code: string) {
    return this.http.get(`${environment.apiUrl}/token/facebook?code=${code}`, {observe: "response"})
  }
}

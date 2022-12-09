import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {BehaviorSubject, catchError, Observable} from "rxjs";
import {Router} from "@angular/router";
import {User} from "../model/User";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  authenticated = new BehaviorSubject(false);

  constructor(
    private http: HttpClient) {
    if (localStorage.getItem("email") != null) {
      this.authenticated.next(true);
    }
  }

  getUser() {
    return this.http.get<User>(environment.apiUrl + "/users/" + localStorage.getItem('email'))
  }

  getUsers(email: string) {
    return this.http.get<User[]>(environment.apiUrl + "/users?email=" + email);
  }

  changePassword(oldPassword: string, newPassword: string) {
    return this.http.put(environment.apiUrl + "/users/password", {oldPassword, newPassword}, {observe: 'response'})
  }

  removeByUser() {
    return this.http.delete(environment.apiUrl + "/users/remove/user", {observe: 'response'});
  }

  removeByAdmin(id: number) {
    return this.http.delete(environment.apiUrl + "/users/remove/admin?id=" + id, {observe: 'response'})
  }

  lock(id: number) {
    return this.http.post(environment.apiUrl + "/users/lock?id=" + id, {id}, {observe: 'response'});
  }

  unlock(id: number) {
    return this.http.post(environment.apiUrl + "/users/unlock?id=" + id, {id}, {observe: 'response'});
  }

  confirmResetPassword(newPassword: string, resetToken: string) {
    return this.http.put(environment.apiUrl + "/users/confirm/reset?password="+newPassword+"&token="+resetToken,{newPassword, resetToken}, {observe: 'response'});
  }

  resetPassword(email: string) {
    return this.http.put(environment.apiUrl + "/users/reset?email=" + email, {email}, {observe: 'response'});
  }
}
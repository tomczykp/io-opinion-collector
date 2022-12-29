import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {BehaviorSubject} from "rxjs";
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

  changeUsername(username: string) {
    return this.http.put(environment.apiUrl + "/users/username?newUsername=" + username, {}, {observe: 'response'})
  }

  removeByUser() {
    return this.http.delete(environment.apiUrl + "/users/remove/user", {observe: 'response'});
  }

  removeByAdmin(email: String) {
    return this.http.delete(environment.apiUrl + "/users/remove/admin?email=" + email, {observe: 'response'})
  }

  lock(email: String) {
    return this.http.put(environment.apiUrl + "/users/lock?email=" + email, {}, {observe: 'response'});
  }

  unlock(email: String) {
    return this.http.put(environment.apiUrl + "/users/unlock?email=" + email, {}, {observe: 'response'});
  }

  confirmResetPassword(newPassword: string, resetToken: string) {
    return this.http.put(environment.apiUrl + "/users/confirm/reset?password=" + newPassword + "&token=" + resetToken, null, {observe: 'response'});
  }

  resetPassword(email: string) {
    return this.http.put(environment.apiUrl + "/users/reset?email=" + email, null, {observe: 'response'});
  }
}

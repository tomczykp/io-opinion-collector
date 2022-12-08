import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {BehaviorSubject, catchError, Observable} from "rxjs";

import {User} from "../model/User";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  getUser() {
    return this.http.get<User>(environment.apiUrl + "/users/" + localStorage.getItem('email'))
  }

  removeByUser() {
    return this.http.delete(environment.apiUrl+"/remove/user");
}
}


import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {BehaviorSubject, catchError, Observable} from "rxjs";

@Injectable({
    providedIn: 'root'
  })
export class UserService {

    authenticated = new BehaviorSubject(false);

    constructor(private http: HttpClient) {}

    removeByUser() {
        return this.http.delete(environment.apiUrl+"/remove/user");
    }
}
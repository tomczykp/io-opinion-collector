import {Injectable} from '@angular/core';
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {BehaviorSubject, Observable} from "rxjs";
import * as url from "url";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class EventsService {

  events = new BehaviorSubject([]);

  constructor(private httpClient: HttpClient) {
  }

  public getEvents(): Observable<OC.Event[]> {
    return this.httpClient.get<OC.Event[]>(environment.apiUrl + '/events');
  }

  public closeEvent(id: string): void {
    let url = environment.apiUrl + '/events/' + id + '/close';
    this.httpClient.post(url, null).subscribe(value => {
      console.log(value);
    })
  }
}

export namespace OC {
  export interface Event {
    eventID: string;
    userID: string;
    description: string;
    status: string;
    opinionID: string;
    questionID: string;
    productID: string;
  }
}

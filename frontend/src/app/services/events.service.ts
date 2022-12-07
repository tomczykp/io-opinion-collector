import {Injectable} from '@angular/core';
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {Observable} from "rxjs";
import * as url from "url";

@Injectable({
  providedIn: 'root'
})
export class EventsService {

  constructor(private httpClient: HttpClient) {
  }

  public getEvents(): Observable<Array<OC.Event>> {
    return this.httpClient.get<Array<OC.Event>>('http://localhost:8080/api/events');
  }

  public closeEvent(id: string): void {
    let url = 'http://localhost:8080/api/events/' + id + '/close';
    this.httpClient.post(url, null).subscribe(value => {
      console.log(value);
    });
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

import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {BehaviorSubject, Observable} from "rxjs";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class EventsService {

  events = new BehaviorSubject([]);

  constructor(private httpClient: HttpClient) {
  }

  public getEvents(): Observable<OC.BasicEvent[]> {
    return this.httpClient.get<OC.BasicEvent[]>(`${environment.apiUrl}/events`);
  }

  public getUserEvents(): Observable<OC.BasicEvent[]> {
    return this.httpClient.get<OC.BasicEvent[]>(`${environment.apiUrl}/user/events`);
  }

  public getEventsCount(): Observable<number> {
    return this.httpClient.get<number>(`${environment.apiUrl}/user/eventsCount`)
  }

  public getEvent(eventID: String): Observable<OC.Event> {
    return this.httpClient.get<OC.Event>(`${environment.apiUrl}/events/${eventID}`);
  }

  public closeEvent(id: string): void {
    this.httpClient.post(`${environment.apiUrl}/events/${id}/close`, null).subscribe(value => {
    })
  }

}

export namespace OC {
  export interface BasicEvent {
    eventID: string;
    userName: string;
    description: string;
    status: string;
    type: string;
  }

  export interface Event {
    eventID: string;
    userID: string;
    description: string;
    status: string;
    questionID: string;
    opinionID?: any;
    productID?: any;
    type: string;
  }
}

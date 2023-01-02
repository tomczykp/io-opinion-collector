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

  public getEvents(): Observable<OC.Event[]> {
    return this.httpClient.get<OC.Event[]>(`${environment.apiUrl}/events`);
  }

  public getUserEvents(userID: string): Observable<OC.Event[]> {
    return this.httpClient.get<OC.Event[]>(`${environment.apiUrl}/users/${userID}/events`);
  }

  public getEventsCount(userID: string): Observable<number> {
    return this.httpClient.get<number>(`${environment.apiUrl}/users/${userID}/eventsCount`)
  }

  public closeEvent(id: string): void {
    this.httpClient.post(`${environment.apiUrl}/events/${id}/close`, null).subscribe(value => {
    })
  }
}

export namespace OC {
  export interface Event {
    eventID: string;
    userName: string;
    description: string;
    status: string;
  }
}

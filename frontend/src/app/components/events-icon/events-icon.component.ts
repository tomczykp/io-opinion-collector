import {AfterViewInit, Component, OnDestroy, OnInit} from '@angular/core';
import {EventsService} from "../../services/events.service";
import {User} from "../../model/User";
import {UserService} from "../../services/user.service";
import {AuthService} from "../../services/auth.service";
import {interval, Subscription} from "rxjs";

@Component({
  selector: 'app-events-icon',
  templateUrl: './events-icon.component.html'
})
export class EventsIconComponent implements OnInit, OnDestroy{

  authenticated = false;
  role = ""
  eventsCount: number;
  userID : string;

  counter = interval(5000);
  refreshSubscription: Subscription;


  constructor(
    private eventsService: EventsService,
    private authService: AuthService) {
  }

  ngOnInit(): void {
    this.getEventsCount();
    this.refreshSubscription = this.counter.subscribe(value => {
      this.getEventsCount();
    });
  }

  getEventsCount(): void {
    this.authService.authenticated.subscribe((change) => {
      this.authenticated = change;
      this.role = this.authService.getRole()!;
      if (this.authenticated) {
        this.eventsService.getEventsCount().subscribe((eventsCount) => {
          this.eventsCount = eventsCount;
        })
      }
    })
  }

  ngOnDestroy() {
    this.refreshSubscription.unsubscribe();
  }

}

import {AfterViewInit, Component, OnInit} from '@angular/core';
import {EventsService} from "../../services/events.service";
import {User} from "../../model/User";
import {UserService} from "../../services/user.service";
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-events-icon',
  templateUrl: './events-icon.component.html'
})
export class EventsIconComponent implements OnInit, AfterViewInit {

  authenticated = false;
  eventsCount: number;
  userID : string;

  constructor(
    private eventsService: EventsService,
    private authService: AuthService,
    private userService: UserService) {
  }

  ngOnInit(): void {
    this.authService.authenticated.subscribe((change) => {
      this.authenticated = change;
      if (this.authenticated) {
        this.userService.getUser().subscribe((user) => {
          this.userID = user.id.toString();
        })
      }

      this.eventsService.getEventsCount(this.userID).subscribe((eventsCount) => {
        this.eventsCount = eventsCount;
      })
    })

  }

  ngAfterViewInit() {
  }


}

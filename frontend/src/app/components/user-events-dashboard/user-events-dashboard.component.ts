import {Component, Input, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from "@angular/material/paginator";
import {MatTableDataSource} from "@angular/material/table";
import {MatSort} from "@angular/material/sort";
import {EventsService, OC} from "../../services/events.service";
import {AuthService} from "../../services/auth.service";
import {UserService} from "../../services/user.service";
import {interval, Subscription} from "rxjs";

@Component({
  selector: 'app-user-events-dashboard',
  templateUrl: './user-events-dashboard.component.html'
})
export class UserEventsDashboardComponent implements OnInit, OnDestroy {
  displayedColumns: string[] = ['status', 'description', 'action'];
  dataSource: MatTableDataSource<OC.Event>;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  events: OC.Event[] = [];


  counter = interval(5000);
  refreshSubscription : Subscription;

  isOpenedOnly: boolean = true;

  constructor(
    private eventsService: EventsService,
    private authService: AuthService,
    private userService: UserService
  ) {
  }

  ngOnInit(): void {
    this.getEvents();
    this.refreshSubscription = this.counter.subscribe(value => {
      this.getEvents();
    });
  }

  getEvents(): void {
    this.authService.authenticated.subscribe((authenticated) => {
      if (authenticated) {
        this.userService.getUser().subscribe((user) => {
          let userID = user.id.toString();

          this.eventsService.getUserEvents(userID).subscribe((events) => {
            this.events = events;

            if (this.isOpenedOnly) {
              this.events = this.events.filter(function (event) {
                return event.status != 'Closed';
              });
            }

            this.events = this.events.sort((eventOne, eventTwo) => {
              if (eventOne.status == "Open" && eventTwo.status == "Open")
                return 0;
              else if (eventOne.status == "Open" && eventTwo.status == "Close")
                return 1;

              return -1;
            });

            this.dataSource = new MatTableDataSource(this.events);
            this.dataSource.paginator = this.paginator;
            this.dataSource.sort = this.sort;
          })
        })
      }
    })
  }

  ngOnDestroy() {
    this.refreshSubscription.unsubscribe();
  }
}

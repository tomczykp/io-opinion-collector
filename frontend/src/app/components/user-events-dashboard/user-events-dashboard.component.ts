import {Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from "@angular/material/paginator";
import {MatTableDataSource} from "@angular/material/table";
import {MatSort} from "@angular/material/sort";
import {EventsService, OC} from "../../services/events.service";
import {AuthService} from "../../services/auth.service";
import {UserService} from "../../services/user.service";

@Component({
  selector: 'app-user-events-dashboard',
  templateUrl: './user-events-dashboard.component.html'
})
export class UserEventsDashboardComponent implements OnInit {
  displayedColumns: string[] = ['status', 'description', 'action'];
  dataSource: MatTableDataSource<OC.Event>;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  events: OC.Event[];

  constructor(
    private eventsService: EventsService,
    private authService: AuthService,
    private userService: UserService
  ) {
  }

  ngOnInit(): void {
    this.authService.authenticated.subscribe((authenticated) => {
      if (authenticated) {
        this.userService.getUser().subscribe((user) => {
          let userID = user.id.toString();

          this.eventsService.getUserEvents(userID).subscribe((events) => {
            this.events = events;

            this.dataSource = new MatTableDataSource(this.events);
            this.dataSource.paginator = this.paginator;
            this.dataSource.sort = this.sort;
          })
        })
      }
    })
  }
}

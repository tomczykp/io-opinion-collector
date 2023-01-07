import {AfterViewInit, Component, OnDestroy, OnInit, Output, ViewChild} from '@angular/core';
import {MatTableDataSource, MatTableModule} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {EventsService, OC} from "../../services/events.service";
import {interval, Subscription} from "rxjs";

@Component({
  selector: 'app-events',
  templateUrl: './admin-events-dashboard.component.html',
  styleUrls: ['./admin-events-dashboard.component.css']
})
export class AdminEventsDashboardComponent implements OnInit, OnDestroy {
  displayedColumns: string[] = ['userName', 'description', 'status', 'action'];
  dataSource: MatTableDataSource<OC.BasicEvent>;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  events: OC.BasicEvent[];

  counter = interval(1000);
  refreshSubscription: Subscription

  isOpenedOnly: boolean = true;
  userFilter: string = "";

  constructor(
    private eventsService: EventsService,
  ) {
  }

  ngOnInit(): void {
    this.getEvents()
    this.refreshSubscription = this.counter.subscribe(value => {
      this.getEvents();
    })
  }

  ngOnDestroy() {
    this.refreshSubscription.unsubscribe();
  }

  getEvents(): void {
    this.eventsService.getEvents().subscribe(value => {
      this.events = value;

      if (this.isOpenedOnly) {
        this.events = this.events.filter(function (event) {
          return event.status != 'Closed';
        });
      }

      if (this.userFilter != "") {
        let filter = this.userFilter.toLowerCase();

        this.events = this.events.filter((event) => {
          return event.userName.toLowerCase().includes(filter);
        })
      }

      this.dataSource = new MatTableDataSource(this.events);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    })
  }

  closeEvent(id: string): void {
    this.eventsService.closeEvent(id);
    this.getEvents();
  }

  clearFilter(): void {
    this.userFilter = "";
    this.getEvents();
  }

}

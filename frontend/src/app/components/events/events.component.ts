import {AfterViewInit, Component, OnInit, Output, ViewChild} from '@angular/core';
import {EventsService, OC} from "../../services/events.service";
import {MatTableDataSource, MatTableModule} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {Observable} from "rxjs";

@Component({
  selector: 'app-events',
  templateUrl: './events.component.html',
  styleUrls: ['./events.component.css']
})
export class EventsComponent implements OnInit, AfterViewInit {
  displayedColumns: string[] = ['userID', 'description', 'status', 'action'];
  dataSource: MatTableDataSource<OC.Event>;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  events: OC.Event[];

  constructor(private eventsService: EventsService) {
  }

  ngOnInit(): void {
  }

  ngAfterViewInit() {
    this.getEvents()
  }

  getEvents(): void {
    this.eventsService.getEvents().subscribe(value => {
      this.events = value;

      this.dataSource = new MatTableDataSource(this.events);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    })
  }

  closeEvent(id: string): void {
    this.eventsService.closeEvent(id);
    location.reload();
  }

}

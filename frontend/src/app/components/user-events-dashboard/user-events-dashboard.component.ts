import {Component, Input, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from "@angular/material/paginator";
import {MatTableDataSource} from "@angular/material/table";
import {MatSort} from "@angular/material/sort";
import {EventsService, OC} from "../../services/events.service";
import {AuthService} from "../../services/auth.service";
import {UserService} from "../../services/user.service";
import {interval, Subscription} from "rxjs";
import {Router} from "@angular/router";
import {ProductsService} from "../../services/products.service";
import {OpinionService} from "../../services/opinion.service";
import {QAService} from "../../services/qa.service";

@Component({
  selector: 'app-user-events-dashboard',
  templateUrl: './user-events-dashboard.component.html'
})
export class UserEventsDashboardComponent implements OnInit, OnDestroy {
  displayedColumns: string[] = ['status', 'product', 'description', 'dismiss', 'goto'];
  dataSource: MatTableDataSource<OC.BasicEvent>;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  events: OC.BasicEvent[] = [];


  counter = interval(5000);
  refreshSubscription: Subscription;

  isOpenedOnly: boolean = true;

  constructor(
    private eventsService: EventsService,
    private authService: AuthService,
    private opinionService: OpinionService,
    private qaService: QAService,
    private router: Router
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
        this.eventsService.getUserEvents().subscribe((events) => {
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
      }
    })
  }

  dismissEvent(id: string): void {
    this.eventsService.closeEvent(id).subscribe(() => {
      this.getEvents();
    });
  }

  goToEvent(eventID: string): void {
    this.eventsService.getEvent(eventID).subscribe((event) => {
      if (event.type == 'answerNotify') {
        this.qaService.getQuestion(event.questionID).subscribe((question) => {
          let targetProductID = question.productId;
          const url = this.router.serializeUrl(this.router.createUrlTree([`products/${targetProductID}`], {queryParams: {'highlightQuestion': event.questionID}}));
          window.open(url, '_blank');
        });
      }
    })
  }

  ngOnDestroy() {
    this.refreshSubscription.unsubscribe();
  }
}

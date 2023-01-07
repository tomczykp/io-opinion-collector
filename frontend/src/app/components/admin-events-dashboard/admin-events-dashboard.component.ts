import {AfterViewInit, Component, OnDestroy, OnInit, Output, ViewChild} from '@angular/core';
import {MatTableDataSource, MatTableModule} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {EventsService, OC} from "../../services/events.service";
import {interval, Subscription} from "rxjs";
import {QAService} from "../../services/qa.service";
import {Router} from "@angular/router";
import {ProductsService} from "../../services/products.service";

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
    private qaService: QAService,
    private productService: ProductsService,
    private router: Router,
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

  answerEvent(eventID: string): void {
    this.eventsService.getEvent(eventID).subscribe((event) => {
      console.log(event)

      if (event.type == 'answerReport'
        || event.type == 'questionReport'
        || event.type == 'questionNotify') {
        this.qaService.getQuestion(event.questionID).subscribe((question) => {
          let targetProductID = question.productId;
          const url = this.router.serializeUrl(this.router.createUrlTree([`products/:${targetProductID}`]));
          window.open(url, '_blank');
        });
      }
      else if (event.type == 'opinionReport'){
        let targetProductID = event.productID;
        const url = this.router.serializeUrl(this.router.createUrlTree([`products/:${targetProductID}`]));
        window.open(url, '_blank');
      }
      else if (event.type == 'productReport') {
        this.productService.deleteProduct(event.productID);
        this.closeEvent(event.eventID);
      }
      else if (event.type == 'productSuggestion') {
        this.productService.confirmProduct(event.productID).subscribe((response) => {
          if (response.status == 200)
          {
            this.closeEvent(event.eventID);
          }
        });
      }

    })
  }

}

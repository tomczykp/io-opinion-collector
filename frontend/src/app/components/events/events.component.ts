import { Component, OnInit } from '@angular/core';
import {EventsService, OC} from "../../services/events.service";

@Component({
  selector: 'app-events',
  templateUrl: './events.component.html',
  styleUrls: ['./events.component.css']
})
export class EventsComponent implements OnInit {

  events: Array<OC.Event>;
  constructor(private eventsService: EventsService) { }

  ngOnInit(): void {
    this.eventsService.getEvents().subscribe(value =>{
      this.events = value;
    })
  }

  closeEvent(id: string): void {
    this.eventsService.closeEvent(id);
  }

}

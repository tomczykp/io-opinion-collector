import {Component, OnInit} from '@angular/core';
import {User} from "../../model/User";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {

  users: User[];
  email = "";

  constructor() {
  }

  ngOnInit(): void {
    this.email = localStorage.getItem("email")!;
  }
}

import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-admin-nav',
  templateUrl: './admin-nav.component.html'
})
export class AdminNavComponent implements OnInit {

  constructor() { }

  email = "";

  ngOnInit(): void {
    this.email = localStorage.getItem("email")!;
  }

}

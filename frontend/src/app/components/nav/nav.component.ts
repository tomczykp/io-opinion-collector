import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css']
})
export class NavComponent implements OnInit {

  authenticated = false;
  email = "";

  constructor(public authService: AuthService) { }

  ngOnInit(): void {
    this.authService.authenticated.subscribe((change) => {
      this.authenticated = change;
      if (this.authenticated) {
        this.email = localStorage.getItem("email")!;
      }
    })
  }

  onLogout() {
    this.authService.logout();
  }
}

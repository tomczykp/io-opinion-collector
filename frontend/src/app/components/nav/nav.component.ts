import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css']
})
export class NavComponent implements OnInit {

  authenticated = false;
  username = "";
  role = "";

  constructor(public authService: AuthService) { }

  ngOnInit(): void {
    this.authService.authenticated.subscribe((change) => {
      this.authenticated = change;
      if (this.authenticated) {
        this.username = this.authService.getUsername()!;
        this.role = this.authService.getRole()!;
      }
    })
  }

  onLogout() {
    this.authService.logout();
  }
}

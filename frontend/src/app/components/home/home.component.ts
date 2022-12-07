import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  text = "";
  authenticated = false;

  constructor(public authService: AuthService) { }

  ngOnInit(): void {

    this.authService.authenticated.subscribe((change) => {
      this.authenticated = change;

      if (this.authenticated) {
        this.text = localStorage.getItem("email")!;
      } else {
        this.text = "";
      }
    })
  }

}

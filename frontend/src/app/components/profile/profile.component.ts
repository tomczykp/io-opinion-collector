import { Component, OnInit } from '@angular/core';
import {UserService} from "../../services/user.service";
import {User} from "../../model/User";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  user: User | undefined;

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.userService.getUser().subscribe((user) => {
      this.user = user;
    })
  }

  removeUser() {
    this.userService.removeByUser()
    console.log("Email send");
  }
}

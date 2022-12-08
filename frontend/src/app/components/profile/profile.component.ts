import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  ngOnInit(): void {
  }

  constructor(private userService: UserService) { }

  removeUser() {
    this.userService.removeByUser()
    console.log("Email send");
  }
}

import { Component, OnInit, ViewChild } from '@angular/core';
import { UserService } from 'src/app/services/user.service';
import {User} from "../../model/User";
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';
import * as _ from "underscore";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {

  @ViewChild('paginator') paginator:MatPaginator;
  @ViewChild(MatSort) sort: MatSort
  dataSource: MatTableDataSource<User>;
  displayedColumns: string[] = ['username', 'email', 'active', 'role', 'ban', 'delete'];

  users: User[];
  email = "";
  filteremail = "";

  constructor(
    private userService: UserService,
  ) { }

  ngOnInit(): void {
    this.email = localStorage.getItem("email")!;
  }

  ngAfterViewInit() {
      this.getUsers();
  }


  getUsers() {
    this.userService.getUsers(this.filteremail).subscribe((users) => {
      this.users = users.filter(function (user) {
        return user.role != 'ADMIN';
      });
      this.users = _.sortBy(this.users, 'id');
      this.dataSource = new MatTableDataSource(this.users);
      this.dataSource.paginator = this.paginator;
    })
  }

  removeUser(email: String) {
    this.userService.removeByAdmin(email).subscribe((result) => {
    console.log(result.status);
    this.getUsers();
    });
  }

  lockUser(email: String) {
    this.userService.lock(email).subscribe((result) => {
      //console.log(result.status);
      this.getUsers();
      });
  }

  unlockUser(email: String) {
    this.userService.unlock(email).subscribe((result) => {
      //console.log(result.status);
      this.getUsers();
      });
  }
}

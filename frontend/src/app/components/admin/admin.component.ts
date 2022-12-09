import { Component, OnInit, ViewChild } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';
import { UserService } from 'src/app/services/user.service';
import {User} from "../../model/User";
import {Router} from "@angular/router";
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';
import * as _ from "underscore";

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html'
})
export class AdminComponent implements OnInit {

  @ViewChild('paginator') paginator:MatPaginator;
  @ViewChild(MatSort) sort: MatSort
  dataSource: MatTableDataSource<User>;
  displayedColumns: string[] = ['ID', 'username', 'email', 'active', 'role', 'ban', 'delete'];

  users: User[];
  email = "";
  filteremail = "";

  constructor(
    private userService: UserService,
    private router: Router
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

  removeUser(id: number) {
    this.userService.removeByAdmin(id).subscribe((result) => {
    console.log(result.status);
    this.getUsers();
    });
  }

  lockUser(id: number) {
    this.userService.lock(id).subscribe((result) => {
      console.log(result.status);
      this.getUsers();
      });
  }

  unlockUser(id: number) {
    this.userService.unlock(id).subscribe((result) => {
      console.log(result.status);
      this.getUsers();
      });
  }
}

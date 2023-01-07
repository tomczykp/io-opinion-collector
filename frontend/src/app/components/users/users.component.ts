import {Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {MatTableDataSource} from "@angular/material/table";
import {User} from "../../model/User";
import {UserService} from "../../services/user.service";
import * as _ from "underscore";

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html'
})
export class UsersComponent implements OnInit {

  @ViewChild('paginator') paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort
  dataSource: MatTableDataSource<User>;
  displayedColumns: string[] = ['username', 'email', 'active', 'role', 'ban', 'delete'];

  users: User[];
  email = "";
  emailFilter = "";

  constructor(
    private userService: UserService,
  ) {
  }

  ngOnInit(): void {
    this.email = localStorage.getItem("email")!;
  }

  ngAfterViewInit() {
    this.getUsers();
  }


  getUsers() {
    this.userService.getUsers(this.emailFilter).subscribe((users) => {
      this.users = users.filter(function (user) {
        return user.role != 'ADMIN';
      });
      this.users = _.sortBy(this.users, 'id');
      this.dataSource = new MatTableDataSource(this.users);
      this.dataSource.paginator = this.paginator;
    })
  }

  removeUser(email: String) {
    if (confirm("Do you really want to delete user with email: " + email + "? This action can't be undone")) {
      this.userService.removeByAdmin(email).subscribe((result) => {
        this.getUsers();
      });
    }
  }

  lockUser(email: String) {
    this.userService.lock(email).subscribe((result) => {
      this.getUsers();
    });
  }

  unlockUser(email: String) {
    this.userService.unlock(email).subscribe((result) => {
      this.getUsers();
    });
  }

  clearFilter() {
    this.emailFilter = "";
    this.getUsers();
  }
}

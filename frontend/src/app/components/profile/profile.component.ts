import { Component, OnInit } from '@angular/core';
import {UserService} from "../../services/user.service";
import {User} from "../../model/User";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  changePasswordForm = new FormGroup({
    oldPassword: new FormControl('', [Validators.required]),
    repeatedPassword: new FormControl('', [Validators.required]),
    newPassword: new FormControl('', [Validators.required])
  })

  get oldPassword() {
    return this.changePasswordForm.get('oldPassword');
  }

  get newPassword() {
    return this.changePasswordForm.get('newPassword');
  }

  get repeatedPassword() {
    return this.changePasswordForm.get('repeatedPassword');
  }

  user: User | undefined;
  passwordChangeStatus = 0
  // 0 if no display message
  // 1 if successful password change
  // 2 if failed password change
  // 3 if newPassword and repeatedPassword not match

  constructor(
    private userService: UserService,
    private authService: AuthService) { }

  ngOnInit(): void {
    this.userService.getUser().subscribe((user) => {
      this.user = user;
    })
  }

  forceLogout() {
    this.authService.logoutFromAllDevices();
  }

  onPasswordChangeSubmit() {
    if (this.changePasswordForm.valid) {

      let oldP = this.changePasswordForm.getRawValue().oldPassword;
      let newP = this.changePasswordForm.getRawValue().newPassword;
      let repeatedP = this.changePasswordForm.getRawValue().repeatedPassword;

      if (newP !== repeatedP) {
        this.passwordChangeStatus = 3
        return
      }

      this.userService.changePassword(oldP!.toString(), newP!.toString())
        .subscribe((result) => {
          console.log(result.status)
          if (result.status == 200) {
            console.log("Success")
            this.passwordChangeStatus = 1
          }
        }, (error) => {
          console.log(error)
          this.oldPassword?.reset()
          this.passwordChangeStatus = 2
        });
    }
  }
  removeUser() {
    this.userService.removeByUser()
    console.log("Email send");
  }
}

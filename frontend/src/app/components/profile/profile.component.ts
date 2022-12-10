import { Component, OnInit } from '@angular/core';
import {UserService} from "../../services/user.service";
import {User} from "../../model/User";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
})
export class ProfileComponent implements OnInit {

  changePasswordForm = new FormGroup({
    oldPassword: new FormControl('', [Validators.required]),
    repeatedPassword: new FormControl('', [Validators.required]),
    newPassword: new FormControl('', [Validators.required])
  })

  showUsernameChangeInput = false;
  newUsername = "";

  showDeletionLinkSent = false;

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
  passwordChangeStatus = 0;
  usernameChangeStatus = 0;
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

  showUsernameChange() {
    this.newUsername = this.user!.visibleName;
    this.showUsernameChangeInput = true;
  }

  changeUsername() {
    if (this.user?.visibleName == this.newUsername) {
      this.usernameChangeStatus = 3
      return
    }
    if (this.newUsername == "") {
      this.usernameChangeStatus = 4
      return
    }

    this.userService.changeUsername(this.newUsername).subscribe((result) => {
      if (result.status === 200) {
        this.usernameChangeStatus = 1;
        this.newUsername = "";
        this.ngOnInit();
      }
    }, (error) => {
      this.usernameChangeStatus = 2;
    })
    this.showUsernameChangeInput = false;
  }

  cancelUsernameChange() {
    this.usernameChangeStatus = 0;
    this.showUsernameChangeInput = false;
    this.newUsername = "";
  }

  deleteAccount() {
    this.userService.removeByUser()
    this.showDeletionLinkSent = true;
  }
}

import {Component, OnInit} from '@angular/core';
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
    newPassword: new FormControl('', [Validators.required, Validators.minLength(8), Validators.maxLength(20)])
  })

  showUsernameChangeInput = false;
  newUsername = "";

  oldPasswordInputTextType = false;
  newPasswordInputTextType = false;
  repeatPasswordInputTextType = false;

  deletionStatus = 0;
  user: User | undefined;
  passwordChangeStatus = 0;
  // 0 if no display message
  // 1 if successful password change
  // 2 if failed password change
  // 3 if newPassword and repeatedPassword not match

  usernameChangeStatus = 0;

  constructor(
    private userService: UserService,
    private authService: AuthService) {
  }

  get oldPassword() {
    return this.changePasswordForm.get('oldPassword');
  }

  get newPassword() {
    return this.changePasswordForm.get('newPassword');
  }

  get repeatedPassword() {
    return this.changePasswordForm.get('repeatedPassword');
  }

  ngOnInit(): void {
    this.getUser();
  }

  getUser() {
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
        this.passwordChangeStatus = 3;
        this.repeatedPassword?.setErrors({notSame: true});
        return;
      }

      if (newP === oldP) {
        this.passwordChangeStatus = 4;
        this.newPassword?.setErrors({identicalPasswords: true});
        return;
      }

      this.userService.changePassword(oldP!.toString(), newP!.toString())
        .subscribe((result) => {
          if (result.status == 200) {
            this.changePasswordForm.reset();
            this.passwordChangeStatus = 1;
          }
        }, (error) => {
          this.oldPassword?.reset()
          this.oldPassword?.setErrors({wrongPassword: true});
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
      this.usernameChangeStatus = 3;
      return;
    }
    if (this.newUsername == "") {
      this.usernameChangeStatus = 4;
      return;
    }

    this.userService.changeUsername(this.newUsername).subscribe((result) => {
      if (result.status === 200) {
        this.usernameChangeStatus = 1;
        this.authService.setUsername(this.newUsername);
        this.newUsername = "";
        this.getUser();
        this.showUsernameChangeInput = false;
      }
    }, (error) => {
      this.usernameChangeStatus = 2;
    })
  }

  cancelUsernameChange() {
    this.usernameChangeStatus = 0;
    this.showUsernameChangeInput = false;
    this.newUsername = "";
  }

  deleteAccount() {
    this.userService.removeByUser().subscribe((result) => {
      if (result.status == 200) {
        this.deletionStatus = 1;
      }
    }, (error) => {
      this.deletionStatus = 2;
    })
  }

}

import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {UserService} from 'src/app/services/user.service';

@Component({
  selector: 'app-reset',
  templateUrl: './reset.component.html'
})
export class ResetComponent implements OnInit {

  emailStatus = 0;

  resetPasswordForm = new FormGroup({
    email: new FormControl('', [Validators.required])
  })

  constructor(private userService: UserService) {
  }

  get email() {
    return this.resetPasswordForm.get('email');
  }

  ngOnInit(): void {
  }

  resetPassword() {
    let email = this.resetPasswordForm.getRawValue().email;
    this.userService.resetPassword(email!.toString())
      .subscribe((result) => {
        console.log(result.status);
        if (result.status == 200) {
          this.email?.reset();
          this.emailStatus = 1;
        }
      }, (error) => {
        if (error.status == 409) {
          this.emailStatus = 4;
        } else if (error.status == 400) {
          this.emailStatus = 2;
        } else {
          this.emailStatus = 3;
        }
        this.email?.reset();
      });
  }
}

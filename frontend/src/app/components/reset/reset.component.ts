import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-reset',
  templateUrl: './reset.component.html'
})
export class ResetComponent implements OnInit {

  emailStatus = 0;

  resetPasswordForm = new FormGroup({
    email: new FormControl('', [Validators.required])
  })

  get email() {
    return this.resetPasswordForm.get('email');
  }

  constructor(private userService: UserService) { }

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
        this.emailStatus = 3;
      } else {
        this.emailStatus = 2;
      }
      this.email?.reset();
    });
  }
}

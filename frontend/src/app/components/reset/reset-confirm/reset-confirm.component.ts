import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import {Router} from "@angular/router";

@Component({
  selector: 'app-reset-confirm',
  templateUrl: './reset-confirm.component.html'
})
export class ResetConfirmComponent implements OnInit {

  token: string = "";
  passwordChangeStatus = 0;

  confirmPassword = new FormGroup({
    password: new FormControl('', [Validators.required]),
    repeatPassword: new FormControl('', [Validators.required])
  })

  get password() {
    return this.confirmPassword.get('password');
  }

  get repeatPassword() {
    return this.confirmPassword.get('repeatPassword');
  }

  constructor(private route: ActivatedRoute,
              private userService: UserService,
              private router: Router) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      this.token = params.get('token')!.toString();
    })
  }

  resetPassword() {
    if (this.confirmPassword.valid) {
    let pass = this.confirmPassword.getRawValue().password;
    let repeat = this.confirmPassword.getRawValue().repeatPassword;

    if (pass !== repeat) {
      this.passwordChangeStatus = 3;
      return;
    }

      this.userService.confirmResetPassword(pass!.toString(), this.token)
      .subscribe((result) => {
        console.log(result.status)
        if (result.status == 200) {
          this.router.navigate(['/login']);
        }
      }, (error) => {
        console.log(error);
        this.passwordChangeStatus = 2;
        this.password?.reset();
        this.repeatPassword?.reset();
      });
    }
  }
}

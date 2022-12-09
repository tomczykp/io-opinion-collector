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

  confirmPassword = new FormGroup({
    password: new FormControl('', [Validators.required]),
    repeatedPassword: new FormControl('', [Validators.required])
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
    let pass = this.confirmPassword.getRawValue().password;
    let repeat = this.confirmPassword.getRawValue().repeatedPassword;
      this.userService.confirmResetPassword(pass!.toString(), this.token)
      .subscribe((result) => {
        this.router.navigate(['/login']);
      })
      this.router.navigate(['/login']);
  }
}

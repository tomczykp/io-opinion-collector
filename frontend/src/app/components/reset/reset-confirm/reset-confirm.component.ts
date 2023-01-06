import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {UserService} from 'src/app/services/user.service';

@Component({
  selector: 'app-reset-confirm',
  templateUrl: './reset-confirm.component.html'
})
export class ResetConfirmComponent implements OnInit {

  token: string = "";
  passwordChangeStatus = 0;

  confirmPassword = new FormGroup({
    password: new FormControl('', [Validators.required, Validators.minLength(8), Validators.maxLength(20)]),
    repeatPassword: new FormControl('', [Validators.required, Validators.minLength(8), Validators.maxLength(20)])
  })

  constructor(private route: ActivatedRoute,
              private userService: UserService,
              private router: Router) {
  }

  get password() {
    return this.confirmPassword.get('password');
  }

  get repeatPassword() {
    return this.confirmPassword.get('repeatPassword');
  }

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
        this.repeatPassword?.setErrors({notSame: true})
        return;
      }

      this.userService.confirmResetPassword(pass!.toString(), this.token)
        .subscribe((result) => {
          if (result.status == 200) {
            this.router.navigate(['/login'], {queryParams: {'password-reset-success': true}});
          }
        }, (error) => {
          if (error.status = 401) {
            this.router.navigate(['/login'], {queryParams: {'reset-expired': true}});
          } else if (error.status = 400) {
            this.router.navigate(['/login'], {queryParams: {'token-deleted': true}});
          }else {
            this.passwordChangeStatus = 2;
          }
          this.password?.reset();
          this.repeatPassword?.reset();
        });
    }
  }
}

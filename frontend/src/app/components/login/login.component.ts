import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../services/auth.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
})
export class LoginComponent implements OnInit {

  loginForm = new FormGroup({
    email: new FormControl('', [Validators.required]), //Validators.email]),
    password: new FormControl('', [Validators.required])
  })

  wrongCredentials = false;
  accountLocked = false;
  registerSuccessful = false;
  logoutSuccessful = false;
  sessionExpired = false;

  get email() {
    return this.loginForm.get('email');
  }

  get password() {
    return this.loginForm.get('password');
  }

  constructor(
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    let params = this.route.snapshot.queryParamMap;
    this.logoutSuccessful = params.has('logout-success');
    this.registerSuccessful = params.has('register-success');
    this.sessionExpired = params.has('session-expired');
  }

  clearPassword() {
    this.loginForm.get('password')?.reset();
  }

  onSubmit() {
    if (this.loginForm.valid) {
      let email = this.loginForm.getRawValue().email;
      let password = this.loginForm.getRawValue().password;

      this.authService.login(email!.toString(), password!.toString())
        .subscribe((result) => {
          if (result.status == 200) {
            this.authService.saveUserData(result)
            this.authService.authenticated.next(true);
            this.router.navigate(['/']);
          }
      }, (error) => {
          this.authService.clearUserData();
          this.authService.authenticated.next(false);
          this.clearPassword();
          if (error.status === 401) {
            this.wrongCredentials = true;
          } else if (error.status === 423) {
            this.accountLocked = true;
          }
          this.registerSuccessful = false;
          this.logoutSuccessful = false;
          this.sessionExpired = false;
        });
    }
  }

}

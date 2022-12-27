import {Component, OnInit} from '@angular/core';
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
  googleLoginFailed = false;
  accountDeletedSuccessful = false;
  accountConfirmed = false;
  passwordResetSuccessful = false;
  accountDisabled = false;
  accountDeleted = false;
  differentProvider = false;

  constructor(
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {
  }

  get email() {
    return this.loginForm.get('email');
  }

  get password() {
    return this.loginForm.get('password');
  }

  ngOnInit(): void {
    let params = this.route.snapshot.queryParamMap;
    this.logoutSuccessful = params.has('logout-success');
    this.registerSuccessful = params.has('register-success');
    this.sessionExpired = params.has('session-expired');
    this.accountLocked = params.has('account-locked');
    this.googleLoginFailed = params.has('google_auth-failed');
    this.accountDeletedSuccessful = params.has('deleted');
    this.accountConfirmed = params.has('confirmed');
    this.passwordResetSuccessful = params.has('password-reset-success');
    this.accountDeleted = params.has('account-deleted');
    this.differentProvider = params.has('different-provider');

    if (params.has("google_code")) {
      this.exchangeGoogleCode(params.get('google_code')!);
      this.router.navigate(['/']);
    }

    if (params.has("facebook_code")) {
      this.exchangeFacebookCode(params.get('facebook_code')!);
      this.router.navigate(['/']);
    }

    if (this.accountDeletedSuccessful) {
      this.authService.clearUserData();
      this.authService.authenticated.next(false);
    }
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
          } else if (error.status === 406) {
            this.accountDisabled = true;
          }
          this.registerSuccessful = false;
          this.logoutSuccessful = false;
          this.sessionExpired = false;
        });
    }
  }


  loginWithGoogle() {
    this.authService.loginWithGoogle()
  }

  loginWithFacebook() {
    this.authService.loginWithFacebook()
  }

  exchangeGoogleCode(code: string) {
    this.authService.authByGoogleCode(code).subscribe((result) => {
      if (result.status == 200) {
        this.authService.saveUserData(result)
        this.authService.authenticated.next(true);
      }
    }, error => {
      if (error.status === 423) {
        this.router.navigate(['/login'], {queryParams: {'account-locked': true}});
      } else if (error.status === 404) {
        this.router.navigate(['/login'], {queryParams: {'account-deleted': true}})
      } else if (error.status === 409) {
        this.router.navigate(['/login'], {queryParams: {'different-provider': true}})
      } else {
        this.router.navigate(['/login'], {queryParams: {'google_auth-failed': true}})
      }
    })
  }

  private exchangeFacebookCode(code: string) {

    this.authService.authByFacebookCode(code).subscribe((result) => {
      if (result.status == 200) {
        this.authService.saveUserData(result)
        this.authService.authenticated.next(true);
      }
    }, error => {
      if (error.status === 423) {
        this.router.navigate(['/login'], {queryParams: {'account-locked': true}});
      } else if (error.status === 404) {
        this.router.navigate(['/login'], {queryParams: {'account-deleted': true}})
      } else if (error.status === 409) {
        this.router.navigate(['/login'], {queryParams: {'different-provider': true}})
      } else {
        this.router.navigate(['/login'], {queryParams: {'facebook_auth-failed': true}})
      }
    })
  }
}

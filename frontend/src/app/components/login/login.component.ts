import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm = new FormGroup({
    email: new FormControl('', [Validators.required]), //Validators.email]),
    password: new FormControl('', [Validators.required])
  })

  failedLogin = false;

  get email() {
    return this.loginForm.get('email');
  }

  get password() {
    return this.loginForm.get('password');
  }

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {}

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
            localStorage.setItem("email", result.body!.email)
            localStorage.setItem("jwt", result.body!.jwt)
            localStorage.setItem("refreshToken", result.body!.refreshToken)
            localStorage.setItem("role", result.body!.role)
            this.authService.authenticated.next(true);
            console.log("Successful login")
            this.router.navigate(['/']);
          }
      }, (error) => {
          localStorage.removeItem("email")
          localStorage.removeItem("jwt")
          localStorage.removeItem("refreshToken")
          localStorage.removeItem("role")
          this.authService.authenticated.next(false);
          console.log("No authentication");
          this.clearPassword();
          this.failedLogin = true;
        });
    }
  }

}

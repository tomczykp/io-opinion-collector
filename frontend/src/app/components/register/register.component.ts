import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
})
export class RegisterComponent implements OnInit {

  registerForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    username: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required, Validators.minLength(8), Validators.maxLength(20)])
  })

  failedRegister = false;
  passwordInputTextType: boolean;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {
  }

  get email() {
    return this.registerForm.get('email');
  }

  get password() {
    return this.registerForm.get('password');
  }

  get username() {
    return this.registerForm.get('username');
  }

  ngOnInit(): void {
  }

  clearPassword() {
    this.registerForm.get('password')?.reset();
  }

  onSubmit() {
    if (this.registerForm.valid) {
      let email = this.registerForm.getRawValue().email;
      let username = this.registerForm.getRawValue().username;
      let password = this.registerForm.getRawValue().password;

      this.authService.register(email!.toString(), username!.toString(), password!.toString())
        .subscribe((result) => {
          if (result.status == 201) {
            this.router.navigate(['/login'], {queryParams: {'register-success': true}});
          } else {
            this.clearPassword();
          }
        }, (error) => {
          this.failedRegister = true;
        });
    }
  }

  loginWithGoogle() {
    this.authService.loginWithGoogle()
  }

  loginWithFacebook() {
    this.authService.loginWithFacebook()
  }
}

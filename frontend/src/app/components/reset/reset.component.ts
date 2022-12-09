import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-reset',
  templateUrl: './reset.component.html'
})
export class ResetComponent implements OnInit {

  token: string = "";

  resetPasswordForm = new FormGroup({
    email: new FormControl('', [Validators.required])
  })

  get email() {
    return this.resetPasswordForm.get('email');
  }

  constructor(private route: ActivatedRoute,
              private userService: UserService) { }

  ngOnInit(): void {
  }

  resetPassword() {
    let email= this.resetPasswordForm.getRawValue().email;
    console.log(email);
    this.userService.resetPassword(email!.toString()).subscribe((result) => {
       if (result.status == 200) {
        console.log("Email has been send");
       }
    });
  }
}
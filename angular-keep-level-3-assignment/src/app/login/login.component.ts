import { Component, OnInit } from '@angular/core';

import { FormGroup, FormControl, Validators } from '@angular/forms';


import { AuthenticationService } from '../services/authentication.service';

import { RouterService } from '../services/router.service';

import { User } from '../User';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  submitMessage: string;
  username = new FormControl('', [Validators.required]);
  password = new FormControl('', [Validators.required, Validators.minLength(6)]);
  loginForm = new FormGroup({
    username: this.username,
    password: this.password
  });
  user: User;
  errMessage: string;
  successMessage: string;

  // Injecting Authentication Service instance and route service
  constructor(private authenticationService: AuthenticationService, private routerService: RouterService) {
    this.user = new User();
   }

  ngOnInit() {
  }

  loginSubmit() {
    this.authenticationService.authenticateUser(this.loginForm.value).subscribe(
      (data) => {
        this.authenticationService.setBearerToken(data['token']);
        this.authenticationService.setUserNameToSession(this.username.value)
        this.routerService.routeToDashboard();
      },
      (error) => {
        this.submitMessage = error.message;
        if (error.status === 403) {
          this.submitMessage = error.error.message;
        }
      }
    );

  }

  public signUp(){
    if (this.user.username === '' || this.user.password === '') {
      this.errMessage = 'username and password both are required fields';
      return;
    }
    this.authenticationService.registerUser(this.user).then((res) => {
      if(res) {
        this.successMessage = `The user ${this.user.username} was registered successfully`;
        this.user = new User();
      } else {
        this.errMessage = `The user ${this.user.username} was not registered successfully`;
      }
    }).catch(error => {
      this.errMessage = error.message;
    })
  }

}

import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

// for .map to work
import 'rxjs/add/operator/map';

@Injectable()
export class AuthenticationService {

  constructor(private httpClient: HttpClient) {

  }

  setUserNameToSession(userId) {
    sessionStorage.setItem("userId", userId);
  }

  getUserNameFromSession() {
    return sessionStorage.getItem("userId");
  }

  authenticateUser(data) {
    return this.httpClient.post('http://localhost:8089/api/v1/auth/login', data);
  }

  setBearerToken(token) {
    localStorage.setItem('bearerToken', token);
  }

  getBearerToken() {
    return localStorage.getItem('bearerToken');
  }

  registerUser(user): Promise<boolean> {
    return this.httpClient.post('http://localhost:8089/api/v1/auth/register', user).map(res => {
      return res['isCreated'];
    })
    .toPromise();
  }

  isUserAuthenticated(token): Promise<boolean> {
    return this.httpClient.post('http://localhost:8089/api/v1/auth/isAuthenticated', {},
      {
        headers: new HttpHeaders()
          .set('Authorization', `Bearer ${token}`)
      })
      .map(res => {
        return res['isAuthenticated'];
      })
      .toPromise();
  }
}

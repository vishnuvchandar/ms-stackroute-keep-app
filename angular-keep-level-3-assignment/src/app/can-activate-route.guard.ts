import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import { RouterService } from './services/router.service';
import { AuthenticationService } from './services/authentication.service';

@Injectable()
export class CanActivateRouteGuard implements CanActivate {

  constructor(private routerService: RouterService, private authenticationService: AuthenticationService) { }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    return this.authenticationService.isUserAuthenticated(this.authenticationService.getBearerToken())
      .then((isUserAuthenticated) => {
        if (!isUserAuthenticated) {
          this.routerService.routeToLogin();
        }
        return isUserAuthenticated;
      });

  }
}

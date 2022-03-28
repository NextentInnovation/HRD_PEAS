import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthenticationService } from '../authentication/authentication.service';
import { SiteURLS } from '../constants/urls/site-url.constant';
import { StorageControl } from '../storage/storage.control';

@Injectable()
export class AuthGuard implements CanActivate {

  constructor(
    private router: Router,
    private authentication: AuthenticationService ) {
  }

  canActivate() {
    console.log('Autguard: ', this.authentication.isAuthenticated());
    if ( this.authentication.isAuthenticated() ) {
      return true;
    } else {
      this.router.navigate( [ SiteURLS.LOGIN.BASE ] );
      StorageControl.clearLoggedInUserSession();
      console.log('logout AuthGuard');
      this.authentication.logout().subscribe(
        () => {
          console.log('logout success');
        }
      );
      return false;
    }
    return true;
  }

}

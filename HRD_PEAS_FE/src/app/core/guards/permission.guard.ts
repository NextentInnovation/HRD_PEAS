import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { StorageControl } from '../storage/storage.control';
import { NotificationService } from '@src/app/core/notification/notification.service';

@Injectable()
export class PermissionGuard implements CanActivate {
  constructor( public router: Router,
               private notification: NotificationService
  ) {
  }

  canActivate( route: ActivatedRouteSnapshot, state: RouterStateSnapshot ): boolean {
    let ret = false;

    const userPermissions: Array<string> = JSON.parse( StorageControl.get( 'PERMISSION' ) );
    const expectedPermissions = route.data.permissions ? route.data.permissions : null;

    if ( expectedPermissions && expectedPermissions.some( value => userPermissions.find( element => element.indexOf( value ) >= 0 ) ) ) {
      ret = true;
    }
    if ( !ret ) {
      this.notification.error( { message: 'Nincs jogosultsága az oldal megtekintéséhez!' } );
    }

    return ret;
  }
}

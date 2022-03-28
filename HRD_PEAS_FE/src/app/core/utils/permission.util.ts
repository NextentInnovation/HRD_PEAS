import { Injectable } from '@angular/core';
import { StorageControl } from '../storage/storage.control';
import { Permissions } from '../constants/permissions.constants';
import { CommonUtils } from './common.util';

@Injectable()
export class PermissionUtils {

  private static userPermissions: Array<string>;

  private static loadUserPermissions() {
    if ( CommonUtils.isEmpty( this.userPermissions ) || this.userPermissions.length < 1 ) {
      this.userPermissions = StorageControl.getPermission();
    }
  }

  public static forceLoadUserPermissions() {
    this.userPermissions = StorageControl.getPermission();
  }

  public static hasAnyPermission( permissions: Array<Permissions> ) {
    this.loadUserPermissions();
    return (permissions && permissions.some(
        value => CommonUtils.isNotEmpty( this.userPermissions.find( element => element === value ) ) ));
  }

  public static hasEveryPermission( permissions: Array<Permissions> ) {
    this.loadUserPermissions();
    return (permissions && permissions.every(
        value => CommonUtils.isNotEmpty( this.userPermissions.find( element => element === value ) ) ));
  }

}

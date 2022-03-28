import { UserDataModel } from '@src/app/models/user/user.data.model';
import { BaseService } from '@src/app/core/services/base.service';

export class StorageControl {

  static add( key, value ) {
    localStorage.setItem( key, value );
  }

  static remove( key ) {
    localStorage.removeItem( key );
  }

  static get( key ) {
    return localStorage.getItem( key );
  }

  static setLanguageCode( languageCode ) {
    this.add( 'LANGUAGE_CODE', languageCode );
  }

  static getLanguageCode() {
    return this.get( 'LANGUAGE_CODE' );
  }

  static getToken() {
    return this.get( 'TOKEN' );
  }

  static setToken( token: string ) {
    return this.add( 'TOKEN', token );
  }

  static getUsername() {
    return this.get( 'USER_NAME' );
  }

  static setUsername( username: string ) {
    return this.add( 'USER_NAME', username );
  }

  static setUserData(userdata: UserDataModel) {
    return this.add( 'USER_DATA', JSON.stringify( userdata ) );
  }

  static getUserData() {
    return JSON.parse( this.get( 'USER_DATA' ) );
  }

  static setPermission(permissions) {
    return this.add( 'PERMISSION', JSON.stringify( permissions ) );
  }

  static getPermission() {
    return JSON.parse( this.get( 'PERMISSION' ) );
  }

  static clearLoggedInUserSession() {
    this.remove('TOKEN');
    this.remove('USER_NAME');
    this.remove('USER_DATA');
    this.remove('PERMISSION');
    BaseService.DropCache();
  }

}

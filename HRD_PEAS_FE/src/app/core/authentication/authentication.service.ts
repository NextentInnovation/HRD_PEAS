import { Injectable } from '@angular/core';
import { ApiURLS } from '../constants/urls/api-url.constant';
import { BaseService } from '../services/base.service';
import { HttpClient } from '@angular/common/http';
import { StorageControl } from '../storage/storage.control';
import { CommonUtils } from '@src/app/core/utils/common.util';

@Injectable( {
  providedIn: 'root'
} )
export class AuthenticationService extends BaseService {

  constructor(
    private http: HttpClient,
  ) {
    super();
  }

  getToken(): string {
    return StorageControl.getToken();
  }

  login( request: any ) {
    return this.http.post<any>( ApiURLS.LOGIN, request );
  }

  logout() {
    return this.http.delete<any>( ApiURLS.LOGOUT );
  }

  public isAuthenticated() {
    return CommonUtils.isNotEmpty(StorageControl.getToken()); // !this.jwtHelper.isTokenExpired( StorageControl.getToken() );
  }
}

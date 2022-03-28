import { Injectable } from '@angular/core';
import { HttpErrorResponse, HttpEvent, HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable, throwError } from 'rxjs/index';
import { fromPromise } from 'rxjs/internal/observable/fromPromise';
import { Router } from '@angular/router';
import { AuthenticationService } from '../authentication/authentication.service';
import { StorageControl } from '../storage/storage.control';
import { SiteURLS } from '../constants/urls/site-url.constant';
import { NotificationService } from './../notification/notification.service';
import { CommonUtils } from '@src/app/core/utils/common.util';
import { catchError } from 'rxjs/operators';

/**
 * A HTTP
 *
 */
@Injectable()
export class RequestHeaderInterceptor implements HttpInterceptor {
  constructor( private auth: AuthenticationService,
               private notification: NotificationService,
               private router: Router ) {
  }

  intercept( request: HttpRequest<any>, next: HttpHandler ): Observable<HttpEvent<any>> {
    return fromPromise( this.handleAccess( request, next ) );
  }

  private async handleAccess( request: HttpRequest<any>, next: HttpHandler ): Promise<HttpEvent<any>> {
    const token = await this.auth.getToken();

    const headerSettings: { [ name: string ]: string | string[]; } = {};

    for ( const key of request.headers.keys() ) {
      headerSettings[ key ] = request.headers.getAll( key );
    }

    const newHeader = new HttpHeaders( headerSettings );
    const changedRequest = request.clone( {
      headers: newHeader
    } );

    headerSettings[ 'Content-Type' ] = 'application/json';
    headerSettings[ 'Accept' ] = 'application/json';
    headerSettings[ 'Language' ] = StorageControl.getLanguageCode();
    // headerSettings[ 'Cache-Control' ] = 'no-cache';

    if ( CommonUtils.isNotEmpty(token) ) {
      headerSettings[ 'x-auth-token' ] = token;
    }

    /*
    if ( !request.url.includes( 'autocomplete' ) ) {
      headerSettings[ 'Language' ] = StorageControl.getLanguageCode();
      // headerSettings[ 'TimeZone-Offset' ] = DateUtils.getTimezoneOffset();
      const newHeader = new HttpHeaders( headerSettings );
      changedRequest = request.clone( {
        headers: newHeader
      } );
    }
    */

    return next.handle( changedRequest ).pipe(
        catchError((err: HttpErrorResponse) => {
          console.log(err);
          if ( err instanceof HttpErrorResponse ) {
            if ( err.status === 401 ) {
              StorageControl.clearLoggedInUserSession();
              this.router.navigate( [ SiteURLS.LOGIN.BASE ] );
              if ( err.error instanceof Array ) {
                this.notification.error( err );
              } else {
                this.notification.info( err.error.message );
              }
            /*
            } else if ( err.status === 404 ) {
              this.notification.info( 'Nem található a kívánt adat!\r\n' + err.error.message );
            */
            } else {
              this.notification.error( err.error.message );
            }
          }
          return throwError(err);
        })).toPromise();
  }

}

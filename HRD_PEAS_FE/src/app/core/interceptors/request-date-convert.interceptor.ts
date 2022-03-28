import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/index';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

/**
 * A HTTP
 *
 */
@Injectable()
export class RequestDateConvertInterceptor implements HttpInterceptor {
    constructor() { }

    intercept( request: HttpRequest<any>, next: HttpHandler ): Observable<HttpEvent<any>> {
        return next.handle(request).pipe( map( (val: HttpEvent<any>) => {
            if (val instanceof HttpResponse) {
                const body = val.body;
                this.convertStringDateToDateObject(body);
            }
            return val;
        }));
    }

    private convertStringDateToDateObject( obj: any ) {
        if (obj && typeof obj === 'object' && Array.isArray(Object.keys(obj))) {
            Object.keys( obj ).forEach( key => {
                const value = obj [ key ];
                if ( typeof value === 'string' ) {
                    obj [ key ] = this.ifDateConvert( value );
                }
                if ( typeof value === 'object' ) {
                    this.convertStringDateToDateObject( obj [ key ] );
                }
            } );
        }
    }

    private ifDateConvert(value: any): Date | any {
        if (moment(value, moment.ISO_8601, true).isValid()) {
            return new Date(value);
        }
        return value;
    }
}

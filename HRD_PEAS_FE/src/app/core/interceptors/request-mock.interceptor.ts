import { Injectable } from '@angular/core';
import { HttpClient, HttpEvent, HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest, HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs/index';
import { environment } from '@src/environments/environment';
import { MockServiceList } from '@src/app/services/mock.service.list';

/**
 * A HTTP
 *
 */
@Injectable()
export class RequestMockInterceptor implements HttpInterceptor {
    constructor(private http: HttpClient) {
    }

    intercept( request: HttpRequest<any>, next: HttpHandler ): Observable<HttpEvent<any>> {
        if ( environment.MOCK ) {
            const mockData = MockServiceList.serviceList.find(
                value => request.url.match( value.url ) && request.url.match( value.url ).length > 0);
            if ( mockData && mockData.mock && mockData.method === request.method) {
                console.log( 'Loaded from mock file : ' + request.url + ' FILE: ' + mockData.mockFile );
                // Ez szöveget tölt be és nem magát a fájlt
                // return of( new HttpResponse( { status: 200, body: ((mockData.mockFile) as any).default } ) );
                return next.handle(request.clone({ url: mockData.mockFile, method: 'GET' }));
            }
        }
        return next.handle( request );
    }
}

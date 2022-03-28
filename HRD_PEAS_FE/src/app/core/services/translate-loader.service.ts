import { Injectable } from '@angular/core';
import { TranslateLoader } from '@ngx-translate/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiURLS } from '../constants/urls/api-url.constant';
import { BaseService } from './base.service';

@Injectable()
export class CustomLoader extends BaseService implements TranslateLoader {
  contentHeader = new HttpHeaders( { 'Content-Type': 'application/json', 'Access-Control-Allow-Origin': '*' } );

  constructor( private http: HttpClient ) {
    super();
  }

  getTranslation( lang: string ): Observable<any> {
    return new Observable( observer => {
      return this.http.get<any>( this.createURI( ApiURLS.LABEL, { language: lang } )).subscribe( ( res: Response ) => {
        observer.next( res );
        observer.complete();
      } );
    } );
  }
}

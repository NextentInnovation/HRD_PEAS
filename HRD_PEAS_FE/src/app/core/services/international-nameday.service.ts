import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BaseService } from './base.service';
import { map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';

/***
 * This service implements the https://api.abalin.net/ international nameday api
 */
@Injectable( {
  providedIn: 'root'
} )
export class InternationalNamedayService extends BaseService {

  private static API_URL: string = environment.API_HOST + '/sas-mdm/name-day';
  private static TODAY_API_URL: string = InternationalNamedayService.API_URL + '/today/';

  constructor( private http: HttpClient ) {
    super();
  }

  /**
   * Get a nameday for today
   * @Param: country: string filter available country (us cz sk pl fr hu hr se at it de es )
   * @Return: names of string
   */
  getTodayNameday( country: string = 'hu' ) {
    return this.http.get<any>( InternationalNamedayService.TODAY_API_URL + country  )
               .pipe(
                 map(
                   response => {
                     return response.data[ 'name_' + country ];
                   }
                 )
               );
  }


}

import { Injectable } from '@angular/core';

@Injectable()
export class SearchClearUtil {

  public static clear(searchObj: object) {
    console.log(searchObj);
    Object.keys( searchObj ).forEach( key => {
      searchObj[ key ] = null;
    } );
    console.log(searchObj);
  }
}
